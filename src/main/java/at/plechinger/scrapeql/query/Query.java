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

import at.plechinger.scrapeql.query.variable.Variable;
import at.plechinger.scrapeql.query.variable.VariableBuilder;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Lukas Plechinger
 */
public class Query {
    
    private QueryContext context=new QueryContext();
    
    private VariableBuilder variableBuilder=new VariableBuilder(context);
    
    public Query load(String url){
        context.setLoadExpression(new LoadExpression(url));
        return this;
    }
    
    public SelectFirstExpression select(Variable... selects){
        return select(Arrays.asList(selects));
    }
    
    public SelectFirstExpression select(List<Variable> selects){
        SelectFirstExpression exp=new SelectFirstExpression(selects, this);
        context.addSelect(exp);
        return exp;
    }
    
    public SelectEveryExpression selectEvery(Variable... selects){
        SelectEveryExpression exp=new SelectEveryExpression(Arrays.asList(selects), this);
        context.addSelect(exp);
        return exp;
    }
    
    public Query output(String... outputs){
        for(String exp:outputs){
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
    
    public Map<String,Object> execute(){
        //ececute load statement
        context.getLoadExpression().execute(context);
        
        for(SelectExpression select:context.getSelects()){
            select.execute(context);
        }
        
        Map<String,Object> resultMap=new LinkedHashMap<>();
        
        for(String variable:context.getOutputVariables()){
            resultMap.put(variable, context.getVariable(variable).getValue());
        }
        
        return resultMap;
    }
}
