package com.github.entrypointkr.entrylib.command;

/**
 * Created by JunHyeong Lim on 2018-05-28
 */
public interface BukkitCommand extends ICommand<CommandSenderEx, CommandArguments>, CommandInformation {
    @Override
    void execute(CommandSenderEx sender, CommandArguments args);

    @Override
    default String description() {
        return null;
    }

    @Override
    default String usage() {
        return null;
    }
}
