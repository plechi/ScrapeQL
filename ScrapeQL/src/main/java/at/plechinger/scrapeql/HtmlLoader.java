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
package at.plechinger.scrapeql;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author lukas
 */
public class HtmlLoader {

    private static HtmlLoader loader = null;

    public static HtmlLoader getLoader() {
        if (loader == null) {
            loader = new HtmlLoader();
        }
        return loader;
    }

    LoadingCache<URL, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES).build(cacheLoader);

    private HtmlLoader() {

    }

    public String load(String url) {
        return load(url, true);
    }

    public String load(String url, boolean cached) {
        try {
            URL u=new URL(url);
            if (cached) {
                return cache.get(u);
            }
            return cacheLoader.load(u);
        } catch (Exception ex) {
            throw new RuntimeException("Error while loading html from " + url, ex);
        }
    }

    private static CacheLoader<URL, String> cacheLoader = new CacheLoader<URL, String>() {

        @Override
        public String load(URL url) throws Exception {
            StringBuilder builder = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append('\n');
                }
            }

            return builder.toString();
        }
    };
}
