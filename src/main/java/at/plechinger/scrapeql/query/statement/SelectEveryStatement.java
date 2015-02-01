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

import at.plechinger.scrapeql.Utils;
import at.plechinger.scrapeql.query.Query;
import at.plechinger.scrapeql.query.QueryContext;
import at.plechinger.scrapeql.query.variable.ListVariable;
import at.plechinger.scrapeql.query.variable.SelectorVariable;
import at.plechinger.scrapeql.query.variable.Variable;
import com.google.common.base.Preconditions;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Lukas Plechinger
 */
public class SelectEveryStatement extends AbstractSelectStatement implements SelectStatement {

    private String into;

    private String in;

    public SelectEveryStatement(List<Variable> elements, Query rootQuery) {
        super(elements, rootQuery);
    }

    public SelectEveryStatement in(String in) {
        this.in = in;
        return this;
    }

    public SelectEveryStatement into(String variable) {
        this.into = variable;
        return this;
    }

    @Override
    public void execute(QueryContext context) {
        SelectorVariable fromVariable = context.getVariable(from, SelectorVariable.class);

        Element fromElement = fromVariable.getElement();
        RowContext rowContext = new RowContext(context);

        Elements inElements = fromElement.select(in);

        List<Map<String, String>> resultList = new LinkedList<>();

        for (Element rowElement : inElements) {
            rowContext.nextRow();
            for (Variable var : elements) {
                if (var instanceof SelectorVariable) {
                    SelectorVariable s = (SelectorVariable) var;
                    s.setRoot(rowElement);
                }
                var.execute(rowContext);
            }
            resultList.add(rowContext.getRow());
        }

        context.addVariable(into, new ListVariable<>(resultList));
    }

    /**
     * Inner class for intercepting variable adding
     */
    private static class RowContext extends QueryContext {

        private QueryContext parent;

        private Map<String, String> rowVariables = new LinkedHashMap<>();

        public RowContext(QueryContext parent) {
            this.parent = parent;
        }

        @Override
        public void addVariable(String name, Variable value) {
            Preconditions.checkArgument(!rowVariables.containsKey(name), "Variable '%s' is already defined.", name);
            Preconditions.checkArgument(Utils.isValidVariableName(name), "Variable %s contains illegal characters.", name);
            rowVariables.put(name, value.getValue());
        }

        @Override
        public Variable getVariable(String variableName) {
            return parent.getVariable(variableName);
        }

        @Override
        public <T extends Variable> T getVariable(String variableName, Class<T> clazz) {
            return parent.getVariable(variableName, clazz);
        }

        @Override
        public Element getRootElement() {
            return parent.getRootElement();
        }

        public Map<String, String> getRow() {
            return rowVariables;
        }

        public void nextRow() {
            rowVariables = new LinkedHashMap<>();
        }

    }
}
