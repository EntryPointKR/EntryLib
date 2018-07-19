package com.github.entrypointkr.entrylib.command;

import com.github.entrypointkr.entrylib.bukkit.Players;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by JunHyeong Lim on 2018-05-28
 */
public class Commands {
    private static final CommandMap COMMAND_MAP = getInternalCommandMap();
    private static final MapCommand<CommandSenderEx, CommandArguments> COMMAND = MapCommand.ofBukkit();
    private static final ICommand<CommandSenderEx, CommandArguments> INTERNAL = CommandExceptionCatcher.ofBukkit(COMMAND);

    private Commands() {
    }

    public static void init(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new CommandProcessor(), plugin);
    }

    public static MapCommand<CommandSenderEx, CommandArguments> getCommand() {
        return COMMAND;
    }

    public static void execute(CommandSender sender, String command) {
        CommandSenderEx mclSender = new EntryLibCommandSender(sender);
        CommandArguments arguments = EntryLibCommandArguments.of(command.split(" "));
        if (!arguments.isEmpty() && COMMAND.has(arguments.get(0))) {
            INTERNAL.execute(mclSender, arguments);
        }
    }

    public static List<String> tabComplete(CommandSender sender, String buffer, List<String> defCompletes) {
        if (!buffer.isEmpty()) {
            CommandSenderEx mclSender = new EntryLibCommandSender(sender);
            CommandArguments arguments = EntryLibCommandArguments.of(StringUtils.splitByWholeSeparator(buffer, " "));
            List<String> ret = COMMAND.tabComplete(mclSender, arguments);
            if (!ret.isEmpty()) {
                return ret;
            }
        }
        return new ArrayList<>(defCompletes);
    }

    public static List<String> tabComplete(CommandSender sender, String buffer) {
        return tabComplete(sender, buffer, Players.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
    }

    public static void register(Plugin plugin, ICommand<CommandSenderEx, CommandArguments> command, String... aliases) {
        for (String alias : aliases) {
            COMMAND.put(alias, command);
            CommandAdapter mockCommand = new CommandAdapter(alias, plugin);
            COMMAND_MAP.register(alias, mockCommand);
        }
    }

    private static CommandMap getInternalCommandMap() {
        try {
            Field mapField = SimplePluginManager.class.getDeclaredField("commandMap");
            mapField.setAccessible(true);
            return (CommandMap) mapField.get(Bukkit.getPluginManager());
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    static class CommandProcessor implements Listener {
        @EventHandler
        public void onCommand(PlayerCommandPreprocessEvent e) {
            String message = e.getMessage();
            execute(e.getPlayer(), message.substring(1));
        }

        @EventHandler
        public void onCommand(ServerCommandEvent e) {
            execute(e.getSender(), e.getCommand());
        }

        @EventHandler
        public void onTabComplete(TabCompleteEvent e) {
            CommandSender sender = e.getSender();
            String command = sender instanceof ConsoleCommandSender
                    ? e.getBuffer() : e.getBuffer().substring(1);
            e.setCompletions(tabComplete(sender, command, e.getCompletions()));
        }
    }
}
