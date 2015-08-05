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

package at.plechinger.scrapeql.function;

import at.plechinger.scrapeql.ScrapeQLException;
import at.plechinger.scrapeql.function.annotation.FunctionDefinition;
import at.plechinger.scrapeql.function.impl.StringFunctions;
import at.plechinger.scrapeql.value.Value;
import at.plechinger.scrapeql.value.ValueConverter;
import com.google.common.collect.Sets;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;

/**
 * Created by lukas on 04.08.15.
 */
public class FunctionRepository {

    private static FunctionRepository functionRepository=null;

    public static FunctionRepository instance(){
        if(functionRepository==null){
            functionRepository=new FunctionRepository();
        }

        return functionRepository;
    }

    private Set<Function> functions= Sets.newLinkedHashSet();

    private FunctionRepository(){}

    public void register(Function function){
        functions.add(function);
    }

    public Function getFunction(String name) throws ScrapeQLException{
        for(Function function:functions){
            if(function.getName().toLowerCase().equals(name.toLowerCase())){
                return function;
            }
        }
        throw new ScrapeQLException("Function "+name+" does not exist.");
    }


    public void registerFunctions(Class<?> functionClass){
        for(Method method:functionClass.getMethods()){
            if(method.isAnnotationPresent(FunctionDefinition.class)){
                try{
                    register(new AnnotationBasedFunction(method));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) {
        FunctionRepository.instance().registerFunctions(StringFunctions.class);
    }

}
