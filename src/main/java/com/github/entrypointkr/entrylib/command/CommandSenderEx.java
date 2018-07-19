package com.github.entrypointkr.entrylib.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by JunHyeong Lim on 2018-05-28
 */
public interface CommandSenderEx extends CommandSender {
    Optional<Player> getPlayer();

    Player getPlayerOrThrow(String message);
}
