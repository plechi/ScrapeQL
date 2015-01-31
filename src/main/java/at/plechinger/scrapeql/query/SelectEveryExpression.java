/*
 * The MIT License
 *
 * Copyright 2015 lukas.
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
import java.util.List;

/**
 *
 * @author lukas
 */
public class SelectEveryExpression extends AbstractQueryAware implements SelectExpression{
    private List<Variable> elements;
    
    private String from;
    
    private String into;
    
    public SelectEveryExpression(List<Variable> elements, Query rootQuery){
        super(rootQuery);
        this.elements=elements;
    }
    
    public SelectEveryExpression from(String from){
        this.from=from;
        return this;
    }
    
    public Query into(String variable){
        this.into=variable;
        return rootQuery;
    }
    
    
    @Override
    public void execute(QueryContext context) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
