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

import at.plechinger.scrapeql.CachedUrlLoader;
import at.plechinger.scrapeql.ScrapeQLException;
import at.plechinger.scrapeql.value.StringValue;
import at.plechinger.scrapeql.value.Value;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by lukas on 04.08.15.
 */
public class UrlFn extends AbstractFunction {
    public UrlFn() {
        super("URL", p(StringValue.class));
    }

    @Override
    protected Value executeChecked(List<Value> parameters) throws ScrapeQLException {
        StringValue val = param(0, parameters);

        try {
            URL url = new URL(val.getValue());
            String document = CachedUrlLoader.getLoader().load(url.toString());
            StringValue value = new StringValue(document);
            value.setVariableName(String.format("url($s)", url.getHost()));
            return value;

        } catch (MalformedURLException malex) {
            throw new ScrapeQLException("Url is not well formatted.");
        }
    }
}
