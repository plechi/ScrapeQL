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

import at.plechinger.scrapeql.query.expression.ListVariable;
import at.plechinger.scrapeql.query.expression.Variable;
import at.plechinger.scrapeql.query.expression.VariableBuilder;
import at.plechinger.scrapeql.query.statement.LoadStatement;
import at.plechinger.scrapeql.query.statement.SelectStatement;
import at.plechinger.scrapeql.query.statement.SelectFirstStatement;
import at.plechinger.scrapeql.query.statement.SelectEveryStatement;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lukas Plechinger
 */
public class Query {

    private QueryContext context = new QueryContext();

    private VariableBuilder variableBuilder = new VariableBuilder(context);

    public Query load(String url) {
        context.setLoadExpression(new LoadStatement(url));
        return this;
    }

    public SelectFirstStatement select(Variable... selects) {
        return select(Arrays.asList(selects));
    }

    public SelectFirstStatement select(List<Variable> selects) {
        SelectFirstStatement exp = new SelectFirstStatement(selects, this);
        context.addSelect(exp);
        return exp;
    }

    public SelectEveryStatement selectEvery(Variable... selects) {
        return selectEvery(Arrays.asList(selects));
    }

    public SelectEveryStatement selectEvery(List<Variable> selects) {
        SelectEveryStatement exp = new SelectEveryStatement(selects, this);
        context.addSelect(exp);
        return exp;
    }

    public Query output(String... outputs) {
        for (String exp : outputs) {
            context.addOutputVariable(exp);
        }
        return this;
    }

    public QueryContext getContext() {
        return context;
    }

    public VariableBuilder getVariableBuilder() {
        return variableBuilder;
    }

    
    
    /**
     * Default execution (eg. for JSON)
     * @return 
     */
    public Map<String, Object> execute() {
        //ececute load statement
        context.getLoadExpression().execute(context);

        for (SelectStatement select : context.getSelects()) {
            select.execute(context);
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();

        for (String variable : context.getOutputVariables()) {
            Variable varElement = context.getVariable(variable);
            if (varElement instanceof ListVariable) {
                resultMap.put(variable, ((ListVariable) context.getVariable(variable)).getList());
            } else {
                resultMap.put(variable, context.getVariable(variable).getValue());
            }
        }

        return resultMap;
    }
    
    /**
     * Single Row output (linke SQL RDBMS)
     * @return 
     */
    public Table<Integer, String,String> executeTable(){
        Preconditions.checkArgument(context.getOutputVariables().size()>0,"No output variable is set.");
        
        
        Table<Integer,String,String> table=HashBasedTable.create();
        
        //fetch first (ignore others if multiple outputs)
        String variableName=context.getOutputVariables().get(0);
        Variable variable=context.getVariable(variableName);
        
        if(variable instanceof ListVariable){
            
            ListVariable<Map<String, String>> listVar=(ListVariable) variable;
            
            List<Map<String,String>> originalList=listVar.getList();
            
            for(int i=0;i<originalList.size();i++){
               for(Map.Entry<String,String> cell:originalList.get(i).entrySet()){
                   table.put(i, cell.getKey(), cell.getValue());
               } 
            }

        }else{
            table.put(0, variableName, variable.getValue());
        }
        
        
        return table;
    }
}
