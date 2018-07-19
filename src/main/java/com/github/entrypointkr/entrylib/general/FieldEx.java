package com.github.entrypointkr.entrylib.general;

import com.google.gson.internal.Primitives;
import org.apache.commons.lang.Validate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by JunHyeong Lim on 2018-07-05
 */
public class FieldEx {
    private final Field field;

    public FieldEx(Field field) {
        this.field = field;
        unlock(field);
    }

    public static Collection<Field> getFields(Class<?> aClass) {
        Set<Field> ret = new HashSet<>();
        ret.addAll(Arrays.asList(aClass.getFields()));
        ret.addAll(Arrays.asList(aClass.getDeclaredFields()));
        return ret;
    }

    public static Optional<FieldEx> find(Class aClass, String name) {
        return getFields(aClass).stream()
                .filter(f -> f.getName().equals(name))
                .findAny()
                .map(FieldEx::new);
    }

    public static void unlock(Field field) {
        int modifier = field.getModifiers();
        if (!Modifier.isPublic(modifier)) {
            field.setAccessible(true);
        }
        if (Modifier.isFinal(modifier)) {
            find(field.getClass(), "modifiers").ifPresent(f ->
                    f.set(field, field.getModifiers() & ~Modifier.FINAL));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Object instance) {
        try {
            Object data = field.get(instance);
            return Optional.ofNullable((T) data);
        } catch (Exception e) {
            // Ignore
        }
        return Optional.empty();
    }

    public <T> Optional<T> get() {
        return get(null);
    }

    public void set(Object instance, Object val) {
        Class<?> type = field.getType();
        if (type.isPrimitive()) {
            type = Primitives.wrap(type);
        }
        Validate.isTrue(type.isInstance(val), "Mismatched type");
        try {
            field.set(instance, val);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public void set(Object val) {
        set(null, val);
    }
}
