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

package at.plechinger.scrapeql.value;

import at.plechinger.scrapeql.ScrapeQLException;

/**
 * Created by lukas on 04.08.15.
 */
public class ValueConverter {


    public static EntityValue toEntityValue(Value val) throws ScrapeQLException {
        if (checkType(val, EntityValue.class)) {
            return EntityValue.class.cast(val);
        }
        throw new ScrapeQLException("Cannot convert " + val.getDataTypeName() + " to " + EntityValue.TYPE_NAME);
    }

    public static ArrayValue toArrayValue(Value val) {
        if (checkType(val, ArrayValue.TYPE_NAME)) {
            return ArrayValue.class.cast(val);
        } else {
            return new ArrayValue(val);
        }
    }


    public static boolean checkType(Value val, String type) {
        return val.getDataTypeName().equals(type);
    }

    public static boolean checkType(Value val1, Value val2) {
        return checkType(val1, val2.getClass());
    }

    public static boolean checkType(Value val, Class<? extends Value> clazz) {
        return val.getClass().isAssignableFrom(EntityValue.class);
    }

    public static boolean checkTypeExact(Value val1, Value val2) {
        return checkType(val1, val2) && checkType(val1, val2.getDataTypeName());
    }


    public static Class<? extends Value> getValueClassFor(Class<?> clazz) {

        switch (clazz.getSimpleName()) {
            case "String":
                return StringValue.class;
            case "Integer":
            case "Long":
            case "Byte":
            case "Short":
                return IntegerValue.class;
            case "Float":
            case "Double":
                return FloatValue.class;
            case "Relation":
                return RelationValue.class;
            case "Boolelan":
                return BooleanValue.class;
            case "Entity":
                return EntityValue.class;
            case "Date":
                return IntegerValue.class;
        }

        return Value.class;
    }
}
