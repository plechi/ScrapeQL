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

package at.plechinger.scrapeql.query.datacontext;

import at.plechinger.scrapeql.query.DataType;
import at.plechinger.scrapeql.query.expression.Expression;
import at.plechinger.scrapeql.query.expression.Variable;
import com.google.common.base.Preconditions;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Optional;

/**
 * Created by lukas on 12.05.15.
 */
public class DataContext {

    private String name;

    private Variable parameter;


    private Element rootElement;

    public DataContext(Variable parameter, String name) {
        this.parameter=parameter;
        this.name=name;
    }


    private void loadDataContext(){

        //check type
        Preconditions.checkArgument(DataType.STRING.equals(parameter.getType())
        || DataType.ELEMENT.equals(parameter.getType()));


    }

    public List<DataElement> selector(String selector){
        return null;
    }

}
