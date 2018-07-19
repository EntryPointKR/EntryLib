package com.github.entrypointkr.entrylib.bukkit;

import org.bukkit.ChatColor;

/**
 * Created by JunHyeong Lim on 2018-06-01
 */
public class Colors {
    public static String colorize(String contents) {
        return ChatColor.translateAlternateColorCodes('&', contents);
    }
}
