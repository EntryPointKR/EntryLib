package com.github.entrypointkr.entrylib.command;

import com.github.entrypointkr.entrylib.general.OptionalList;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by JunHyeong Lim on 2018-05-28
 */
public interface CommandArguments extends OptionalList<String> {
    String getOrThrow(int index, String message);

    Optional<Player> getPlayer(int index);

    Player getPlayerOrThrow(int index, String message);

    Optional<World> getWorld(int index);

    World getWorldOrThrow(int index, String message);

    Optional<Number> getNumber(int index);

    Number getNumberOrThrow(int index, String message);
}
