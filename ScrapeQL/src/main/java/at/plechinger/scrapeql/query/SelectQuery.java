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

import at.plechinger.scrapeql.ScrapeQLException;
import at.plechinger.scrapeql.context.Context;
import at.plechinger.scrapeql.expression.*;
import at.plechinger.scrapeql.function.FunctionRepository;
import at.plechinger.scrapeql.function.impl.Attr;
import at.plechinger.scrapeql.function.impl.Concat;
import at.plechinger.scrapeql.function.impl.DateFormat;
import at.plechinger.scrapeql.function.impl.Lower;
import at.plechinger.scrapeql.loader.html.HtmlLoaderFunction;
import at.plechinger.scrapeql.relation.Relation;
import at.plechinger.scrapeql.relation.Selector;
import at.plechinger.scrapeql.type.StringValue;
import at.plechinger.scrapeql.type.Value;
import at.plechinger.scrapeql.util.Timer;
import com.google.common.collect.Lists;

import java.text.ParseException;
import java.util.List;

/**
 * Created by lukas on 04.08.15.
 */
public class SelectQuery {

    private List<Expression> expressions = Lists.newLinkedList();

    private List<RelationExpression> relations = Lists.newLinkedList();

    public SelectQuery(Expression... expressions) {
        this.expressions = Lists.newArrayList(expressions);
    }

    public SelectQuery from(RelationExpression... relations) {
        this.relations.addAll(Lists.newArrayList(relations));
        return this;
    }

    public void execute() throws ScrapeQLException, ParseException {
        Context context = new Context();

        Relation joinRelation = new Relation();

        Timer timer = new Timer();
        for (RelationExpression relex : relations) {
            timer.stop();
            Relation value = relex.evaluate(context).getValue();
            System.out.println("Fetched in " + timer.stop());
            joinRelation = joinRelation.join(value);
            System.out.println("joined in " + timer.stop());
        }

        timer.stop();
        List<StarExpression> starExpressions = Lists.newLinkedList();

        for (Expression exp : expressions) {
            if (exp.getClass().isAssignableFrom(StarExpression.class)) {
                starExpressions.add(StarExpression.class.cast(exp));
            }
        }


        //relational "projection"
        Relation finalRelation = new Relation();
        for (int row = 0; row < joinRelation.rows(); row++) {

            int column = 0;
            context.setColumns(joinRelation.getRow(row));
            for (Expression exp : expressions) {
                if (!exp.getClass().isAssignableFrom(StarExpression.class)) {
                    Value result = exp.evaluate(context);

                    String columnName = (result.getVariableName() != null) ? result.getVariableName() : "column_" + column;
                    finalRelation.set(row, columnName, result);
                    column++;
                }
            }

            for (StarExpression stex : starExpressions) {
                for (String col : joinRelation.getColumns()) {
                    if (stex.isVisible(col)) {
                        finalRelation.set(row, col, joinRelation.getRow(row).get(col));
                        column++;
                    }
                }
            }
        }
        context.clearColumns();

        System.out.println(finalRelation.toPrettyString());
    }


    public static void main(String[] args) throws Exception {
        FunctionRepository.instance().register(new HtmlLoaderFunction());
        FunctionRepository.instance().register(new Concat());
        FunctionRepository.instance().register(new DateFormat());
        FunctionRepository.instance().register(new Attr());
        FunctionRepository.instance().register(new Lower());


        SelectQuery query = new SelectQuery(new AliasExpression(
                new FunctionExpression("lower",
                new FunctionExpression("CONCAT",
                        new VariableExpression("tracks.title"),
                        new ValueExpression(new StringValue(" test ")),
                        new VariableExpression("tracks1.time")
                )), "concat"
        ),
                new FunctionExpression("date_format", new VariableExpression("tracks1.time"),
                        new ValueExpression(new StringValue("dd.MM.yyyy HH:mm:ss"))
                ),
                new FunctionExpression("attr",
                        new VariableExpression("tracks.amazon_link"),
                        new ValueExpression(new StringValue("href"))
                )
        );

        query.from(new RelationExpression(
                        new Selector("td:eq(0)", "time"),
                        new Selector("td:eq(1)", "title"),
                        new Selector("td:eq(2)", "interpret"),
                        new Selector("td.more>a:eq(0)", "amazon_link")
                ).from(new FunctionExpression(
                        "LOAD_HTML",
                        new ValueExpression(
                                new StringValue("http://soundportal.at/service/now-on-air/")
                        )
                )
                        , new Selector(".tx-abanowonair-pi1 .contenttable tr")).as("tracks")
        ).from(new RelationExpression(
                        new Selector("td:eq(0)", "time"),
                        new Selector("td:eq(1)", "title"),
                        new Selector("td:eq(2", "interpret")
                ).from(new FunctionExpression(
                        "LOAD_HTML",
                        new ValueExpression(
                                new StringValue("http://soundportal.at/service/now-on-air/")
                        )
                )
                        , new Selector(".tx-abanowonair-pi1 .contenttable tr")).as("tracks1")
        );


        query.execute();

    }
}
