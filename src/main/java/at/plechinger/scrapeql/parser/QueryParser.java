/*
 * The MIT License
 *
 * Copyright 2015 lukas.
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
package at.plechinger.scrapeql.parser;

import at.plechinger.scrapeql.lang.ScrapeQLBaseListener;
import at.plechinger.scrapeql.lang.ScrapeQLLexer;
import at.plechinger.scrapeql.lang.ScrapeQLParser;
import at.plechinger.scrapeql.query.Query;
import at.plechinger.scrapeql.query.QueryContext;
import at.plechinger.scrapeql.query.variable.FunctionVariable;
import at.plechinger.scrapeql.query.variable.NamedVariable;
import at.plechinger.scrapeql.query.variable.SelectorVariable;
import at.plechinger.scrapeql.query.variable.StringVariable;
import at.plechinger.scrapeql.query.variable.Variable;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 *
 * @author lukas
 */
public class QueryParser {

    public Query parse(String query) {
        ScrapeQLLexer lexer = new ScrapeQLLexer(new ANTLRInputStream(query));

        ScrapeQLParser parser = new ScrapeQLParser(new CommonTokenStream(lexer));

        ParseTree tree = parser.parse();

        final Query parsedQuery = new Query();

        ParseTreeWalker.DEFAULT.walk(new ScrapeQLBaseListener() {

            @Override
            public void enterLoad(ScrapeQLParser.LoadContext ctx) {
                parsedQuery.load(stripEnclosure(ctx.document_name().getText()));
            }

            @Override
            public void enterSelect_single(ScrapeQLParser.Select_singleContext ctx) {

                List<Variable> selectedVariables = new ArrayList<>(ctx.variable_list().variable().size());
                for (ScrapeQLParser.VariableContext var : ctx.variable_list().variable()) {
                    Variable variable = parseExpression(var.expr(), parsedQuery.getContext());
                    variable.as(var.element_name().getText());
                    selectedVariables.add(variable);
                }

                parsedQuery.select(selectedVariables)
                        .from(ctx.from_name().getText());
            }

            @Override
            public void enterSelect_every(ScrapeQLParser.Select_everyContext ctx) {
                List<Variable> selectedVariables = new ArrayList<>(ctx.variable_list().variable().size());
                for (ScrapeQLParser.VariableContext var : ctx.variable_list().variable()) {
                    Variable variable = parseExpression(var.expr(), parsedQuery.getContext());
                    variable.as(var.element_name().getText());
                    selectedVariables.add(variable);
                }

                parsedQuery.selectEvery(selectedVariables)
                        .in(stripEnclosure(ctx.selector_name().getText()))
                        .from(ctx.from_name().getText())
                        .into(ctx.into_name().getText());

            }

            @Override
            public void enterOutput(ScrapeQLParser.OutputContext ctx) {
                parsedQuery.output(ctx.element_name().getText());
            }
        }, tree
        );

        return parsedQuery;
    }

    private String stripEnclosure(String input) {
        return input.substring(1, input.length() - 1);
    }

    private Variable parseExpression(ScrapeQLParser.ExprContext ctx, QueryContext qCtx) {
        if (ctx.selector_name() != null) {
            return new SelectorVariable(stripEnclosure(ctx.selector_name().getText()));
        } else if (ctx.element_name() != null) {
            return new NamedVariable(ctx.element_name().getText());
        } else if (ctx.string_name() != null) {
            return new StringVariable(stripEnclosure(ctx.string_name().getText()));
        } else if (ctx.function_name() != null) {
            List<Variable> variableList = new ArrayList<>(ctx.expr().size());
            for (ScrapeQLParser.ExprContext childCtx : ctx.expr()) {
                variableList.add(parseExpression(childCtx, qCtx));
            }
            return new FunctionVariable(ctx.function_name().getText(), variableList);
        }
        return new StringVariable(ctx.getText());
    }

}
