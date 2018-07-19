package com.github.entrypointkr.entrylib.bukkit.yaml;

import com.github.entrypointkr.entrylib.general.FieldEx;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.FieldProperty;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.lang.reflect.Field;

/**
 * Created by JunHyeong Lim on 2018-07-16
 */
public class ReflectivePropertyUtils extends PropertyUtils {
    @Override
    public Property getProperty(Class<?> type, String name, BeanAccess bAccess) {
        Property property = super.getProperty(type, name, bAccess);
        if (property instanceof FieldProperty) {
            FieldProperty fieldProperty = ((FieldProperty) property);
            FieldEx.find(FieldProperty.class, "field")
                    .flatMap(f -> f.<Field>get(fieldProperty))
                    .ifPresent(FieldEx::unlock);
        }
        return property;
    }
}
