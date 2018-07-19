package com.github.entrypointkr.entrylib;

import com.github.entrypointkr.entrylib.bukkit.Events;
import com.github.entrypointkr.entrylib.command.Commands;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by JunHyeong Lim on 2018-05-27
 */
public class EntryLibrary extends JavaPlugin {
    private static Plugin plugin;

    public EntryLibrary() {
        plugin = this;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        Commands.init(this);
        EntryLibCommand.register(this);
        Events.init(this);
    }
}
