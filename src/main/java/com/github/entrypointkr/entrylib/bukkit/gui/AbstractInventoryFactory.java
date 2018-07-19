package com.github.entrypointkr.entrylib.bukkit.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

/**
 * Created by JunHyeong Lim on 2018-07-08
 */
public abstract class AbstractInventoryFactory<T extends AbstractInventoryFactory<T>> implements InventoryFactory {
    protected InventoryType type;
    protected int row;
    protected String title;

    public AbstractInventoryFactory() {
        type(InventoryType.CHEST);
    }

    public T type(InventoryType type) {
        this.type = Objects.requireNonNull(type);
        row(type.getDefaultSize() / 9);
        title(type.getDefaultTitle());
        return getInstance();
    }

    public T row(int row) {
        this.row = row;
        return getInstance();
    }

    public T title(String title) {
        this.title = Objects.requireNonNull(title);
        return getInstance();
    }

    public Inventory createDefault() {
        return type == InventoryType.CHEST
                ? Bukkit.createInventory(null, row * 9, title)
                : Bukkit.createInventory(null, type, title);
    }

    protected abstract T getInstance();
}
