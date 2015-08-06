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

package at.plechinger.scrapeql.filter;

import at.plechinger.scrapeql.ScrapeQLException;
import at.plechinger.scrapeql.context.Context;
import at.plechinger.scrapeql.ScrapeQLException;
import at.plechinger.scrapeql.context.Context;

/**
 * Created by lukas on 05.08.15.
 */
public class OrFilter implements Filter {

    private Filter filter1;
    private Filter filter2;

    public OrFilter(Filter filter1, Filter filter2) {
        this.filter1 = filter1;
        this.filter2 = filter2;
    }

    @Override
    public boolean filter(Context ctx) throws ScrapeQLException {
        return filter1.filter(ctx) || filter2.filter(ctx);
    }
}
