package com.github.entrypointkr.entrylib.command;

/**
 * Created by JunHyeong Lim on 2018-05-28
 */
public class DefaultCommandInformation implements CommandInformation {
    public static final DefaultCommandInformation INSTANCE = new DefaultCommandInformation();

    private DefaultCommandInformation() {
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String usage() {
        return null;
    }
}
