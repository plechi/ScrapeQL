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

package at.plechinger.scrapeql.util;

import at.plechinger.scrapeql.ScrapeQLException;
import at.plechinger.scrapeql.value.Value;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Created by lukas on 07.08.15.
 */
public class ValueComparator {

    public static int compare(Value<?> v1, Value<?> v2) throws ScrapeQLException{
        Object o1=v1.getValue();
        Object o2=v2.getValue();

        if(is(Number.class,o1) && is(Number.class,o2)){
            return new BigDecimal(o1.toString()).compareTo(new BigDecimal(o2.toString()));
        }else if(o1.getClass().equals(o2.getClass()) && is(Comparable.class,o1)){
            return v1.getValue(Comparable.class).compareTo(o2);
        }

        return o1.toString().compareTo(o2.toString());
    }

    private static boolean is(Class<?> cls, Object o){
        return cls.isAssignableFrom(o.getClass());
    }
}
