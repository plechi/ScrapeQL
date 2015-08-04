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

import at.plechinger.scrapeql.expression.value.Value;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lukas on 18.05.15.
 */
public class Relation {

    private Table<Integer, String, Value> relation=HashBasedTable.create();
    private Set<String> columns=new LinkedHashSet<String>();
    private int rowNum=0;

    public void addRow(Map<String,Value> row){
        for(Map.Entry<String,Value> entry:row.entrySet()){
            columns.add(entry.getKey());
            relation.put(rowNum,entry.getKey(),entry.getValue());
        }

        rowNum++;
    }

    @Override
    public String toString() {
        StringBuilder print=new StringBuilder();
        StringBuilder builder=new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            print.append("%s\t");
        }

        builder.append(String.format(print.toString(),columns.toArray(new String[columns.size()])));
        builder.append('\n');

        for (int i = 0; i < rowNum; i++) {

            Map<String,Value> ro=relation.row(i);
            for(String col:columns){
                Value value =ro.get(col);

                if(value !=null){
                    builder.append(value.getValue());
                }else{
                 builder.append("NULL");
                }
                builder.append('\t');

            }
            builder.append('\n');
        }

        return builder.toString();
    }
}
