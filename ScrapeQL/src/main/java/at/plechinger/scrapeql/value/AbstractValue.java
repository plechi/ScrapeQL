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

package at.plechinger.scrapeql.value;

import at.plechinger.scrapeql.ScrapeQLException;
import at.plechinger.scrapeql.ScrapeQLException;

/**
 * Created by lukas on 04.08.15.
 */
public abstract class AbstractValue<T> implements Value<T> {

    protected T value;

    protected String variableName;

    public AbstractValue(T value){
        this.value=value;
    }

    protected AbstractValue(){}

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return value.toString();
    }

    @Override
    public String toString() {
        return getStringValue();
    }

    @Override
    public String getVariableName() {
        return variableName;
    }

    @Override
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public <S> S getValue(Class<S> clazz) throws ScrapeQLException {
        if(!value.getClass().isAssignableFrom(clazz)){
            throw new ScrapeQLException("Cannot cast "+value.getClass()+" to "+clazz);
        }

        return clazz.cast(value);
    }
}
