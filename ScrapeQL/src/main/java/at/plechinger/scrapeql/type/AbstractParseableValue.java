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

package at.plechinger.scrapeql.type;

import com.google.common.collect.Lists;

import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lukas on 04.08.15.
 */
public abstract class AbstractParseableValue<T> extends AbstractValue<T> implements ParseableValue<T> {

    protected String originalValue;

    protected static List<Pattern> patterns = Lists.newArrayList();

    public AbstractParseableValue(String toParse) {
        this.originalValue = toParse;
       setParsedValue(toParse);
    }

    public AbstractParseableValue(T value, String originalValue){
        this.value=value;
        this.originalValue=originalValue;
    }

    protected abstract T parseMatch(Matcher matcher);

    @Override
    public String getStringValue() {
        return originalValue;
    }

    @Override
    public void setParsedValue(String string) {

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(string);
            if (matcher.matches()) {
                value = parseMatch(matcher);
                return;
            }
        }
        throw new RuntimeException("Input String " + string + " cannot be converted to " + getDataTypeName() + " found.");
    }

    @Override
    public T getValue() {
        return value;
    }
}
