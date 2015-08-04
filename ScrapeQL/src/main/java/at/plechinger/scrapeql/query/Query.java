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

package at.plechinger.scrapeql.query;

import at.plechinger.scrapeql.ScrapeParser;
import at.plechinger.scrapeql.expression.Expression;
import at.plechinger.scrapeql.relation.Relation;
import at.plechinger.scrapeql.relation.RelationFactory;
import com.google.common.collect.Lists;

import java.util.*;

/**
 * Created by lukas on 18.05.15.
 */
public class Query {

    private List<Expression> expressionList;
    private List<DataContext> dataContexts= Lists.newLinkedList();

    public Query select(Expression... expressions) {
        expressionList= Arrays.asList(expressions);
        return this;
    }

    public Query from(DataContext context) {
        dataContexts.add(context);
        return this;
    }

    private DataContext findDataContext(String name){
        for(DataContext context:dataContexts){
            if(name.equals(context.getName())){
                return context;
            }
        }
        throw new RuntimeException("Context does not exist: "+name);
    }


    public Relation execute(){

        Map<DataContext,List<Expression>> expressionContexts=new HashMap<>();

        return null;
    }
}
