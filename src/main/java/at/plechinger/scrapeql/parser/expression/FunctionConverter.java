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
import at.plechinger.scrapeql.query.variable.FunctionVariable;
import at.plechinger.scrapeql.query.variable.Variable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lukas
 */
public class FunctionConverter implements ExpressionConverter {

    @Override
    public boolean isSuited(ScrapeQLParser.ExprContext ctx) {
        return ctx.function_name() != null;
    }

    @Override
    public Variable convert(ScrapeQLParser.ExprContext ctx, ExpressionVariableConverter converter) {
        List<Variable> variableList = new ArrayList<>(ctx.expr().size());
        for (ScrapeQLParser.ExprContext childCtx : ctx.expr()) {
            variableList.add(converter.convert(childCtx));
        }
        return new FunctionVariable(ctx.function_name().getText(), variableList);
    }

}
