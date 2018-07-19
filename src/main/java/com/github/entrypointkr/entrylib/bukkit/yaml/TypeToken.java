package com.github.entrypointkr.entrylib.bukkit.yaml;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by JunHyeong Lim on 2018-07-17
 */
public class TypeToken<T> {
    private final Type type;
    private final Class<T> rawType;

    @SuppressWarnings("unchecked")
    protected TypeToken() {
        Type superclass = getClass().getGenericSuperclass();
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        this.rawType = (Class<T>) $Gson$Types.getRawType(type);
    }

    public Class<T> getRawType() {
        return rawType;
    }
}
