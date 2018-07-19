package com.github.entrypointkr.entrylib.bukkit.yaml;

import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Optional;

/**
 * Created by JunHyeong Lim on 2018-07-16
 */
public class Yamls {
    private static final Yamls DEFAULT = new Yamls(Yamls.createReflectiveYaml());
    private final Yaml yaml;

    public static File createFileIfAbsent(File file) {
        if (!file.isFile()) {
            File parent = file.getParentFile();
            if (!parent.isDirectory() && !file.getParentFile().mkdirs()) {
                throw new IllegalArgumentException("Cannot create the directory");
            }

            try {
                if (!file.createNewFile()) {
                    throw new IllegalStateException("Cannot create the file");
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return file;
    }

    public static Yaml createReflectiveYaml() {
        Representer representer = new YamlRepresenter();
        PropertyUtils utils = new ReflectivePropertyUtils();
        DumperOptions options = new DumperOptions();

        utils.setBeanAccess(BeanAccess.FIELD);
        utils.setAllowReadOnlyProperties(true);
        representer.setPropertyUtils(utils);
        representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        return new Yaml(new ReflectiveConstructor(), representer, options);
    }

    public static Yamls getDefault() {
        return DEFAULT;
    }

    public Yamls(Yaml yaml) {
        this.yaml = yaml;
    }

    public void write(Writer writer, Object object) {
        yaml.dump(object, writer);
    }

    public void write(Plugin plugin, String name, Object object) {
        File file = createFileIfAbsent(new File(plugin.getDataFolder(), name));
        try {
            write(new FileWriter(file), object);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T> Optional<T> read(Reader reader) {
        return Optional.ofNullable(yaml.load(reader));
    }

    public <T> Optional<T> read(Plugin plugin, String name) {
        File file = createFileIfAbsent(new File(plugin.getDataFolder(), name));
        try {
            return read(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
