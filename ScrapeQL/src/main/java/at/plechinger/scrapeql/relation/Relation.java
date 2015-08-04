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

import at.plechinger.scrapeql.type.Value;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lukas on 04.08.15.
 */
public class Relation {

    private List<ColumnDefinition> columns = Lists.newLinkedList();

    public void addColumn(String name, List<Value> values) {
        columns.add(new ColumnDefinition(name, values));
    }

    public int size() {
        int size = 1;
        for (ColumnDefinition col : columns) {
            size *= col.getValues().size();
        }
        return size;
    }

    private void join(Table<Integer, String, Value> table, Map<String, Value> row, int col) {

        if (col >= columns.size()) {
            int rowNum = table.rowKeySet().size();

            for (Map.Entry<String, Value> entry : row.entrySet()) {
                table.put(rowNum, entry.getKey(), entry.getValue());
            }
            return;
        }

        ColumnDefinition column = columns.get(col);

        for (Value val : column.getValues()) {
            Map<String, Value> newMap = new LinkedHashMap<>(row);
            newMap.put(column.getName(), val);
            join(table, newMap, col + 1);
        }
    }

    public Table<Integer, String, Value> joinTable() {
        int rows = size();
        int cells = columns.size();

        Table<Integer, String, Value> result = HashBasedTable.create(rows, cells);

        join(result, new LinkedHashMap<String, Value>(), 0);

        return result;
    }

    private static class ColumnDefinition {

        private String name;

        private List<Value> values;

        public ColumnDefinition(String name, List<Value> values) {
            this.name = name;
            this.values = values;
        }

        public String getName() {
            return name;
        }

        public List<Value> getValues() {
            return values;
        }

    }

    public Set<String> getColumns(){
        Set ret= Sets.newLinkedHashSet();
        for(ColumnDefinition col:columns){
            ret.add(col.getName());
        }
        return ret;
    }
}
