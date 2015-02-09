/*
 * The MIT License
 *
 * Copyright 2015 Lukas Plechinger.
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;

/**
 *
 * @author lukas
 */
class ScrapeQLStatement extends AbstractWrappable implements java.sql.Statement {

    protected ScrapeQLConnection connection;
    protected ScrapeQLResultSet resultSet;
    protected SQLWarning sqlWarning;

    ScrapeQLStatement(ScrapeQLConnection connection) {
        this.connection=connection;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        execute(sql);
        return resultSet;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        throw notSupported("executeUpdate");
    }

    @Override
    public void close() throws SQLException {
        if ( resultSet != null )
        {
            resultSet.close();
        }
        connection = null;
        resultSet = null;
        sqlWarning = null;
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        throw notSupported("getMaxFieldSize" );
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        throw notSupported( "setMaxFieldSize" );
    }

    @Override
    public int getMaxRows() throws SQLException {
        throw notSupported( "cancel" );
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        throw notSupported( "setMaxRows" );
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        throw notSupported( "setEscapeProcessing" );
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        throw notSupported( "getQueryTimeout" );
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        throw notSupported( "setQueryTimeout" );
    }

    @Override
    public void cancel() throws SQLException {
        throw notSupported( "cancel" );
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return sqlWarning;
    }

    @Override
    public void clearWarnings() throws SQLException {
        sqlWarning = null;
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        throw notSupported("setCursorName" );
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        try {
            resultSet = connection.executeQuery(sql);
            return true;
        }
        catch ( SQLWarning e )
        {
            if ( sqlWarning == null )
            {
                sqlWarning = e;
            }
            else
            {
                sqlWarning.setNextWarning( e );
            }
            throw e;
        }
        catch ( SQLException e )
        {
            throw e;
        }
        catch ( Throwable e )
        {
            throw new SQLException( e );
        }
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return resultSet;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        throw notSupported("getUpdateCount");
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        resultSet = null;
        return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        throw notSupported("setFetchDirection");
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return ResultSet.FETCH_UNKNOWN;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        throw notSupported("setFetchSize");
    }

    @Override
    public int getFetchSize() throws SQLException {
        throw notSupported("getFetchSize");
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public int getResultSetType() throws SQLException {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        throw notSupported("addBatch");
    }

    @Override
    public void clearBatch() throws SQLException {
        throw notSupported("clearBatch");
    }

    @Override
    public int[] executeBatch() throws SQLException {
        throw notSupported("executeBatch");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return getMoreResults();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        throw notSupported("getGeneratedKeys");
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return executeUpdate(sql);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return executeUpdate(sql);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return executeUpdate(sql);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return execute(sql);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return execute(sql);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return execute(sql);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return connection == null;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        throw notSupported("setPoolable");
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        throw notSupported("closeOnCompletion");
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return false;
    }

    protected SQLFeatureNotSupportedException notSupported(String methodName) {
        return new SQLFeatureNotSupportedException(methodName + " is not supported.");
    }

}
