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
import at.plechinger.scrapeql.type.IntegerValue;
import at.plechinger.scrapeql.type.StringValue;
import at.plechinger.scrapeql.type.Value;
import at.plechinger.scrapeql.type.ValueConverter;
import com.google.common.base.Preconditions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lukas on 04.08.15.
 */
public class DateFormat extends AbstractFunction{

    public DateFormat() {
        super("date_format",p(Value.class),p(StringValue.class));
    }

    @Override
    protected Value executeChecked(List<Value> parameters) {
        StringValue format=param(1,parameters);
        Value toFormat=parameters.get(0);
        Value returnValue;
        try {
            SimpleDateFormat sdf=new SimpleDateFormat(format.getValue());
            Date parsed=sdf.parse(toFormat.getStringValue());
            returnValue=new IntegerValue(parsed.getTime());

        } catch (ParseException e) {
            returnValue=toFormat;
        }

        returnValue.setVariableName(String.format("date_format(%s)",toFormat.getVariableName()));
        return returnValue;
    }
}