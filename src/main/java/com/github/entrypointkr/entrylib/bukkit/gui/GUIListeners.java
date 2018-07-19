package com.github.entrypointkr.entrylib.bukkit.gui;

import org.bukkit.event.inventory.InventoryEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by JunHyeong Lim on 2018-07-08
 */
public class GUIListeners<T extends InventoryEvent> implements Consumer<T> {
    private final Set<Consumer<T>> listeners = new HashSet<>();

    @SafeVarargs
    public final GUIListeners<T> addListener(Consumer<T>... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
        return this;
    }

    @Override
    public void accept(T e) {
        listeners.forEach(listener -> listener.accept(e));
    }
}
