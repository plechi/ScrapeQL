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

package at.plechinger.scrapeql.function.impl;

import at.plechinger.scrapeql.ScrapeQLException;
import at.plechinger.scrapeql.function.Function;
import at.plechinger.scrapeql.type.Value;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lukas on 04.08.15.
 */
public abstract class AbstractFunction implements Function{

    protected final List<Param> params;
    protected final String name;

    public AbstractFunction(String name, Param... params) {
        this.name = name;
        this.params = Lists.newArrayList(params);
    }

    public void checkParameters(List<Value> parameters) throws ScrapeQLException{
        int min=0;
        int max=0;
        int i=0;
        for(Param p:params){
            if(p.isRequired()){
                min++;
            }
            max++;
        }

        if(!(min<=parameters.size() && parameters.size()<=max)){
            throw new ScrapeQLException("Wrong parameter count "+getName());
        }

        for (int j = 0; j < parameters.size(); j++) {
            if(!params.get(j).getType().isAssignableFrom(parameters.get(j).getClass())){
                throw new ScrapeQLException("Wrong parameter type "+i+" in "+getName()+": is "+parameters.get(j).getClass()+" should be "+params.get(j).getType());
            }
        }
    }

    protected <T> T param(int param, List<Value> params){
        return (T) params.get(param);
    }

    @Override
    public Value execute(List<Value> parameters) throws ScrapeQLException {
        checkParameters(parameters);
        return executeChecked(parameters);
    }

    protected static Param p(Class<? extends Value> type, boolean required){
        return new Param(type, required);
    }

    protected static Param p(Class<? extends Value> type){
        return p(type,true);
    }

    protected abstract Value executeChecked(List<Value> parameters);

    @Override
    public String getName() {
        return name;
    }

    protected static class Param{
        private Class<? extends Value> type;
        private boolean required=true;


        public Param(Class<? extends Value> type, boolean required) {
            this.type = type;
            this.required = required;
        }

        public Param(Class<? extends Value> type) {
            this.type = type;
        }

        public Class<? extends Value> getType() {
            return type;
        }

        public boolean isRequired() {
            return required;
        }
    }
}
