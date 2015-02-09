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

import java.io.InputStream;
import java.io.Reader;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author lukas
 */
public abstract class AbstractScrapeQLPreparedStatement extends ScrapeQLStatement implements PreparedStatement{

    public AbstractScrapeQLPreparedStatement(ScrapeQLConnection connection) {
        super(connection);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        throw typeNotSupported("Nulls");
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        throw typeNotSupported("Bytes[]");
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        throw typeNotSupported("Dates");
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        throw typeNotSupported("Time");
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        throw typeNotSupported("Timestamps");
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw typeNotSupported("Streams");
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw typeNotSupported("Streams");
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw typeNotSupported("Streams");
    }


    @Override
    public void addBatch() throws SQLException {
        throw notSupported("addBatch");
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        throw typeNotSupported("Streams");
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
       throw typeNotSupported("Refs");
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        throw typeNotSupported("Blobs");
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        throw typeNotSupported("Clobs");
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        throw typeNotSupported("Arrays");
    }
    
    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        throw typeNotSupported("Date");
    }


    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        throw typeNotSupported("Time");
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
       throw typeNotSupported("Timestamps");
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        throw typeNotSupported("Nulls");
    }


    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        throw typeNotSupported("RowId");
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        throw typeNotSupported("NSttring");
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
       throw typeNotSupported("Streams");
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
       throw typeNotSupported("Clobs");
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
       throw typeNotSupported("Clobs");
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        throw typeNotSupported("Blobs");
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
       throw typeNotSupported("Clobs");
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        throw typeNotSupported("SQLXML");
    }


    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw typeNotSupported("Blobs");
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw typeNotSupported("Streams");
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        throw typeNotSupported("Streams");
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        throw typeNotSupported("Streams");
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        throw typeNotSupported("Streams");
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        throw typeNotSupported("Streams");
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        throw typeNotSupported("Streams");
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        throw typeNotSupported("Clobs");
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
         throw typeNotSupported("Blobs");
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        throw typeNotSupported("Clobs");
    }
    
    
    protected SQLException typeNotSupported(String methodName) {
        return new SQLException(methodName + " not supported as parameter type.");
    } 
}
