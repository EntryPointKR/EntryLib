package com.github.entrypointkr.entrylib.command;

import com.github.entrypointkr.entrylib.general.DelegateOptionalMap;
import com.github.entrypointkr.entrylib.general.OptionalList;
import com.github.entrypointkr.entrylib.general.OptionalMap;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by JunHyeong Lim on 2018-05-27
 */
public class MapCommand<T, E extends OptionalList<String>> implements ICommand<T, E> {
    private final OptionalMap<String, ICommand<T, E>> map = DelegateOptionalMap.of(new LinkedHashMap<>());
    private ICommand<T, E> defaultCommand;

    private MapCommand() {
    }

    private MapCommand(ICommand<T, E> defaultCommand) {
        this.defaultCommand = defaultCommand;
    }

    public static <T, E extends OptionalList<String>> MapCommand<T, E> of(ICommand<T, E> defaultCommand) {
        return new MapCommand<>(defaultCommand);
    }

    public static <T, E extends OptionalList<String>> MapCommand<T, E> of(Function<MapCommand<T, E>, ICommand<T, E>> factory) {
        MapCommand<T, E> map = new MapCommand<>();
        map.setDefaultCommand(factory.apply(map));
        return map;
    }

    public static <T, E extends OptionalList<String>> MapCommand<T, E> of() {
        return of(new EmptyCommand<>());
    }

    public static MapCommand<CommandSenderEx, CommandArguments> ofBukkit() {
        return of(mapCommand -> (data, args) -> {
            throw new CommandException("잘못된 사용법입니다.", mapCommand);
        });
    }

    @Override
    public void execute(T data, E args) {
        ICommand<T, E> command = args.getOptional(0)
                .filter(map::containsKey)
                .map(arg -> {
                    args.remove(0);
                    return map.get(arg);
                })
                .orElse(defaultCommand);
        try {
            command.execute(data, args);
        } catch (CommandException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new CommandException(ex, command);
        }
    }

    @Override
    public List<String> tabComplete(T data, E args) {
        if (args.isEmpty()) {
            return Collections.emptyList();
        }
        String argument = args.remove(0);
        return map.getOptional(argument)
                .map(command -> command.tabComplete(data, args))
                .orElseGet(() -> entrySet().stream()
                        .map(Map.Entry::getKey)
                        .filter(key -> key.startsWith(argument))
                        .collect(Collectors.toList()));
    }

    public Set<Map.Entry<String, ICommand<T, E>>> entrySet() {
        return Collections.unmodifiableSet(map.entrySet());
    }

    public MapCommand<T, E> put(String key, ICommand<T, E> command) {
        map.put(key, command);
        return this;
    }

    public MapCommand<T, E> put(ICommand<T, E> command, String... aliases) {
        for (String key : aliases) {
            put(key, command);
        }
        return this;
    }

    public boolean has(String key) {
        return map.containsKey(key);
    }

    public MapCommand<T, E> remove(String alias) {
        map.remove(alias);
        return this;
    }

    public ICommand<T, E> getDefaultCommand() {
        return defaultCommand;
    }

    public MapCommand<T, E> setDefaultCommand(ICommand<T, E> defaultCommand) {
        this.defaultCommand = Objects.requireNonNull(defaultCommand);
        return this;
    }
}
