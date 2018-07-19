package com.github.entrypointkr.entrylib.general;

import java.util.Map;
import java.util.Optional;

/**
 * Created by JunHyeong Lim on 2018-06-21
 */
public interface OptionalMap<K, V> extends Map<K, V> {
    Optional<V> getOptional(K key);

    Optional<V> removeOptional(K key);
}
