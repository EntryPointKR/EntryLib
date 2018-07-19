package com.github.entrypointkr.entrylib.command;

import com.github.entrypointkr.entrylib.bukkit.Players;
import com.github.entrypointkr.entrylib.general.DelegateOptionalList;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by JunHyeong Lim on 2018-05-28
 */
public class EntryLibCommandArguments extends DelegateOptionalList<String> implements CommandArguments {
    public EntryLibCommandArguments(List<String> delegate) {
        super(delegate);
    }

    public static EntryLibCommandArguments of(Collection<String> args) {
        return new EntryLibCommandArguments(new LinkedList<>(args));
    }

    public static EntryLibCommandArguments of(String[] args) {
        return of(Arrays.asList(args));
    }

    @Override
    public String getOrThrow(int index, String message) {
        return getOptional(index).orElseThrow(() -> new IllegalStateException(message));
    }

    @Override
    public Optional<Player> getPlayer(int index) {
        return getOptional(index).flatMap(Players::getPlayer);
    }

    @Override
    public Player getPlayerOrThrow(int index, String message) {
        return getPlayer(index).orElseThrow(() -> new IllegalStateException(message));
    }

    @Override
    public Optional<World> getWorld(int index) {
        return getOptional(index).map(Bukkit::getWorld);
    }

    @Override
    public World getWorldOrThrow(int index, String message) {
        return getWorld(index).orElseThrow(() -> new IllegalStateException(message));
    }

    @Override
    public Optional<Number> getNumber(int index) {
        return getOptional(index).map(arg -> {
            try {
                return NumberUtils.createNumber(arg);
            } catch (Exception ex) {
                return null;
            }
        });
    }

    @Override
    public Number getNumberOrThrow(int index, String message) {
        return getNumber(index).orElseThrow(() -> new IllegalStateException(message));
    }
}
