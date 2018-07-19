package com.github.entrypointkr.entrylib.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JunHyeong Lim on 2018-05-27
 */
public class CommandExceptionCatcher<T, E extends List<String>> implements ICommand<T, E> {
    private final ICommand<T, E> command;
    private final ExceptionHandler<T, List<String>> handler;

    private CommandExceptionCatcher(ICommand<T, E> command, ExceptionHandler<T, List<String>> handler) {
        this.command = command;
        this.handler = handler;
    }

    public static <T, E extends List<String>> CommandExceptionCatcher<T, E> of(ICommand<T, E> command, ExceptionHandler<T, List<String>> handler) {
        return new CommandExceptionCatcher<>(command, handler);
    }

    public static <T, E extends List<String>> CommandExceptionCatcher<T, E> ofAdjust(ICommand<T, E> command, ExceptionHandler<T, List<String>> handler) {
        List<String> cache = new ArrayList<>();
        return of((sender, args) -> {
            cache.clear();
            cache.addAll(args);
            command.execute(sender, args);
        }, (sender, args, ex) -> {
            cache.removeAll(args);
            cache.add(args.isEmpty() ? "" : args.get(args.size() - 1));
            handler.handle(sender, cache, ex);
        });
    }

    public static CommandExceptionCatcher<CommandSenderEx, CommandArguments> ofBukkit(ICommand<CommandSenderEx, CommandArguments> command) {
        return ofAdjust(command, (sender, args, ex) -> {
            if (ex instanceof CommandException) {
                CommandException commandException = ((CommandException) ex);
                String message = ChatColor.RED + getPlainDetailMessage(commandException);
                ICommand<?, ?> failed = commandException.getCommand();
                CommandHelper helper = CommandHelper.of(args);
                helper.write(failed);
                sender.sendMessage("");
                if (StringUtils.isNotBlank(message)) {
                    sender.sendMessage(message);
                }
                sender.sendMessage(helper.toString());
            } else {
                ex.printStackTrace();
                sender.sendMessage(ChatColor.RED + "An error has occurred. " + ex);
            }
        });
    }

    public static String getPlainDetailMessage(Throwable throwable) {
        Throwable cause = throwable.getCause();
        return cause != null
                ? getPlainDetailMessage(cause)
                : throwable.getMessage();
    }

    @Override
    public void execute(T data, E args) {
        try {
            command.execute(data, args);
        } catch (Exception ex) {
            handler.handle(data, args, ex);
        }
    }

    public interface ExceptionHandler<T, E extends List<String>> {
        void handle(T data, E args, Throwable th);
    }
}
