package com.github.entrypointkr.entrylib.bukkit.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by JunHyeong Lim on 2018-07-08
 */
public class MapFactory extends AbstractInventoryFactory<MapFactory> {
    private final Map<Integer, ItemStack> map = new HashMap<>();

    public MapFactory item(ItemStack item, int... slots) {
        IntStream.of(slots).forEach(num -> map.put(num, item));
        return this;
    }

    @Override
    public Inventory create() {
        Inventory inventory = createDefault();
        map.forEach(inventory::setItem);
        return inventory;
    }

    @Override
    protected MapFactory getInstance() {
        return this;
    }
}
