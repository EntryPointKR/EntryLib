package com.github.entrypointkr.entrylib.command;

import java.util.List;

/**
 * Created by JunHyeong Lim on 2018-05-27
 */
public class EmptyCommand<T, E extends List<String>> implements ICommand<T, E> {
    @Override
    public void execute(T data, E args) {
        // Ignore
    }
}
