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

import at.plechinger.scrapeql.HtmlLoader;
import at.plechinger.scrapeql.expression.Expression;
import at.plechinger.scrapeql.expression.Variable;
import com.google.common.base.Preconditions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by lukas on 15.05.15.
 */
public class DataContext{

    private Element rootElement=null;

    private String contextName=null;

    private Expression<String> url;

    public DataContext(Expression<String> url){
        this.url=url;
    }

    public DataContext as(String alias){
        this.contextName=alias;
        return this;
    }

    public String getName(){
        return contextName;
    }

    public Variable<Element> select(Variable<String> selector) {
        Preconditions.checkArgument(!selector.isNull(),"Selector must not be null");

        Elements elements=rootElement.select(selector.getValue());
        if(elements!=null && !elements.isEmpty()){
            return new Variable<Element>(elements.first());
        }else{
            return null;
        }
    }

    public void load(QueryContext ctx){
        Variable<String> urlVariable=url.execute(null);
        String html=HtmlLoader.getLoader().load(urlVariable.getValue());
        Document doc=Jsoup.parse(html);
        rootElement=doc.select(":root").first();
    }
}
