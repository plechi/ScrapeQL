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
package at.plechinger.scrapeql;


import at.plechinger.scrapeql.lang.ScrapeQLBaseListener;
import at.plechinger.scrapeql.lang.ScrapeQLLexer;
import at.plechinger.scrapeql.lang.ScrapeQLParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;


/**
 *
 * @author Lukas Plechinger
 */
public class Parser {

    public static void main(String[] args) {
        String query = "LOAD \"URL\";\n"
                + "SELECT 'selector' AS mainselector FROM ':root';\n"
                + "SELECT FIRST 'h1' AS header FROM genericname; \n"
                + "SELECT EVERY 'selector' AS name1, 'selector' AS name2, CONCAT('selector1','selector2',\"FIXSTRING\") AS name3, header FROM genericname INTO listvariable;\n"
                + "\n"
                + "OUTPUT listvariable;";
        
        
        ScrapeQLLexer lexer = new ScrapeQLLexer(new ANTLRInputStream(query));
        
        ScrapeQLParser parser = new ScrapeQLParser(new CommonTokenStream(lexer));
        
        ParseTree tree=parser.parse();
     
        ParseTreeWalker.DEFAULT.walk(new ScrapeQLBaseListener(){

            @Override
            public void enterLoad(ScrapeQLParser.LoadContext ctx) {
                System.out.println("Load"+ctx.document_name().getText());
            }
            
            
            
        }, tree
        );
        
       
    }
    
}
