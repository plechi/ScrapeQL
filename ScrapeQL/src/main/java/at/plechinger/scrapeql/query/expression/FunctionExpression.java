/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Lukas Plechinger
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

package at.plechinger.scrapeql.query.expression;

import at.plechinger.scrapeql.query.QueryContext;
import at.plechinger.scrapeql.query.expression.function.FunctionDefinition;
import at.plechinger.scrapeql.query.expression.function.FunctionRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by lukas on 12.05.15.
 */
public class FunctionExpression extends AbstractExpression implements Expression {

    private FunctionDefinition function;

    private List<Expression> parameters=Collections.emptyList();

    public FunctionExpression(String functionName, Expression... parameters){
       this(functionName, Arrays.asList(parameters));
    }

    public FunctionExpression(String functionName, List<Expression> parameters){
        this.function= FunctionRepository.repository().getDefinedFunction(functionName);
        if(parameters!=null && !parameters.isEmpty()){
            this.parameters =parameters;
        }
    }

    @Override
    public Variable express(QueryContext ctx) {
        //generate variables
        List<Variable> variables=new ArrayList<>(parameters.size());
        for (Expression param:parameters){
            variables.add(param.express(ctx));
        }
        return function.execute(ctx,variables);
    }
}
