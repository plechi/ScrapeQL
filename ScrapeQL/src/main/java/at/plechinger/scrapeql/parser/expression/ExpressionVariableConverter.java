/*
 * The MIT License
 *
 * Copyright 2015 Lukas Plechinger.
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
package at.plechinger.scrapeql.parser.expression;

import at.plechinger.scrapeql.lang.ScrapeQLParser;
import at.plechinger.scrapeql.query.variable.StringVariable;
import at.plechinger.scrapeql.query.variable.Variable;
import com.google.common.collect.Lists;
import java.util.List;

/**
 *
 * @author lukas
 */
public class ExpressionVariableConverter {

    private static final List<ExpressionConverter> converters = Lists.newLinkedList();

    static {
        //Add converters
        converters.add(new SelectorConverter());
        converters.add(new StringConverter());
        converters.add(new NamedVariableConverter());
        converters.add(new FunctionConverter());
    }

    public Variable convert(ScrapeQLParser.ExprContext ctx) {
        for (ExpressionConverter converter : converters) {
            if (converter.isSuited(ctx)) {
                return converter.convert(ctx, this);
            }
        }

        //FIXME: better way to handle unknown expressions (maybe Exception)
        return new StringVariable(ctx.getText());
    }
}
