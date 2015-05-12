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

import at.plechinger.scrapeql.parser.QueryParser;
import at.plechinger.scrapeql.query.Query;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 *
 * @author lukas
 */
public class ScrapeQLConnection extends AbstractWrappable implements java.sql.Connection {

    private String url;
    private boolean closed = false;
    private Properties properties = new Properties();
    private SQLWarning sqlWarnings;

    private Properties clientInfo = new Properties();

    private QueryParser parser = new QueryParser();

    ScrapeQLConnection(String url, Properties properties) {
        this.properties.putAll(properties);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
    
    

    @Override
    public Statement createStatement() throws SQLException {
        return new ScrapeQLStatement(this);
    }

    ScrapeQLResultSet executeQuery(String sql) throws SQLException {
        try {

            Query query = parser.parse(sql);
            ScrapeQLResultSet rs = new ScrapeQLResultSet(query.executeTable());
            return rs;
        } catch (Throwable thr) {
            throw new SQLException(thr);
        }
    }

    ScrapeQLResultSet executeQuery(String sql, Map<Integer, Object> params) throws SQLException {
        StringBuilder finalQuery = new StringBuilder();
        int cursor = 0;
        for (Object param : params.values()) {

            int start = sql.indexOf("?", cursor);
            if (start < 0) {
                throw new SQLException("Invalid argument count in prepared statement.");
            }
            finalQuery.append(sql.substring(cursor, start));
            finalQuery.append('"');
            finalQuery.append(param);
            finalQuery.append('"');
            cursor = start + 1;
        }

        //there should be no more ? in prepared statement
        if (sql.indexOf("?", cursor) >= 0) {
            throw new SQLException("Invalid argument count in prepared statement.");
        }

        //rest of query
        finalQuery.append(sql.substring(cursor));
        return executeQuery(finalQuery.toString());
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new ScrapeQLPreparedStatement(this, sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return null;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return sql;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {

    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return true;
    }

    @Override
    public void commit() throws SQLException {

    }

    @Override
    public void rollback() throws SQLException {

    }

    @Override
    public void close() throws SQLException {
        closed = true;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return new ScrapeQLDatabaseMetadata(this);
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return true;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        //do nothing
    }

    @Override
    public String getCatalog() throws SQLException {
        return "default";
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {

    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return 0;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.sqlWarnings;
    }

    @Override
    public void clearWarnings() throws SQLException {
        sqlWarnings = null;
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return createStatement();
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return Collections.EMPTY_MAP;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        //do nothing
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        //do nothing
    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return null;
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return null;
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        //do nothing
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        //do nothing
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return prepareCall(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public Clob createClob() throws SQLException {
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return null;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return true;
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        this.clientInfo.setProperty(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        this.clientInfo.putAll(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return this.clientInfo.getProperty(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return this.clientInfo;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return null;
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        //do nothing
    }

    @Override
    public String getSchema() throws SQLException {
        return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        //do nothing
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        //do nothing
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return 0;
    }
}
