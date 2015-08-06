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
import com.google.common.collect.Sets;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by lukas on 06.08.15.
 */
public class RelationImpl<T> implements Relation<T> {

    private List<Row<T>> rows = Lists.newLinkedList();
    private Set<String> columns = Sets.newLinkedHashSet();


    public RelationImpl(){}

    public RelationImpl(String startColumn, List<T> startValues){
        for(T val:startValues){
            Row<T> row=new RowImpl<>();
            row.setValue(startColumn,val);
            rows.add(row);
        }
    }

    @Override
    public int rows() {
        return rows.size();
    }

    @Override
    public int columns() {
        return columns.size();
    }

    @Override
    public Set<String> getColumnNames() {
        return columns;
    }

    @Override
    public Row<T> getRow(int row) {
        return rows.get(row);
    }

    @Override
    public List<T> getColumn(String name) {
        List<T> column = Lists.newArrayListWithExpectedSize(rows());
        for (Row<T> row : rows) {
            column.add(row.getValue(name));
        }
        return column;
    }


    @Override
    public void addRow(Row<T> row) {
        columns.addAll(row.getColumnNames());
        rows.add(row);
    }

    @Override
    public T getValue(int row, String column) {
        Row<T> r = getRow(row);
        return r.getValue(column);
    }

    @Override
    public void setValue(int row, String column, T value) {
        Row<T> r = getRow(row);
        r.setValue(column, value);
    }

    @Override
    public Iterator<Row<T>> iterator() {
        return rows.iterator();
    }
    //set operations

    @Override
    public Relation<T> union(Relation<T> other) {
        Relation<T> union = new RelationImpl<>();
        Set<String> commonColumns = Sets.intersection(getColumnNames(), other.getColumnNames());

        for (Row<T> r : this) {
            Row<T> row = new RowImpl<>();
            for (String c : commonColumns) {
                row.setValue(c, r.getValue(c));
            }
            union.addRow(row);
        }

        for (Row<T> r : other) {
            Row<T> row = new RowImpl<>();
            for (String c : commonColumns) {
                row.setValue(c, r.getValue(c));
            }
            union.addRow(row);
        }

        return union;
    }

    @Override
    public Relation<T> intersect(Relation<T> other) {
        throw new NotImplementedException();
    }

    @Override
    public Relation<T> difference(Relation<T> other) {
        throw new NotImplementedException();
    }

    @Override
    public Relation<T> cartesian(Relation<T> other) {
        Relation<T> cartesian = new RelationImpl<>();
        for (Row<T> first : this) {
            for (Row<T> second : other) {
                Row<T> newRow = new RowImpl<>();
                copyInto(first, newRow);
                copyInto(second, newRow);
                cartesian.addRow(newRow);
            }
        }
        return cartesian;
    }


    private void copyInto(Row<T> from, Row<T> into) {
        for (String column : from.getColumnNames()) {
            into.setValue(column, from.getValue(column));
        }
    }


    @Override
    public String toString() {
        return RelationPrinter.getPrettyString(this);
    }
}
