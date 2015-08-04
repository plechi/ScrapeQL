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
import at.plechinger.scrapeql.loader.html.HtmlLoaderFunction;
import at.plechinger.scrapeql.type.StringValue;
import at.plechinger.scrapeql.type.Value;
import com.google.common.collect.Lists;

import java.text.ParseException;
import java.util.List;

/**
 * Created by lukas on 04.08.15.
 */
public class SelectQuery {
/*
    private List<Expression> expressions = Lists.newLinkedList();

    private LoaderExpression loader;

    public SelectQuery(Expression... expressions) {
        this.expressions = Lists.newArrayList(expressions);
    }

    public SelectQuery from(LoaderExpression loader) {
        this.loader = loader;
        return this;
    }

    public void execute() throws ScrapeQLException, ParseException {
        Context ctx = new Context();
        loader.evaluate(ctx);

        for (Expression expression : expressions) {
            Value result = expression.evaluate(ctx);
            System.out.println(result.getDataTypeName());
            System.out.println(result.getStringValue());
        }

    }

    public static void main(String[] args) throws ParseException, ScrapeQLException {
        FunctionRepository.instance().register(new HtmlLoaderFunction());


        SelectQuery query = new SelectQuery(new SelectorExpression(new ValueExpression(new StringValue("a")),
                new EntityExpression("test")),
                new SelectorExpression(new ValueExpression(new StringValue("a")),
                        new EntityExpression("test")));


        query.from(new LoaderExpression("test",
                        new SelectorExpression(new ValueExpression(
                                new StringValue(".toc ul.sectlevel0>li")
                        ), new FunctionExpression(
                                "LOAD_HTML",
                                new ValueExpression(
                                        new StringValue("http://docs.spring.io/spring-data/jpa/docs/1.8.x/reference/html/")
                                )
                        )
                        )
                )
        );


        query.execute();

    }*/
}
