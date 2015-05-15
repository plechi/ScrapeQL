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

import at.plechinger.scrapeql.query.DataType;

/**
 * Created by lukas on 12.05.15.
 */
public class Variable<T> {

    private T value;

    private Class<? extends T> valueClass=null;


    private DataType type;

    public Variable(T value){
        this.value=value;
        if(value!=null){
            this.valueClass=(Class<? extends T>) value.getClass();
        }
        type=DataType.determine(valueClass);
    }

    public Class<? extends T> getValueClass() {
        return valueClass;
    }

    public T getValue() {
        return value;
    }

    public boolean isNull(){
        return value!=null;
    }

    public DataType getType() {
        return type;
    }
}
