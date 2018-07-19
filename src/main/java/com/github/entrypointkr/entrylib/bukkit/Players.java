package com.github.entrypointkr.entrylib.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by JunHyeong Lim on 2018-05-28
 */
public class Players {
    public static Optional<Player> getPlayer(String name) {
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }

    public static Optional<Player> getPlayer(UUID uuid) {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    public static Collection<? extends Player> getOnlinePlayers() {
        try {
            return Bukkit.getOnlinePlayers();
        } catch (NoSuchMethodError th) {
            List<Player> onlinePlayers = new ArrayList<>();
            try {
                Method method = Bukkit.class.getMethod("getOnlinePlayers");
                onlinePlayers.addAll(Arrays.asList((Player[]) method.invoke(null)));
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.WARNING, e, () -> "Failed getOnlinePlayers()");
            }
            return onlinePlayers;
        }
    }
}
