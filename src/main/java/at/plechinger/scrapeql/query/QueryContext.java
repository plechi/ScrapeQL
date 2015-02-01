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
package at.plechinger.scrapeql.query;

import at.plechinger.scrapeql.query.statement.LoadStatement;
import at.plechinger.scrapeql.Utils;
import at.plechinger.scrapeql.query.statement.SelectStatement;
import at.plechinger.scrapeql.query.variable.Variable;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.nodes.Element;

/**
 *
 * @author Lukas Plechinger
 */
public class QueryContext {
    
    private Element rootElement;

    private LoadStatement load;

    private List<SelectStatement> selects = new ArrayList<>();

    private Map<String, Variable> variables = new HashMap<>();

    private List<String> outputVariables = new ArrayList<>();

    public void addSelect(SelectStatement exp) {
        selects.add(exp);
    }

    public void addVariable(String name, Variable value) {
        Preconditions.checkArgument(!variables.containsKey(name), "Variable '%s' is already defined.", name);
        Preconditions.checkArgument(Utils.isValidVariableName(name), "Variable %s contains illegal characters.", name);
        variables.put(name, value);
    }

    public void addOutputVariable(String variable) {
        outputVariables.add(variable);
    }

    public void setLoadExpression(LoadStatement loadExpression) {
        this.load = loadExpression;
    }

    public LoadStatement getLoadExpression() {
        return load;
    }

    public List<SelectStatement> getSelects() {
        return selects;
    }

    public List<String> getOutputVariables() {
        return outputVariables;
    }

    public <T extends Variable> T getVariable(String variableName, Class<T> clazz) {
        Preconditions.checkArgument(variables.containsKey(variableName), "Variable '%s' is not defined.", variableName);
        Variable var = variables.get(variableName);
        Preconditions.checkArgument(clazz.isAssignableFrom(clazz), "Variable type is %s but must be %s.", var.getClass().getName(), clazz.getName());
        return (T) variables.get(variableName);
    }

    public Variable getVariable(String variableName) {
        return getVariable(variableName, Variable.class);
    }

    public Element getRootElement() {
        return rootElement;
    }

    public void setRootElement(Element root) {
        this.rootElement = root;
    }
}
