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

import at.plechinger.scrapeql.lang.ScrapeQLParser;
import at.plechinger.scrapeql.parser.expression.ExpressionVariableConverter;
import at.plechinger.scrapeql.query.Query;
import at.plechinger.scrapeql.query.expression.Variable;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author lukas
 */
public class SelectSingleStatementParser extends AbstractStatementParser<ScrapeQLParser.Select_singleContext> {

    private ExpressionVariableConverter converter = new ExpressionVariableConverter();

    public SelectSingleStatementParser() {
        super(ScrapeQLParser.Select_singleContext.class);
    }

    @Override
    protected void parseRule(Query query, ScrapeQLParser.Select_singleContext ctx) {
        List<Variable> selectedVariables = new ArrayList<>(ctx.variable_list().variable().size());
        for (ScrapeQLParser.VariableContext var : ctx.variable_list().variable()) {
            Variable variable = converter.convert(var.expr());
            variable.as(var.element_name().getText());
            selectedVariables.add(variable);
        }

        query.select(selectedVariables)
                .from(ctx.from_name().getText());
    }

}
