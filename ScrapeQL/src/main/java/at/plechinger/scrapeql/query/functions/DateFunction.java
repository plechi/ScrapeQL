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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.jsoup.nodes.Element;

/**
 *
 * @author Lukas Plechinger
 */
class DateFunction implements FunctionDefinition {

    private static final String NAME = "date";

    private static final String BASE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Variable execute(QueryContext context, List<Variable> parameters, Element baseElement) {
        //parameter count must be 2 or 3
        Preconditions.checkArgument(parameters.size() <= 2, "date(): argument count: %d", parameters.size());

        try {
            Variable toFormat = parameters.get(0);

            if (toFormat instanceof RootAwareVariable) {
                ((RootAwareVariable) toFormat).setRoot(baseElement);
            }

            toFormat.execute(context);

            SimpleDateFormat format;
            //is format parameter set?
            if (parameters.size() == 2) {
                Preconditions.checkArgument(parameters.get(1) instanceof StringVariable, "Second parameter must be a string.");
                Variable param1 = parameters.get(1);
               format = new SimpleDateFormat(param1.getValue());
            }else{ //no format set, base format
                format=new SimpleDateFormat(BASE_FORMAT);
            }
            
            Date formatted=format.parse(toFormat.getValue());
            
            return new StringVariable(Long.toString(formatted.getTime()));

        } catch (ParseException ex) {
            return new StringVariable(parameters.get(0).getValue());
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

}
