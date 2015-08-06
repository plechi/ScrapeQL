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

import at.plechinger.scrapeql.function.annotation.FunctionDefinition;
import at.plechinger.scrapeql.loader.html.HtmlEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lukas on 05.08.15.
 */
public class StringFunctions {

    @FunctionDefinition(value = "lower", strict = false)
    public String lower(String input) {
        return input.toLowerCase();
    }

    @FunctionDefinition(value = "upper", strict = false)
    public String upper(String input) {
        return input.toUpperCase();
    }

    @FunctionDefinition(value = "regex_replace", strict = false)
    public String regexReplace(String input, String regex, String replace) {
        return input.replaceAll(regex, replace);
    }

    @FunctionDefinition(value = "date_format", strict = false)
    public long dateFormat(String value, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date parsed = sdf.parse(value);
            return parsed.getTime();

        } catch (ParseException e) {
            return 0;
        }
    }

    @FunctionDefinition(value = "contains", strict = false)
    public Boolean contains(String input, String contains) {
        return input.contains(contains);
    }

    @FunctionDefinition(value = "tag_text")
    public String tagText(HtmlEntity input) {

        System.out.println("test"+input.getWrappedEntity().html());
        return input.getWrappedEntity().text();
    }
}
