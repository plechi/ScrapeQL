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
import at.plechinger.scrapeql.expression.ExpressionContext;
import at.plechinger.scrapeql.expression.value.Value;
import com.google.common.base.Optional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;


/**
 * Created by lukas on 18.05.15.
 */
public class DataContext {
    private String name;
    private List<Element> rowElements;

    private Document document;

    private boolean loaded=false;

    private Optional<Expression> selector=Optional.absent();
    private final Expression parameter;

    public DataContext(Expression parameter) {
        this.parameter=parameter;
    }

    public DataContext as(String dataContext) {
        this.name=dataContext;
        return this;
    }

    public String getName() {
        return name;
    }

    public List<Element> getRowElements() {
        return rowElements;
    }

    public boolean isLoaded(){
        return loaded;
    }

    public DataContext select(Expression selectorExpression) {
        this.selector=Optional.of(selectorExpression);
        return this;
    }

    public void load(ExpressionContext ctx){
        if(loaded){
           return;
        }

        Value<String> urlVar=parameter.execute(ctx);
        if(urlVar.isNull()){
            throw new RuntimeException("No Variable Name specified");
        }

        String url=urlVar.getValue();

        String html=HtmlLoader.getLoader().load(url);

        this.document=Jsoup.parse(html,url);


        if(selector.isPresent()){
            Value<String> selectorVar=selector.get().execute(ctx);
            if(selectorVar.isNull()){
                throw new RuntimeException("Selector must not be null");
            }
            rowElements=document.select(selectorVar.getValue());
        }else{
            rowElements=document.select(":root");
        }
        loaded=true;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj==null){
            return false;
        }

        if(!this.getClass().isAssignableFrom(obj.getClass())){
            return false;
        }

        DataContext another=(DataContext) obj;

        return name.equals(((DataContext) obj).getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
