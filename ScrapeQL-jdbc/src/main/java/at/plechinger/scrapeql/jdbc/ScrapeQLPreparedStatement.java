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

import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author lukas
 */
public class ScrapeQLPreparedStatement extends AbstractScrapeQLPreparedStatement {

    private String sql;
    private Map<Integer, Object> params = new TreeMap<>();

    public ScrapeQLPreparedStatement(ScrapeQLConnection connection, String sql) {
        super(connection);
        this.sql = sql;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        resultSet = connection.executeQuery(sql, params);
        return resultSet;
    }

    @Override
    public boolean execute() throws SQLException {
        executeQuery();
        return true;
    }

    @Override
    public int executeUpdate() throws SQLException {
        throw notSupported("excuteUpdate");
    }

    private void add(int parameterIndex, Object value) {
        params.put(parameterIndex, value);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        add(parameterIndex, Boolean.toString(x));
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        add(parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        add(parameterIndex, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        add(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        add(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        add(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        add(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        add(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        add(parameterIndex, x);
    }

    @Override
    public void clearParameters() throws SQLException {
        params.clear();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        add(parameterIndex, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        add(parameterIndex, x);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        add(parameterIndex, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        add(parameterIndex, x);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        if (resultSet == null) {
            execute();
        }
        return resultSet.getMetaData();
    }

}
