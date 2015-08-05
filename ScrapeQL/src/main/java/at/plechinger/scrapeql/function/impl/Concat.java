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

import at.plechinger.scrapeql.function.Function;
import at.plechinger.scrapeql.value.StringValue;
import at.plechinger.scrapeql.value.Value;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lukas on 04.08.15.
 */
public class Concat implements Function {

    private static final String NAME = "CONCAT";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Value execute(List<Value> parameters) {
        StringBuilder buffer = new StringBuilder();

        List<String> cols = Lists.newArrayListWithExpectedSize(parameters.size());
        for (Value val : parameters) {
            buffer.append(val.getStringValue());
            cols.add(val.getVariableName());
        }

        StringBuilder variableName = new StringBuilder("concat(");
        Joiner.on(',').skipNulls().appendTo(variableName, cols);
        variableName.append(')');

        StringValue val = new StringValue(buffer.toString());
        val.setVariableName(variableName.toString());
        return val;
    }
}
