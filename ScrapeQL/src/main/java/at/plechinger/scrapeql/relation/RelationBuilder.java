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

package at.plechinger.scrapeql.relation;

import at.plechinger.scrapeql.expression.*;
import com.google.common.base.Optional;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lukas on 18.05.15.
 */
public class RelationBuilder {

    public RelationBuilder(Elements rows, List<Expression> expressionList) {
        this.rows = rows;
        this.expressionList = expressionList;
    }

    private Elements rows;
    private List<Expression> expressionList;
    private Relation relation = new Relation();


    public Relation build() {

        for (Element element : rows) {
            relation.addRow(executeRow(element));
        }

        return relation;
    }

    private Map<String, Variable> executeRow(Element base){

        Map<String, Variable> row = new LinkedHashMap<>();
        ExpressionContext ctx=new ExpressionContext("test",base);

        for(Expression exp:expressionList){
            Variable value=exp.execute(ctx);

            String colName= "col_"+row.size();

            //"Better" instanceof
            if(AliasExpression.class.isAssignableFrom(exp.getClass())){
                AliasExpression nex=AliasExpression.class.cast(exp);
                Optional<String> alias=nex.getAlias();

                if(alias.isPresent()){
                    colName=alias.get();
                }
            }
            row.put(colName,value);
        }

        return row;
    }

}
