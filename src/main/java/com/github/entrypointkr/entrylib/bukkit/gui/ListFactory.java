package com.github.entrypointkr.entrylib.bukkit.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by JunHyeong Lim on 2018-07-08
 */
public class ListFactory extends AbstractInventoryFactory<ListFactory> implements ListInventoryFactory<ListFactory> {
    public static final BiConsumer<List<ItemStack>, Inventory> APPLIER = (items, inv) -> items.forEach(inv::addItem);
    private final List<ItemStack> items = new ArrayList<>();

    @Override
    public ListFactory addItem(ItemStack... items) {
        addItem(Arrays.asList(items));
        return this;
    }

    @Override
    public ListFactory addItem(Collection<ItemStack> items) {
        this.items.addAll(items);
        return this;
    }

    @Override
    public Inventory create() {
        Inventory inventory = createDefault();
        items.forEach(inventory::addItem);
        return inventory;
    }

    @Override
    protected ListFactory getInstance() {
        return this;
    }

    public List<ItemStack> getItems() {
        return items;
    }
}
