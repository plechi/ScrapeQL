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
package at.plechinger.scrapeql.parser.statement;

import at.plechinger.scrapeql.parser.statement.StatementParser;
import at.plechinger.scrapeql.query.Query;
import com.google.common.base.Preconditions;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 *
 * @author lukas
 */
public abstract class AbstractStatementParser<T extends ParserRuleContext> implements StatementParser {

    private Class<T> clazz;

    public AbstractStatementParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean isSuited(ParserRuleContext ctx) {
        return clazz.isAssignableFrom(ctx.getClass());
    }

    @Override
    public void parse(Query query, ParserRuleContext ctx) {
        Preconditions.checkArgument(
                isSuited(ctx),
                "Parser %s is not suited to parse context of type %s.",
                this.getClass().getName(),
                ctx.getClass().getName()
        );

        T context = (T) ctx;

        parseRule(query, context);
    }

    protected abstract void parseRule(Query query, T ctx);

}
