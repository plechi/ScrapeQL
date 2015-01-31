/*
 * The MIT License
 *
 * Copyright 2015 lukas.
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
package at.plechinger.scrapeql.query.variable;

import at.plechinger.scrapeql.query.QueryContext;
import com.google.common.base.Preconditions;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author lukas
 */
public class SelectorVariable implements Variable {

    private String selector;
    private String alias;

    private Element result = null;

    private Element root = null;

    public SelectorVariable(String selector) {
        this.selector = selector;
    }

    public void setRoot(Element rootElement) {
        this.root = rootElement;
    }

    public Element getElement() {
        Preconditions.checkNotNull(result, "Selector '%s' as not found anything", selector);
        return result;
    }

    @Override
    public String getValue() {
        return getElement().text();
    }

    @Override
    public SelectorVariable as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void execute(QueryContext context) {
        Element r;
        if (root != null) {
            r = root;
        } else {
            r = context.getRootElement();
        }
        Elements results = r.select(selector);

        if (results != null && results.size() > 0) {
            result = results.first();
        }
        if (alias != null) {
            context.addVariable(alias, this);
        }
    }

}
