package com.github.entrypointkr.entrylib.bukkit;

import com.github.entrypointkr.entrylib.general.FieldEx;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by JunHyeong Lim on 2018-07-05
 */
public class Events {
    private static final Set<Consumer<Event>> LISTENERS = new HashSet<>();

    public static void init(Plugin plugin) {
        FieldEx field = FieldEx.find(HandlerList.class, "allLists")
                .orElseThrow(IllegalStateException::new);
        List<HandlerList> handlers = field.<List<HandlerList>>get().orElseThrow(IllegalStateException::new);
        ArrayList<HandlerList> newHandlers = new ArrayListHook(plugin);
        newHandlers.addAll(handlers);
        field.set(newHandlers);
    }

    @SafeVarargs
    public static void registerListener(Consumer<Event>... listeners) {
        LISTENERS.addAll(Arrays.asList(listeners));
    }

    public static void removeListener(Consumer<Event> listener) {
        LISTENERS.remove(listener);
    }

    static class ArrayListHook extends ArrayList<HandlerList> {
        private final Plugin plugin;

        public ArrayListHook(Plugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean add(HandlerList handlerList) {
            handlerList.register(new RegisteredListener(new Listener() {
            }, null, EventPriority.LOWEST, plugin, false) {
                @Override
                public void callEvent(Event event) {
                    LISTENERS.forEach(listener -> listener.accept(event));
                }
            });
            return super.add(handlerList);
        }

        @Override
        public boolean addAll(Collection<? extends HandlerList> c) {
            for (HandlerList list : c) {
                add(list);
            }
            return true;
        }
    }
}
