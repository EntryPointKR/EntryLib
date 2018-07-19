package com.github.entrypointkr.entrylib.bukkit.gui;

import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Created by JunHyeong Lim on 2018-07-12
 */
public interface ListInventoryFactory<I extends ListInventoryFactory<I>> extends InventoryFactory {
    I addItem(ItemStack... items);

    I addItem(Collection<ItemStack> items);
}
