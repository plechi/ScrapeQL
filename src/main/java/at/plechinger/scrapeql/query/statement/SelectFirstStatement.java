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
package at.plechinger.scrapeql.query.statement;

import at.plechinger.scrapeql.query.Query;
import at.plechinger.scrapeql.query.QueryContext;
import at.plechinger.scrapeql.query.variable.SelectorVariable;
import at.plechinger.scrapeql.query.variable.Variable;
import java.util.List;
import lombok.extern.log4j.Log4j;
import org.jsoup.nodes.Element;

/**
 *
 * @author Lukas Plechinger
 */
@Log4j
public class SelectFirstStatement extends AbstractSelectStatement implements SelectStatement {

    public SelectFirstStatement(List<Variable> elements, Query rootQuery) {
        super(elements,rootQuery);
    }
    
    @Override
    public void execute(QueryContext context) {
        SelectorVariable fromVariable = context.getVariable(from, SelectorVariable.class);

        Element fromElement = fromVariable.getElement();
        for (Variable var : elements) {

            if (var instanceof SelectorVariable) {
                SelectorVariable s = (SelectorVariable) var;
                s.setRoot(fromElement);
            }

            var.execute(context);
        }
    }
}
