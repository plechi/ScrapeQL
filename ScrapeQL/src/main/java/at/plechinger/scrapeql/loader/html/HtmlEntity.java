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

package at.plechinger.scrapeql.loader.html;

import at.plechinger.scrapeql.loader.Entity;
import at.plechinger.scrapeql.util.Mapper;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by lukas on 04.08.15.
 */
public class HtmlEntity implements Entity<Element> {

    private Element element;

    public HtmlEntity(Element element){
        this.element=element;
    }

    @Override
    public List<Entity<Element>> select(String selector) {
        return Mapper.map(element.select(selector), new Mapper.MapFn<Element, Entity<Element>>() {
            @Override
            public Entity<Element> map(Element from) {
                return new HtmlEntity(from);
            }
        });
    }

    @Override
    public Element getWrappedEntity() {
        return element;
    }

    @Override
    public String getStringValue() {
        return element.ownText();
    }

    @Override
    public String toString() {
        return getStringValue();
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }
}
