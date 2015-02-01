/*
 * The MIT License
 *
 * Copyright 2015 lukas.
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

import at.plechinger.scrapeql.query.Query;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author lukas
 */
public class QueryParserTest {

    private QueryParser parser = new QueryParser();

    @Test
    public void testSimple() {

        Query query = parser.parse(
                "LOAD \"file:src/test/resources/TestQueries.html\";"
                + "SELECT 'h1' as test FROM root;"
                + "OUTPUT test;");

        Assert.assertEquals("Header1", query.execute().get("test"));
    }

    @Test
    public void testComplex() {

        Query query = parser.parse(
                "LOAD \"file:src/test/resources/TestQueries.html\";"
                + "SELECT 'p' AS para FROM root;"
                + "SELECT attr('a',\"href\") AS test FROM para;"
                + "OUTPUT test;");

        Assert.assertEquals("link", query.execute().get("test"));
    }

    @Test
    public void testMultiple() {

        Query query = parser.parse(
                "LOAD \"file:src/test/resources/TestQueries.html\";"
                + "SELECT EVERY 'li' AS item IN 'ul#list>li' FROM root INTO test;"
                + "OUTPUT test;");

        List<Map<String, String>> result = new ArrayList<>(4);
        {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("item", "elemen1");
            result.add(map);
        }
        {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("item", "elemen2");
            result.add(map);
        }
        {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("item", "elemen3");
            result.add(map);
        }
        {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("item", "elemen4");
            result.add(map);
        }

        Assert.assertEquals(result, query.execute().get("test"));
    }

}
