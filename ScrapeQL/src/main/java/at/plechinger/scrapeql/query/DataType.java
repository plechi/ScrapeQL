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


import at.plechinger.scrapeql.query.datacontext.DataElement;
import com.google.common.base.Optional;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by lukas on 12.05.15.
 */
public enum DataType {

    STRING(Pattern.compile(".*"), String.class),
    INT(Pattern.compile("\\d+"), Long.class),
    DECIMAL(Pattern.compile("\\d+\\.\\d+"), Double.class),
    /**
     * Boolean datatype
     * Does not match 0 or 1 because this should be interpreted as INT.
     */
    BOOL(Pattern.compile("(true|false)",Pattern.CASE_INSENSITIVE), Boolean.class),
    ELEMENT(DataElement.class),
    DATE(Date.class),
    NULL;

    public Class<?> getInternalType() {
        return internalType;
    }

    public Pattern getTestPattern() {
        return testPattern.get();
    }

    public boolean hasTestPattern() {
        return testPattern.isPresent();
    }

    private Class<?> internalType;

    private Optional<Pattern> testPattern = Optional.absent();

    private DataType(Pattern testPattern, Class<?> internalType) {
        this.testPattern = Optional.of(testPattern);
        this.internalType = internalType;
    }

    private DataType(Class<?> internalType) {
        this.internalType = internalType;
    }

    private DataType(){}

    public static DataType determine(String input) {
        for(DataType type:values()){
            //string is default
            if(!STRING.equals(type) &&
                    type.testPattern.isPresent() &&
                    type.testPattern.get().matcher(input.trim()).matches()){
                return type;
            }
        }
        return STRING;
    }

    public static DataType determine(Class<?> clazz){
        if(clazz==null){
            return NULL;
        }

        for(DataType type:values()){
            //string is default
            if(type.internalType.equals(clazz)){
                return type;
            }
        }
        throw new RuntimeException("Cannot determine Datatype of "+clazz.getName());
    }

}
