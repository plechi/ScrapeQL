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
import at.plechinger.scrapeql.function.annotation.Param;
import at.plechinger.scrapeql.loader.Entity;
import at.plechinger.scrapeql.relation.Relation;
import at.plechinger.scrapeql.value.*;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.List;

/**
 * Created by lukas on 05.08.15.
 */
@Slf4j
public class AnnotationBasedFunction implements Function {



    private Method method;

    private FunctionDefinition functionDefinition;

    private Object methodOwner;

    private boolean strictMapping = false;


    private Class<?> returnTypeClass;

    private List<Parameter> parameters;

    public AnnotationBasedFunction(Method method) throws IllegalAccessException, InstantiationException, ScrapeQLException {
        this.method = method;
        this.functionDefinition = method.getDeclaredAnnotation(FunctionDefinition.class);
        this.methodOwner = method.getDeclaringClass().newInstance();
        this.strictMapping = this.functionDefinition.strict();
        this.parameters=Lists.newArrayList(method.getParameters());
        this.returnTypeClass = method.getReturnType();
    }


    @Override
    public String getName() {
        return functionDefinition.value();
    }

    @Override
    public Value execute(List<Value> callParams) throws ScrapeQLException {
        try {
            Object[] methodParameter = new Object[callParams.size()];

            for (int j = 0; j < callParams.size(); j++) {

                //occurs on varargs
                Parameter parameter=null;
                if(j<parameters.size()){
                    parameter=parameters.get(j);
                }
                Value callParam=callParams.get(j);

                //if subclass of value: return value
                if(parameter != null && Value.class.isAssignableFrom(parameter.getType())) {
                    methodParameter[j]=callParam;
                }else if(parameter != null && !this.strictMapping && String.class.isAssignableFrom(parameter.getType())){
                    methodParameter[j]=callParam.getStringValue();
                }else{
                    methodParameter[j] = callParam.getValue();
                }
            }

            Object r = method.invoke(methodOwner, methodParameter);
            switch (r.getClass().getSimpleName()) {
                case "String":
                    return new StringValue((String) r);
                case "Integer":
                case "Long":
                case "Byte":
                case "Short":
                    return new IntegerValue(((Number) r).longValue());
                case "Float":
                case "Double":
                    return new FloatValue(((Number) r).doubleValue());
                case "Relation":
                    return new RelationValue((Relation<Value>) r);
                case "Boolean":
                    return new BooleanValue((Boolean) r);
                case "Entity":
                    return new EntityValue((Entity) r);
                case "Date":
                    return new IntegerValue(((Date) r).getTime());
                default:
                    throw new ScrapeQLException("Unknown datatype:" + r.getClass());
            }
        } catch (IllegalArgumentException e) {
            switch (e.getMessage().toLowerCase()) {
                case "wrong number of arguments":
                    throw new ScrapeQLException("Wrong number of arguments",e);
                default:
                    throw new ScrapeQLException("Error while function execution", e);
            }
        } catch (Exception e) {
            throw new ScrapeQLException("Error while function execution", e);
        }
    }
}
