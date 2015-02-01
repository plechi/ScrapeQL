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
package at.plechinger.scrapeql.query.variable;

import at.plechinger.scrapeql.query.QueryContext;
import com.google.common.base.Preconditions;
import java.util.List;

/**
 *
 * @author lukas
 */
public class ListVariable<T> implements Variable {

    private String alias;

    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public ListVariable(List<T> list) {
        Preconditions.checkNotNull(list, "List must not be null.");
        this.list = list;
    }

    @Override
    public String getValue() {
        return String.format("List[%d]", list.size());
    }

    @Override
    public Variable as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void execute(QueryContext context) {
        if (alias != null) {
            context.addVariable(alias, this);
        }
    }

}
