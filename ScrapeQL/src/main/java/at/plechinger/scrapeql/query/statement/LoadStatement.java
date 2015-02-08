/*
 * The MIT License
 *
 * Copyright 2015 Lukas Plechinger.
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
package at.plechinger.scrapeql.query.statement;

import at.plechinger.scrapeql.query.Executable;
import at.plechinger.scrapeql.query.QueryContext;
import at.plechinger.scrapeql.query.expression.SelectorVariable;
import com.google.common.base.Charsets;
import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



/**
 *
 * @author Lukas Plechinger
 */
public class LoadStatement implements Executable {

    private String url;

    public LoadStatement(String url) {
        this.url = url;
    }

    @Override
    public void execute(QueryContext context) {
        try {
            Document doc;

            //FIXME: better load algorithm (maybe with caching?)
            if (url.startsWith("file:")) {
                doc = Jsoup.parse(new File(url.substring("file:".length())), Charsets.UTF_8.name());
            } else {
                doc = Jsoup.connect(url).get();
            }

            context.setRootElement(doc.select(":root").first());

            SelectorVariable var = new SelectorVariable(":root");
            var.execute(context);
            context.addVariable("root", var);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
            //FIXME:error handling
        }
    }

}