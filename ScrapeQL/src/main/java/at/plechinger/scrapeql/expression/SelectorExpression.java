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


import com.google.common.base.Preconditions;
import org.jsoup.nodes.Element;

/**
 * Created by lukas on 15.05.15.
 */
public class SelectorExpression extends AbstractNamedExpression<Element> {

    private Expression<String> selector;

    private String dataContext;

    public SelectorExpression(Expression<String> selector) {
        super("$");
        this.selector=selector;
    }

    public SelectorExpression(Expression<String> selector, String dataContext) {
       this(selector);
        this.dataContext=dataContext;
    }

    @Override
    public Variable<Element> execute(ExpressionContext expressionContext) {
        Preconditions.checkArgument(expressionContext.getDataContextName().equals(dataContext),"You just can select from one context at a time");
        return expressionContext.select(selector.execute(expressionContext));
    }

    public SelectorExpression ctx(String dataContext) {
        this.dataContext=dataContext;
        return this;
    }
}
