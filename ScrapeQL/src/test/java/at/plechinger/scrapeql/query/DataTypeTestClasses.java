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

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * Created by lukas on 12.05.15.
 */
@RunWith(Parameterized.class)
public class DataTypeTestClasses extends TestCase {


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{

                //class tests
                {String.class, DataType.STRING},
                {Boolean.class, DataType.BOOL},
                {Long.class, DataType.INT},
                {Double.class, DataType.DECIMAL},
                {DataElement.class, DataType.ELEMENT},
                {Date.class, DataType.DATE},
        });
    }

    @Parameterized.Parameter(0)
    public Class<?> input;

    @Parameterized.Parameter(1)
    public DataType expected;

    @Test
    public void testDetermine() {
        assertEquals(String.format("Expect \"%s\" to be a %s",input.getName(),expected.name()),expected, DataType.determine(input));
    }
}