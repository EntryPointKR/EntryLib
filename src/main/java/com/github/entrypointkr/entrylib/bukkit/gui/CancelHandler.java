package com.github.entrypointkr.entrylib.bukkit.gui;

import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * Created by JunHyeong Lim on 2018-07-13
 */
public class CancelHandler implements Consumer<InventoryEvent> {
    public static final CancelHandler DEFAULT = new CancelHandler((e, slot) -> slot < e.getInventory().getSize());
    private final BiPredicate<InventoryEvent, Integer> predicate;

    public CancelHandler(BiPredicate<InventoryEvent, Integer> predicate) {
        this.predicate = predicate;
    }

    @Override
    public void accept(InventoryEvent e) {
        if (e instanceof InventoryClickEvent) {
            InventoryClickEvent event = ((InventoryClickEvent) e);
            int rawSlot = event.getRawSlot();
            if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
                Inventory topInv = event.getView().getTopInventory();
                ItemStack cursor = event.getCursor();
                for (int i = 0; i < topInv.getSize(); i++) {
                    ItemStack item = topInv.getItem(i);
                    if (cursor.isSimilar(item) && predicate.test(event, i)) {
                        event.setCancelled(true);
                        break;
                    }
                }
            } else {
                if (predicate.test(event, rawSlot)) {
                    event.setCancelled(true);
                }
            }
        } else if (e instanceof InventoryDragEvent) {
            InventoryDragEvent event = ((InventoryDragEvent) e);
            if (event.getRawSlots().stream().anyMatch(slot -> predicate.test(event, slot))) {
                event.setCancelled(true);
            }
        }
    }
}
