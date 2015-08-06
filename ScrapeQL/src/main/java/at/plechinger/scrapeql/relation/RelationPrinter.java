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

import com.google.common.collect.Lists;
import dnl.utils.text.table.TextTable;

import javax.swing.table.AbstractTableModel;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by lukas on 06.08.15.
 */
public class RelationPrinter {

    public static String getPrettyString(Relation relation){

        final List<String> cols = Lists.newArrayList(relation.getColumnNames());
        TextTable tt = new TextTable(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return relation.rows();
            }

            @Override
            public int getColumnCount() {
                return relation.columns();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Relation.Row row=relation.getRow(rowIndex);
                return row.getValue(getColumnName(columnIndex));
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
