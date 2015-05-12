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
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

/**
 * Created by lukas on 12.05.15.
 */
@RunWith(Parameterized.class)
public class DataTypeTest extends TestCase {


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"true", DataType.BOOL},
                {"false", DataType.BOOL},
                {"0.5", DataType.DECIMAL},
                {"123.456", DataType.DECIMAL},
                {"  0.5  ", DataType.DECIMAL},
                {"lorem ipsum", DataType.STRING},
                {"123 test 456", DataType.STRING},
                {"true and false", DataType.STRING},
                {"false and true", DataType.STRING},
                {"0.1+345", DataType.STRING},
                {"1234false", DataType.STRING},
                {"", DataType.STRING},
                {"10", DataType.INT},
                {"123456", DataType.INT},
        });
    }

    @Parameterized.Parameter // first data value (0) is default
    public String input;

    @Parameterized.Parameter(value = 1)
    public DataType expected;

    @Test
    public void testDetermine() {
        assertEquals(String.format("Expect \"%s\" to be a %s",input,expected.name()),expected, DataType.determine(input));
    }
}