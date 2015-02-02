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
        Preconditions.checkArgument(parameters.size() >= 2 && parameters.size() <= 3, "date(): argument count: %d", parameters.size());
        Preconditions.checkArgument(parameters.get(1) instanceof StringVariable, "Second parameter must be a string.");
        
        
        try {

            //parse input
            Variable param0=parameters.get(0);
            //execute input, because is could be a selector
            if(param0 instanceof RootAwareVariable){
                ((RootAwareVariable) param0).setRoot(baseElement);
            }
            
            param0.execute(context);
            
            
            Variable param1=parameters.get(1);
            
            
            SimpleDateFormat input = new SimpleDateFormat(param1.getValue());

            Date date = input.parse(param0.getValue());

            //parse output
            SimpleDateFormat outputFormat;

            if (parameters.size() == 3) {
                Preconditions.checkArgument(parameters.get(2) instanceof StringVariable, "Third parameter must be a string.");
                Variable param2=parameters.get(3);
                outputFormat = new SimpleDateFormat(param2.getValue());
            } else {
                outputFormat = new SimpleDateFormat(BASE_FORMAT);
            }

            return new StringVariable(outputFormat.format(date));
        } catch (ParseException ex) {
            return new StringVariable(parameters.get(0).getValue());
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

}
