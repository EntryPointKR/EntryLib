package com.github.entrypointkr.entrylib.bukkit.gui;

import com.github.entrypointkr.entrylib.general.DelegateOptionalMap;
import com.github.entrypointkr.entrylib.general.OptionalMap;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by JunHyeong Lim on 2018-07-12
 */
public class ClickListener implements Consumer<InventoryEvent> {
    private final OptionalMap<Integer, GUIListeners<InventoryClickEvent>> map = DelegateOptionalMap.ofHashMap();

    public Optional<GUIListeners<InventoryClickEvent>> get(Integer slot) {
        return map.getOptional(slot);
    }

    @SafeVarargs
    public final ClickListener add(Integer slot, Consumer<InventoryClickEvent>... listeners) {
        map.computeIfAbsent(slot, k -> new GUIListeners<>()).addListener(listeners);
        return this;
    }

    @Override
    public void accept(InventoryEvent e) {
        if (e instanceof InventoryClickEvent) {
            InventoryClickEvent event = ((InventoryClickEvent) e);
            map.getOptional(event.getRawSlot())
                    .ifPresent(listener -> listener.accept(event));
        }
    }
}
