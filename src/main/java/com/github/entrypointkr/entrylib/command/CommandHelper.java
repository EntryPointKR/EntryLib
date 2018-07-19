package com.github.entrypointkr.entrylib.command;

import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by JunHyeong Lim on 2018-06-01
 */
public class CommandHelper {
    private final StringBuilder builder = new StringBuilder();
    private final StringBuilder prefix = new StringBuilder();
    private final Set<Integer> ignoreDuplicate = new HashSet<>();
    private final String lastArgument;

    public CommandHelper(String prefix, String lastArgument) {
        this.lastArgument = lastArgument;
        this.prefix.append(prefix);
    }

    public static CommandHelper of(List<String> arguments) {
        Validate.isTrue(!arguments.isEmpty(), "Should be not empty.");
        String lastArgument = arguments.remove(arguments.size() - 1);
        String prefix = String.join(" ", arguments);
        return new CommandHelper(prefix, lastArgument);
    }

    public static CommandInformation getCommandInformation(ICommand command) {
        return command instanceof CommandInformation
                ? ((CommandInformation) command)
                : DefaultCommandInformation.INSTANCE;
    }

    public void write(ICommand<?, ?> command) {
        write(command, prefix.toString());
    }

    public void write(ICommand<?, ?> command, String tempPrefix) {
        int hashCode = command.hashCode();
        if (!ignoreDuplicate.add(hashCode)) {
            return;
        }

        if (command instanceof MapCommand) {
            MapCommand<?, ?> mapCommand = ((MapCommand<?, ?>) command);
            Predicate<Map.Entry<String, ? extends ICommand<?, ?>>> predicate = entry -> entry.getKey().startsWith(lastArgument);
            List<Map.Entry<String, ? extends ICommand<?, ?>>> list = mapCommand.entrySet().stream().anyMatch(predicate)
                    ? mapCommand.entrySet().stream().filter(predicate).collect(Collectors.toList())
                    : new ArrayList<>(mapCommand.entrySet());
            list.forEach(entry -> write(entry.getValue(), prefix + " " + entry.getKey()));
        } else {
            if (builder.length() > 0) {
                builder.append('\n');
            }
            CommandInformation information = CommandHelper.getCommandInformation(command);
            String usage = Optional.ofNullable(information.usage()).orElse("");
            String desc = Optional.ofNullable(information.description()).orElse("");

            builder.append(tempPrefix);
            if (!usage.isEmpty()) {
                builder.append(' ').append(usage);
            }
            if (!desc.isEmpty()) {
                builder.append(": ").append(desc);
            }
        }
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
