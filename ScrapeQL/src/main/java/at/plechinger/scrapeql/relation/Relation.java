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
import com.google.common.collect.Table;
import dnl.utils.text.table.TextTable;

import javax.swing.table.AbstractTableModel;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lukas on 04.08.15.
 */
public class Relation {

    private Table<Integer, String, Value> table;

    public Relation(){
        table = HashBasedTable.create();
    }

    public Relation(Table<Integer, String, Value> table){
        this.table=table;
    }

    public Table<Integer, String, Value> makeTable(String col, List<Value> values) {
        Table<Integer, String, Value> table = HashBasedTable.create(values.size(), 1);
        for (int i = 0; i < values.size(); i++) {
            table.put(i, col, values.get(i));
        }
        return table;
    }

    public void addColumn(String name, List<Value> values) {
        for (int i = 0; i < values.size(); i++) {
            table.put(i, name, values.get(i));
        }
    }

    public Relation join(Relation value) {
        return new Relation(cartesian(table,value.table));
    }

    private Table<Integer, String, Value> cartesian(Table<Integer, String, Value> table, Table<Integer, String, Value> otherTable) {
        Table<Integer, String, Value> newTable = HashBasedTable.create(table.rowKeySet().size() * otherTable.rowKeySet().size(),
                table.columnKeySet().size() + otherTable.columnKeySet().size());

        if (table.size() > 0 && otherTable.size() > 0) {
            System.out.println("join");
            int row = 0;
            for (Integer r1 : table.rowKeySet()) {
                for (Integer r2 : otherTable.rowKeySet()) {
                    Map<String, Value> row1 = table.row(r1);
                    Map<String, Value> row2 = otherTable.row(r2);

                    for (String c : row1.keySet()) {
                        newTable.put(row, c, row1.get(c));
                    }

                    for (String c : row2.keySet()) {
                        newTable.put(row, c, row2.get(c));
                    }
                    row++;
                }
            }
            return newTable;
        } else if (table.size() == 0) {
            return otherTable;
        }
        return table;
    }

    public Set<String> getColumns() {
        return table.columnKeySet();
    }

    public String toPrettyString() {
        final List<String> cols = Lists.newArrayList(getColumns());
        TextTable tt = new TextTable(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return table.rowKeySet().size();
            }

            @Override
            public int getColumnCount() {
                return getColumns().size();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return table.row(rowIndex).get(cols.get(columnIndex));
            }

            @Override
            public String getColumnName(int column) {
                return cols.get(column);
            }
        });

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        tt.printTable(new PrintStream(os), 0);

        return os.toString();
    }

}
