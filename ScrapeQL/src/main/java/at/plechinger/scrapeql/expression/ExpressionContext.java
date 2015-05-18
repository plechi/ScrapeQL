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

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by lukas on 18.05.15.
 */
public class ExpressionContext {

    private String dataContextName;

    private Element rootElement;

    public ExpressionContext(String dataContextName, Element rootElement) {
        this.dataContextName = dataContextName;
        this.rootElement = rootElement;
    }

    public String getDataContextName() {
        return dataContextName;
    }

    public Variable<Element> select(Variable<String> query){
        if(query.isNull()){
            return Variable.NULL;
        }

        Elements select=rootElement.select(query.getValue());

        if(!select.isEmpty()){
            return new Variable<>(select.first());
        }
        return Variable.NULL;
    }
}
