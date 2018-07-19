package com.github.entrypointkr.entrylib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

/**
 * Created by JunHyeong Lim on 2018-05-28
 */
public class CommandAdapter extends Command implements PluginIdentifiableCommand {
    private final Plugin plugin;

    public CommandAdapter(String name, Plugin plugin) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return true;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
