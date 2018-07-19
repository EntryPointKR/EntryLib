package com.github.entrypointkr.entrylib.bukkit.yaml;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * Created by JunHyeong Lim on 2018-07-17
 */
public class YAMLConfiguration extends FileConfiguration {
    protected final Yaml yaml;

    public YAMLConfiguration(Yaml yaml) {
        this.yaml = yaml;
    }

    public static void applyMapToSection(Map<?, ?> map, ConfigurationSection section) {
        map.forEach((key, val) -> {
            if (val instanceof Map) {
                applyMapToSection((Map<?, ?>) val, section.createSection(key.toString()));
            } else {
                section.set(key.toString(), val);
            }
        });
    }

    public static YAMLConfiguration loadReflective(Reader reader) {
        YAMLConfiguration config = new YAMLConfiguration(Yamls.createReflectiveYaml());
        try {
            config.load(reader);
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getConsoleSender().sendMessage("Cannot load configuration from stream " + e);
        }
        return config;
    }

    @Override
    public String saveToString() {
        return yaml.dump(getValues(false));
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        Map<?, ?> map = yaml.load(contents);
        applyMapToSection(map, this);
    }

    @Override
    protected String buildHeader() {
        return "";
    }
}
