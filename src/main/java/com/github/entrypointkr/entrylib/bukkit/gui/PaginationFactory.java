package com.github.entrypointkr.entrylib.bukkit.gui;

import com.github.entrypointkr.entrylib.bukkit.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by JunHyeong Lim on 2018-07-12
 */
public class PaginationFactory extends AbstractInventoryFactory<PaginationFactory> implements ListInventoryFactory<PaginationFactory>, Consumer<InventoryEvent> {
    private final GUIListeners<InventoryEvent> preListeners;
    private final GUIListeners<InventoryEvent> listeners;
    protected int page = 0;
    protected final List<ItemStack> items = new ArrayList<>();

    public PaginationFactory(GUIListeners<InventoryEvent> preListeners, GUIListeners<InventoryEvent> listeners) {
        this.preListeners = preListeners;
        this.listeners = listeners;
        preListeners.addListener(new CancelHandler((e, slot) -> slot >= (row - 1) * 9 && slot < e.getInventory().getSize()));
    }

    public PaginationFactory(GUIListeners<InventoryEvent> listeners) {
        this(new GUIListeners<>(), listeners);
    }

    public PaginationFactory() {
        this(new GUIListeners<>());
    }

    @Override
    public PaginationFactory addItem(ItemStack... items) {
        this.items.addAll(Arrays.asList(items));
        return getInstance();
    }

    @Override
    public PaginationFactory addItem(Collection<ItemStack> items) {
        this.items.addAll(items);
        return getInstance();
    }

    @Override
    public void accept(InventoryEvent e) {
        preListeners.accept(e);
        int size = (row - 1) * 9;
        if (e instanceof InventoryClickEvent) {
            InventoryClickEvent event = ((InventoryClickEvent) e);
            int clickedSlot = event.getRawSlot();
            if (clickedSlot < size) {
                InventoryClickEvent slotAdjustedEvent = new InventoryClickEvent(
                        event.getView(),
                        event.getSlotType(),
                        page * size + clickedSlot,
                        event.getClick(),
                        event.getAction(),
                        event.getHotbarButton()
                );
                slotAdjustedEvent.setCancelled(event.isCancelled());
                listeners.accept(slotAdjustedEvent);
                event.setCancelled(slotAdjustedEvent.isCancelled());
                return;
            } else if (clickedSlot == size + 2) {
                increasePage(-1);
                refresh(event.getWhoClicked());
            } else if (clickedSlot == size + 6) {
                increasePage(1);
                refresh(event.getWhoClicked());
            }
        }
        listeners.accept(e);
    }

    protected void refresh(HumanEntity human) {
        Inventory topInv = human.getOpenInventory().getTopInventory();
        topInv.setContents(create().getContents());
    }

    protected void increasePage(int amount) {
        page = Math.min(Math.max(0, page + amount), maxPage());
    }

    protected int maxPage() {
        return items.size() / ((row - 1) * 9);
    }

    @Override
    public Inventory create() {
        Inventory inventory = createDefault();
        int size = (row - 1) * 9;
        if (!items.isEmpty()) {
            int offset = page * size;
            int end = (page + 1) * size;
            for (int i = offset; i < end && items.size() > i; i++) {
                inventory.setItem(i, items.get(i));
            }
        }
        inventory.setItem(size + 2, new ItemBuilder(Material.STONE_BUTTON)
                .name(String.format("%s/%s", page + 1, maxPage() + 1))
                .build());
        inventory.setItem(size + 6, new ItemBuilder(Material.STONE_BUTTON)
                .name(String.format("%s/%s", page + 1, maxPage() + 1))
                .build());
        return inventory;
    }

    @Override
    protected PaginationFactory getInstance() {
        return this;
    }

    public GUIListeners<InventoryEvent> getPreListeners() {
        return preListeners;
    }

    public GUIListeners<InventoryEvent> getListeners() {
        return listeners;
    }
}
