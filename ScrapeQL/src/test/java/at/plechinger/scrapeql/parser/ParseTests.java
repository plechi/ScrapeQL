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

package at.plechinger.scrapeql.parser;

import at.plechinger.scrapeql.ScrapeParser;
import at.plechinger.scrapeql.query.SelectQuery;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by lukas on 07.08.15.
 */
@RunWith(Parameterized.class)
public class ParseTests {
    private String statement;
    private ScrapeParser parser = new ScrapeParser();

    @Parameterized.Parameters
    public static Collection<Object[]> primeNumbers() throws IOException {

        List<Object[]> parameters = Lists.newArrayList();

        File testfile=new File("src/test/resources/parser/parsetests.sql");
        if(!testfile.exists()){
            testfile=new File("ScrapeQL/src/test/resources/parser/parsetests.sql");
        }

        String[] tests = CharStreams.toString(new FileReader(testfile)).replaceAll("\n"," ").split("(s?)(\\s*?;\\s*?)");

        for (String statement : tests) {
            parameters.add(new Object[]{statement.trim()});
        }

        return parameters;
    }

    public ParseTests(String statement) {
        this.statement = statement;
    }

    @Test
    public void testParse() {
        System.out.println("Parse "+statement);
        SelectQuery query = parser.parse(statement);
        Assert.assertNotNull(query);
    }

}
