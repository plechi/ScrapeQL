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
import at.plechinger.scrapeql.expression.Expression;
import at.plechinger.scrapeql.expression.FunctionExpression;
import at.plechinger.scrapeql.expression.RelationExpression;
import at.plechinger.scrapeql.expression.ValueExpression;
import at.plechinger.scrapeql.function.FunctionRepository;
import at.plechinger.scrapeql.loader.html.HtmlLoaderFunction;
import at.plechinger.scrapeql.relation.Relation;
import at.plechinger.scrapeql.relation.Selector;
import at.plechinger.scrapeql.type.StringValue;
import com.google.common.collect.Lists;

import java.text.ParseException;
import java.util.List;

/**
 * Created by lukas on 04.08.15.
 */
public class SelectQuery {

    private List<Expression> expressions = Lists.newLinkedList();

    private List<RelationExpression> relations=Lists.newLinkedList();

    public SelectQuery(Expression... expressions) {
        this.expressions = Lists.newArrayList(expressions);
    }

    public SelectQuery from(RelationExpression... relations) {
        this.relations.addAll(Lists.newArrayList(relations));
        return this;
    }

    public void execute() throws ScrapeQLException, ParseException {
        Context context = new Context();

        Relation finalRelation = new Relation();

        for (RelationExpression relex : relations) {
            System.out.println(relex.toString());
            Relation value = relex.evaluate(context).getValue();
            System.out.println("RELATION"+value.toPrettyString());
            finalRelation=finalRelation.join(value);

            System.out.println("fin"+finalRelation.toPrettyString());
        }

        System.out.println(finalRelation.toPrettyString());
    }

    public static void main(String[] args) throws Exception {
        FunctionRepository.instance().register(new HtmlLoaderFunction());


        SelectQuery query = new SelectQuery();

        query.from(new RelationExpression(
                        new Selector("td:eq(0)", "time"),
                        new Selector("td:eq(1)", "title"),
                        new Selector("td:eq(2", "interpret")
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
