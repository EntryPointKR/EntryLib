package com.github.entrypointkr.entrylib.general;

import java.util.List;
import java.util.Optional;

/**
 * Created by JunHyeong Lim on 2018-06-01
 */
public interface OptionalList<T> extends List<T> {
    Optional<T> getOptional(int index);

    Optional<T> removeOptional(int index);
}
