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
package at.plechinger.scrapeql.jdbc;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lukas
 */
class ScrapeQLResultSet extends AbstractWrappable implements java.sql.ResultSet {

    private Table<Integer, String, String> table;
    private List<String> columns;
    private boolean closed = false;

    private int cursor = -1;

    private int rowCount;

    private boolean wasLastNull = false;

    ScrapeQLResultSet(Table<Integer, String, String> table) {
        this.table = table;
        this.rowCount = table.rowKeySet().size();

        columns = new ArrayList<>(table.columnKeySet().size());

        columns.addAll(table.columnKeySet());

    }

    private String findColumn(int index) throws SQLException {
        if (index < 0 || index > columns.size()) {
            throw new SQLDataException("Column " + index + " does not exist");
        }
        return columns.get(index);
    }

    @Override
    public int findColumn(String name) throws SQLException {
        //Fail fast
        if (!columns.contains(name)) {
            throw new SQLDataException("Column with name " + name + " does not exist");
        }

        int i = 0;
        for (String col : columns) {
            if (col.equals(name)) {
                return i;
            }
            i++;
        }

        //satisfy compiler
        throw new SQLDataException("Column with name " + name + " does not exist");
    }

    private String getCell(int row, String col) {
        Preconditions.checkArgument(table.containsColumn(col),"Result does not contain column %s.",col);
        return table.get(row, col);
    }

    @Override
    public boolean next() throws SQLException {
        cursor++;

        return cursor < rowCount;
    }

    @Override
    public void close() throws SQLException {
        closed = true;
    }

    @Override
    public boolean wasNull() throws SQLException {
        return wasLastNull;
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return cursor >= rowCount;
    }

    @Override
    public boolean isFirst() throws SQLException {
        return cursor == 0;
    }

    @Override
    public boolean isLast() throws SQLException {
        return cursor == rowCount - 1;
    }

    @Override
    public void beforeFirst() throws SQLException {
        cursor = -1;
    }

    @Override
    public void afterLast() throws SQLException {
        cursor = rowCount;
    }

    @Override
    public boolean first() throws SQLException {
        cursor = 0;
        return true;
    }

    @Override
    public boolean last() throws SQLException {
        cursor = rowCount - 1;
        return true;
    }

    @Override
    public int getRow() throws SQLException {
        return cursor + 1;
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        if (row > 0) {
            cursor = row - 1;
        } else {
            cursor = rowCount - row;
        }

        return false;
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        if (cursor + rows >= 0 && cursor + rows < rowCount) {
            cursor += rows;
            return true;
        }
        return false;
    }

    @Override
    public boolean previous() throws SQLException {
        return relative(-1);
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return cursor == (-1);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public String getCursorName() throws SQLException {
        return null;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return null;
    }


    /*GETTER BY COLUMN INDEX*/
    @Override
    public String getString(int columnIndex) throws SQLException {
        return getString(findColumn(columnIndex));
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        return getBoolean(findColumn(columnIndex));
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        return getByte(findColumn(columnIndex));
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        return getShort(findColumn(columnIndex));
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        return getInt(findColumn(columnIndex));
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        return getLong(findColumn(columnIndex));
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        return getFloat(findColumn(columnIndex));
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        return getDouble(findColumn(columnIndex));
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return getBigDecimal(findColumn(columnIndex), scale);
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        return getBytes(findColumn(columnIndex));
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        return getDate(findColumn(columnIndex));
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        return getTime(findColumn(columnIndex));
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return getTimestamp(findColumn(columnIndex));
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return getObject(findColumn(columnIndex));
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return getBigDecimal(findColumn(columnIndex));
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return getTime(findColumn(columnIndex), cal);
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return getDate(findColumn(columnIndex), cal);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return getTimestamp(findColumn(columnIndex), cal);
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        return getObject(findColumn(columnIndex), type);
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        return getURL(findColumn(columnIndex));
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return getObject(findColumn(columnIndex), map);
    }

    /*GETTER HELPERS*/
    private <T extends Number> T toNumber(String columnLabel, Class<T> clazz) throws SQLException {
        String n = getString(columnLabel);
        try {
            //Convert to Number
            Method valueOf = clazz.getMethod("valueOf", String.class);

            T result = (T) valueOf.invoke(null, n);
            return result;

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException th) {
            throw new SQLDataException("Cannot Parse value", th);
        }
    }

    private long getTimeMillis(String columnLabel) throws SQLException {
        return getLong(columnLabel);
    }

    private long getTimeMillisCal(String columnLabel, Calendar cal) throws SQLException {
        cal.setTimeInMillis(getTimeMillis(columnLabel));
        return cal.getTimeInMillis();
    }

    /*GETTER BY COLUMN NAME*/
    @Override
    public String getString(String columnLabel) throws SQLException {
        return getCell(cursor, columnLabel).trim();
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {

        String value = getString(columnLabel);

        //other boolean formats:
        if ("yes".equals(value)
                || "1".equals(value)) {
            value = "true";
        }

        return Boolean.parseBoolean(value);
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        return toNumber(columnLabel, Byte.class);
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        return toNumber(columnLabel, Short.class);
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        return toNumber(columnLabel, Integer.class);
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        return toNumber(columnLabel, Long.class);
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        return toNumber(columnLabel, Float.class);
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        return toNumber(columnLabel, Double.class);
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        return new BigDecimal(new BigInteger(getString(columnLabel)), scale);
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        return getString(columnLabel).getBytes();
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        return new Date(getTimeMillis(columnLabel));
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
        return new Time(getTimeMillis(columnLabel));
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return new Timestamp(getTimeMillis(columnLabel));
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return new BigDecimal(getString(columnLabel));
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return new Date(getTimeMillisCal(columnLabel, cal));
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return new Time(getTimeMillisCal(columnLabel, cal));
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return new Timestamp(getTimeMillisCal(columnLabel, cal));
    }

    @Override
    public URL getURL(String columnLabel) throws SQLException {
        try {
            URL url = new URL(getString(columnLabel));
            return url;
        } catch (MalformedURLException ex) {
            throw new SQLDataException("Cannot format URL.", ex);
        }
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        return getString(columnLabel);
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return getObject(columnLabel);
    }

    /*OTHER METHODS*/
    @Override
    public void setFetchDirection(int direction) throws SQLException {

    }

    @Override
    public int getFetchDirection() throws SQLException {
        return ResultSet.FETCH_FORWARD;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {

    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public int getType() throws SQLException {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public int getConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        return false;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        return false;
    }

    @Override
    public void refreshRow() throws SQLException {
    }

    @Override
    public void cancelRowUpdates() throws SQLException {

    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("moveToInsertRow");
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("moveToCurrentRow");
    }

    @Override
    public Statement getStatement() throws SQLException {
        return null;
    }

    @Override
    public int getHoldability() throws SQLException {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.closed;
    }

    /**
     * ******************* U N S U P P O R T E D *****************************
     */
    /*Unsupported GETTER*/
    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        throw new SQLFeatureNotSupportedException("<T> T getObject");
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getAsciiStream");
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getUnicodeStream");
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getBinaryStream");
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getAsciiStream");
    }

    @Override
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getUnicodeStream");
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getBinaryStream");
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getCharacterStream");
    }

    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getCharacterStream");
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getRef");
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getBlob");
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getClob");
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getArray");
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getRef");
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getBlob");
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getClob");
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getArray");
    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getRowId");
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getRowId");
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getNClob");
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getNClob");
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getSQLXML");
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getSQLXML");
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getNString");
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getNString");
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("getNCharacterStream");
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("getNCharacterStream");
    }

    /*UPDATE METHODS (not supported)*/
    @Override
    public void updateNull(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateByte(String columnLabel, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateDouble(String columnLabel, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateString(String columnLabel, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateDate(String columnLabel, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateTime(String columnLabel, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void insertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("insert");
    }

    @Override
    public void updateRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void deleteRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("delete");
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateRef(String columnLabel, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateClob(String columnLabel, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateArray(String columnLabel, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("update");
    }
}
