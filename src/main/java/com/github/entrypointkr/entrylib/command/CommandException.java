package com.github.entrypointkr.entrylib.command;

/**
 * Created by JunHyeong Lim on 2018-05-27
 */
public class CommandException extends RuntimeException {
    private final ICommand<?, ?> command;

    public CommandException(ICommand<?, ?> command) {
        this.command = command;
    }

    public CommandException(String message, ICommand<?, ?> command) {
        super(message);
        this.command = command;
    }

    public CommandException(String message, Throwable cause, ICommand<?, ?> command) {
        super(message, cause);
        this.command = command;
    }

    public CommandException(Throwable cause, ICommand<?, ?> command) {
        super(cause);
        this.command = command;
    }

    public CommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ICommand<?, ?> command) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.command = command;
    }

    public ICommand<?, ?> getCommand() {
        return command;
    }
}
