package com.github.entrypointkr.entrylib.bukkit.yaml;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by JunHyeong Lim on 2018-07-16
 */
public class ReflectiveConstructor extends Constructor {
    protected static Object create(Class<?> ancestor) throws InstantiationException {
        ReflectionFactory factory = ReflectionFactory.getReflectionFactory();
        try {
            java.lang.reflect.Constructor<?> def = Object.class.getDeclaredConstructor();
            java.lang.reflect.Constructor<?> constructor = factory.newConstructorForSerialization(ancestor, def);
            return ancestor.cast(constructor.newInstance());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InstantiationException();
        }
    }

    public ReflectiveConstructor() {
        Construct mapConstructor = yamlConstructors.get(Tag.MAP);
        Construct mappingConstructor = yamlClassConstructors.get(NodeId.mapping);
        yamlConstructors.put(Tag.MAP, new SerializableConstructMap());
        yamlClassConstructors.put(NodeId.mapping, new SerializableConstructMapping());
//        yamlConstructors.put(Tag.MAP, new ConstructConfigurationSerializable(mapConstructor::construct));
//        yamlClassConstructors.put(NodeId.mapping, new ConstructConfigurationSerializable(mappingConstructor::construct));
    }

    @Override
    protected Object newInstance(Class<?> ancestor, Node node, boolean tryDefault)
            throws InstantiationException {
        final Class<?> type = node.getType();
        if (typeDefinitions.containsKey(type)) {
            TypeDescription td = typeDefinitions.get(type);
            final Object instance = td.newInstance(node);
            if (instance != null) {
                return instance;
            }
        }
        if (tryDefault && ancestor.isAssignableFrom(type) && !Modifier.isAbstract(type.getModifiers())) {
            try {
                return create(type);
            } catch (Exception e) {
                throw new YAMLException(e);
            }
        }
        throw new InstantiationException();
    }

//    @Override
//    protected Construct getConstructor(Node node) {
//        if (node.getTag() == Tag.MAP
//                || ConfigurationSerializable.class.isAssignableFrom(node.getType())) {
//            return new ConstructConfigurationSerializable();
//        }
//        return super.getConstructor(node);
//    }

    @Override
    protected Class<?> getClassForName(String name) throws ClassNotFoundException {
        return Class.forName(name, true, getClass().getClassLoader());
    }

    protected class SerializableConstructMapping extends ConstructMapping {
        @Override
        public Object construct(Node node) {
            MappingNode mappingNode = ((MappingNode) node);
            if (ConfigurationSerializable.class.isAssignableFrom(node.getType())) {
                Map<String, Object> map = constructMapping(mappingNode).entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
                return ConfigurationSerialization.deserializeObject(map);
            }
            return super.construct(node);
        }
    }

    protected class SerializableConstructMap extends ConstructYamlMap {
        @Override
        @SuppressWarnings("unchecked")
        public Object construct(Node node) {
            Map<Object, Object> map = (Map<Object, Object>) super.construct(node);
            if (map.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                Map<String, Object> converted = map.entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
                return ConfigurationSerialization.deserializeObject(converted);
            }
            return map;
        }
    }

    protected class ConstructConfigurationSerializable implements Construct {
        private final Function<Node, Object> factory;

        public ConstructConfigurationSerializable(Function<Node, Object> factory) {
            this.factory = factory;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object construct(Node node) {
            Object data = factory.apply(node);
            if (data instanceof Map) {
                Map<String, Object> converted = ((Map<Object, Object>) data).entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
                return ConfigurationSerialization.deserializeObject(converted);
            }
            return data;
        }

        @Override
        public void construct2ndStep(Node node, Object object) {
            throw new UnsupportedOperationException();
        }
    }
}
