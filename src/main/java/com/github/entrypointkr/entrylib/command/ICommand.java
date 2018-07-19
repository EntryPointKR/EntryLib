package com.github.entrypointkr.entrylib.command;

import java.util.Collections;
import java.util.List;

/**
 * Created by JunHyeong Lim on 2018-05-27
 */
public interface ICommand<T, E extends List<String>> {
    void execute(T data, E args);

    default List<String> tabComplete(T data, E args) {
        return Collections.emptyList();
    }
}
