package com.github.entrypointkr.entrylib.bukkit.gui;

import com.github.entrypointkr.entrylib.bukkit.Events;
import com.github.entrypointkr.entrylib.general.DelegateOptionalMap;
import com.github.entrypointkr.entrylib.general.OptionalMap;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by JunHyeong Lim on 2018-07-08
 */
public class GUI implements Openable, Consumer<InventoryEvent> {
    private static final OptionalMap<String, Consumer<InventoryEvent>> MAP = DelegateOptionalMap.ofHashMap();

    static {
        Events.registerListener(e -> {
            if (e instanceof InventoryEvent) {
                InventoryEvent event = ((InventoryEvent) e);
                HumanEntity human = event.getView().getPlayer();
                get(human.getName()).ifPresent(observer -> {
                    observer.accept(event);
                    if (e instanceof InventoryCloseEvent) {
                        remove(human.getName());
                    }
                });
            }
        });
    }

    private final InventoryFactory factory;
    private final Consumer<InventoryEvent> observer;

    public GUI(InventoryFactory factory, Consumer<InventoryEvent> observer) {
        this.factory = Objects.requireNonNull(factory);
        this.observer = Objects.requireNonNull(observer);
    }

    public static void register(String name, Consumer<InventoryEvent> observer) {
        MAP.put(name, observer);
    }

    public static void remove(String name) {
        MAP.remove(name);
    }

    public static Optional<Consumer<InventoryEvent>> get(String name) {
        return MAP.getOptional(name);
    }

    public static GUI createPagination(PaginationFactory factory) {
        return new GUI(factory, factory);
    }

    @Override
    public void accept(InventoryEvent e) {
        observer.accept(e);
    }

    @Override
    public void open(HumanEntity human) {
        Validate.notNull(human);

        Inventory topInv = human.getOpenInventory().getTopInventory();
        Inventory gui = factory.create();
        if (topInv.getTitle().equals(gui.getTitle())
                && topInv.getSize() == gui.getSize()
                && topInv.getType().equals(gui.getType())) {
            topInv.setContents(gui.getContents());
        } else {
            human.openInventory(gui);
        }
        register(human.getName(), this);
    }
}
