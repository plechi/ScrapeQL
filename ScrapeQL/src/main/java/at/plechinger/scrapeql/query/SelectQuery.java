/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Lukas Plechinger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package at.plechinger.scrapeql.query;

import at.plechinger.scrapeql.ScrapeParser;
import at.plechinger.scrapeql.ScrapeQLException;
import at.plechinger.scrapeql.expression.RelationExpression;
import at.plechinger.scrapeql.expression.StarExpression;
import at.plechinger.scrapeql.function.FunctionRepository;
import at.plechinger.scrapeql.function.impl.Attr;
import at.plechinger.scrapeql.ScrapeQLException;
import at.plechinger.scrapeql.context.Context;
import at.plechinger.scrapeql.expression.Expression;
import at.plechinger.scrapeql.expression.RelationExpression;
import at.plechinger.scrapeql.expression.StarExpression;
import at.plechinger.scrapeql.filter.Filter;
import at.plechinger.scrapeql.function.FunctionRepository;
import at.plechinger.scrapeql.function.impl.Concat;
import at.plechinger.scrapeql.function.impl.StringFunctions;
import at.plechinger.scrapeql.function.impl.UrlFn;
import at.plechinger.scrapeql.loader.html.HtmlLoaderFunction;
import at.plechinger.scrapeql.function.impl.Attr;
import at.plechinger.scrapeql.relation.Relation;
import at.plechinger.scrapeql.relation.TableRelation;
import at.plechinger.scrapeql.util.Timer;
import at.plechinger.scrapeql.value.Value;
import at.plechinger.scrapeql.util.Mapper;
import at.plechinger.scrapeql.relation.Relation;
import at.plechinger.scrapeql.relation.TableRelation;
import at.plechinger.scrapeql.util.Timer;
import at.plechinger.scrapeql.value.Value;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by lukas on 04.08.15.
 */
public class SelectQuery {

    private List<Expression> expressions = Lists.newLinkedList();

    private List<RelationExpression> relations = Lists.newLinkedList();

    private Optional<Filter> where = Optional.absent();

    public SelectQuery(Expression... expressions) {
        this.expressions = Lists.newArrayList(expressions);
    }

    public SelectQuery(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public SelectQuery from(RelationExpression... relations) {
        this.relations.addAll(Lists.newArrayList(relations));
        return this;
    }

    public SelectQuery from(List<RelationExpression> relations) {
        this.relations = relations;
        return this;
    }

    public SelectQuery where(Filter where) {
        this.where = Optional.fromNullable(where);
        return this;
    }

    public void execute() throws ScrapeQLException, ParseException {
        final Context context = new Context();


        List<Callable<Relation<Value>>> callables = Mapper.map(relations, from -> () -> from.evaluate(context).getValue());

        Timer timer = new Timer();
        Relation<Value> joinRelation=null;
        try {

            ExecutorService executor = Executors.newFixedThreadPool(4);
            List<Future<Relation<Value>>> results = executor.invokeAll(callables);
            executor.shutdown();

            for (Future<Relation<Value>> result : results) {
                System.out.println("result:"+result.get().toString());
                if(joinRelation!=null){
                    joinRelation=joinRelation.cartesian(result.get());
                }else{
                    joinRelation=result.get();
                }
            }

            System.out.println("fetch and join in" + timer.stop());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new ScrapeQLException("Error while loading relation " + e);
        }


        List<StarExpression> starExpressions = Lists.newLinkedList();

        for (Expression exp : expressions) {
            if (exp.getClass().isAssignableFrom(StarExpression.class)) {
                starExpressions.add(StarExpression.class.cast(exp));
            }
        }


        //relational "projection"
        TableRelation finalRelation = new TableRelation();

        timer.stop();
        int finalRow = 0;
        for (int row = 0; row < joinRelation.rows(); row++) {
            int column = 0;
            context.setColumns(joinRelation.getRow(row).getColumns());

            if (where.isPresent() && !where.get().filter(context)) {
                continue;
            }

            for (Expression exp : expressions) {
                if (!exp.getClass().isAssignableFrom(StarExpression.class)) {
                    Value result = exp.evaluate(context);

                    String columnName = (result.getVariableName() != null) ? result.getVariableName() : "column_" + column;
                    finalRelation.set(finalRow, columnName, result);
                    column++;
                }
            }

            for (StarExpression stex : starExpressions) {
                for (String col : joinRelation.getColumnNames()) {
                    if (stex.isVisible(col)) {
                        finalRelation.set(finalRow, col, joinRelation.getRow(row).getValue(col));
                        column++;
                    }
                }
            }
            finalRow++;
        }
        System.out.println("projected in " + timer.stop());
        context.clearColumns();

        System.out.println(finalRelation.toPrettyString());
    }


    public static void main(String[] args) throws Exception {
        FunctionRepository.instance().register(new HtmlLoaderFunction());
        FunctionRepository.instance().register(new Attr());
        FunctionRepository.instance().register(new UrlFn());
        FunctionRepository.instance().register(new Concat());
        FunctionRepository.instance().registerFunctions(StringFunctions.class);


        ScrapeParser parser = new ScrapeParser();

        /*String sql = "SELECT tracks.interpret, tracks1.title, tracks.time, tracks1.time " +
                "FROM (" +
                "LOAD $('td:eq(0)') AS time, " +
                "$('td:eq(2)') AS interpret " +
                "FROM load_html(url('http://soundportal.at/service/now-on-air/')) $('.tx-abanowonair-pi1 .contenttable tr')) AS tracks, " +
                "(LOAD $('td:eq(0)') AS time, " +
                "$('td:eq(1)') AS title " +
                "FROM load_html(url('http://soundportal.at/service/now-on-air/')) $('.tx-abanowonair-pi1 .contenttable tr')) AS tracks1 " +
                "WHERE tracks.time=tracks1.time";*/


        /*String sql="SELECT tag_text(wiki.key), tag_text(wiki.value) \n" +
                "FROM ( " +
                "    LOAD $('th>*') AS key, $('td>*') AS value " +
                "    FROM load_html(url('https://en.wikipedia.org/wiki/Java_(programming_language)'))" +
                "    $('table.infobox>tbody>tr')" +
                ") AS wiki";*/


        String sql= "SELECT test.li" +
                " FROM (LOAD $('li') FROM load_html(TXT>>>\n" +
                "<ul>\n" +
                "   <li>test1</li>\n" +
                "   <li>test2</li>\n" +
                "</ul>" +
                "<<<TXT)) AS test";


        SelectQuery qu = parser.parse(sql);

        qu.execute();


    }
}
