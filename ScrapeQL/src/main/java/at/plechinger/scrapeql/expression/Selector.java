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

package at.plechinger.scrapeql.expression;


import at.plechinger.scrapeql.query.DataContext;
import at.plechinger.scrapeql.query.QueryContext;
import org.jsoup.nodes.Element;

/**
 * Created by lukas on 15.05.15.
 */
public class Selector extends AbstractNamedExpression<Element> {

    private Expression<String> selector;

    private String dataContext;

    public Selector(Expression<String> selector) {
        super("$");
        this.selector=selector;
    }

    public Selector(Expression<String> selector, String dataContext) {
       this(selector);
        this.dataContext=dataContext;
    }

    @Override
    public Variable<Element> execute(QueryContext queryContext) {
        DataContext ctx=queryContext.getDataContext(dataContext);
        return ctx.select(selector.execute(queryContext));
    }

    public Selector ctx(String dataContext) {
        this.dataContext=dataContext;
        return this;
    }
}
