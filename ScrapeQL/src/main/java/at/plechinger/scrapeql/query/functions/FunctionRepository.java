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
package at.plechinger.scrapeql.query.functions;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Lukas Plechinger
 */
public class FunctionRepository {

    private static FunctionRepository functions = null;

    public static FunctionRepository getFunctions() {
        if (functions == null) {
            functions = new FunctionRepository();
        }
        return functions;
    }

    private FunctionRepository() {
        initBaseFunctions();
    }

    private void initBaseFunctions() {
        define(new AttrFunction());
        define(new ConcatFunction());
        define(new DateFunction());
    }

    private Map<String, FunctionDefinition> definedFunctions = new HashMap<>();

    public FunctionDefinition getDefinedFunction(String name) {
        name = name.toLowerCase();
        Preconditions.checkArgument(definedFunctions.containsKey(name), "Function '%s' is not defined.", name);
        return definedFunctions.get(name);
    }

    public void define(FunctionDefinition definition) {
        Preconditions.checkNotNull(definition, "Function definition must not be null.");
        Preconditions.checkNotNull(definition.getName(), "Definition must have a name.");
        String name = definition.getName().toLowerCase();
        Preconditions.checkArgument(!definedFunctions.containsKey(name), "Function '%s' is already defined.", name);
        definedFunctions.put(name, definition);
    }

}
