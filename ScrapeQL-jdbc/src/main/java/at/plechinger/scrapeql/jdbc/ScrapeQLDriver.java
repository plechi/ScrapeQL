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

import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author lukas
 */
public class ScrapeQLDriver implements java.sql.Driver{
    
    
    public static final String PREFIX = "jdbc:scrapeql:";
    
    static
    {
        try
        {
            DriverManager.registerDriver( new ScrapeQLDriver());
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }
    
    void parseUrlProperties( String s, Properties properties )
    {
        if ( s.contains( "?" ) )
        {
            String urlProps = s.substring( s.indexOf( '?' ) + 1 );
            String[] props = urlProps.split( "," );
            for ( String prop : props )
            {
                int idx = prop.indexOf( '=' );
                if ( idx != -1 )
                {
                    String key = prop.substring( 0, idx );
                    String value = prop.substring( idx + 1 );
                    properties.put( key, value );
                }
                else
                {
                    properties.put( prop, "true" );
                }
            }
        }
    }


    @Override
    public ScrapeQLConnection connect(String url, Properties info) throws SQLException {
        if ( !acceptsURL( url ) )
        {
            return null;
        }
        parseUrlProperties( url, info );

        return new ScrapeQLConnection(url,info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith(PREFIX);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[]
                {
                        new DriverPropertyInfo("cache", info.getProperty("cache"))
                };
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 1;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
    
}
