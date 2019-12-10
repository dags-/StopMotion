package me.dags.stopmotion.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtils {

    public static String getTypeName(Object o) {
        Class c = o.getClass();
        while (c != Object.class) {
            Type type = c.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType param = (ParameterizedType) type;
                if (param.getActualTypeArguments().length == 1) {
                    type = param.getActualTypeArguments()[0];
                    if (type instanceof Class) {
                        c = (Class) type;
                        return c.getSimpleName();
                    }
                }
            }
            c = c.getSuperclass();
        }
        return o.getClass().getSimpleName();
    }
}
