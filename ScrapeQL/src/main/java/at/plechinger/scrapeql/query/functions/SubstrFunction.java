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
package at.plechinger.scrapeql.query.functions;

import at.plechinger.scrapeql.query.QueryContext;
import at.plechinger.scrapeql.query.expression.RootAwareVariable;
import at.plechinger.scrapeql.query.expression.StringVariable;
import at.plechinger.scrapeql.query.expression.Variable;
import com.google.common.base.Preconditions;
import java.util.List;
import org.jsoup.nodes.Element;

/**
 *
 * @author Lukas Plechinger
 */
class SubstrFunction implements FunctionDefinition {

    private static final String NAME = "substr";

    @Override
    public Variable execute(QueryContext context, List<Variable> parameters, Element baseElement) {
        Preconditions.checkArgument(parameters.size() >= 2 && parameters.size() <= 3, "Wrong parameter count %d.", parameters.size());
        Preconditions.checkArgument(parameters.get(1) instanceof StringVariable, "Parameter 2 must be a String value");

        Variable input = parameters.get(0);

        input.execute(context);
        String result = input.getValue();

        int start;
        String parameter1 = parameters.get(1).getValue();
        try {
            start = Integer.parseInt(parameter1);
        } catch (NumberFormatException e) {
            start = result.indexOf(parameter1)+parameter1.length();
        }

        int end = result.length();

        if (parameters.size() == 3) {
            Variable p2 = parameters.get(2);
            Preconditions.checkArgument(p2 instanceof StringVariable, "Parameter 3 must be a String value");
            String parameter2 = p2.getValue();
            try {
                end = Integer.parseInt(parameter2);
            } catch (NumberFormatException e) {
                end = result.indexOf(parameter2, start);
            }
        }

        return new StringVariable(result.substring(start, end));
    }

    @Override
    public String getName() {
        return NAME;
    }

}
