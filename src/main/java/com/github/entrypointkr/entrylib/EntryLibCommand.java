package com.github.entrypointkr.entrylib;

import com.github.entrypointkr.entrylib.command.BukkitCommand;
import com.github.entrypointkr.entrylib.command.CommandArguments;
import com.github.entrypointkr.entrylib.command.CommandException;
import com.github.entrypointkr.entrylib.command.CommandSenderEx;
import com.github.entrypointkr.entrylib.command.Commands;
import com.github.entrypointkr.entrylib.command.MapCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by JunHyeong Lim on 2018-05-28
 */
@SuppressWarnings("deprecation")
class EntryLibCommand {
    static void register(Plugin plugin) {
        Commands.register(plugin, MapCommand.ofBukkit()
                        .put(new HealCommand(), "heal")
                        .put(new JumpCommand(), "jump", "j")
                        .put(new EntitiesKillCommand(), "killall")
                , "softlibrary", "softlib", "sl");
    }

    static class HealCommand implements BukkitCommand {
        @Override
        public void execute(CommandSenderEx sender, CommandArguments args) {
            Player player = args.getPlayer(0).orElseGet(() -> sender.getPlayer().orElseThrow(() ->
                    new CommandException("지정된 플레이어가 없습니다.", this)));
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(30);
        }
    }

    static class JumpCommand implements BukkitCommand {
        static final Set<Material> IGNORE_MATERIALS = Stream.of(Material.values())
                .filter(Material::isTransparent)
                .collect(Collectors.toSet());

        @Override
        public void execute(CommandSenderEx sender, CommandArguments args) {
            Player player = sender.getPlayer().orElseThrow(() -> new CommandException("플레이어만 사용 가능합니다.", this));
            Location loc = player.getLocation();
            Location target = player.getTargetBlock(IGNORE_MATERIALS, 200).getLocation().add(0, 1, 0);
            target.setYaw(loc.getYaw());
            target.setPitch(loc.getPitch());
            player.teleport(target);
        }
    }

    static class EntitiesKillCommand implements BukkitCommand {
        @Override
        public void execute(CommandSenderEx sender, CommandArguments args) {
            World world = args.getWorld(0).orElseGet(() -> Bukkit.getWorlds().get(0));
            AtomicInteger counter = new AtomicInteger();
            world.getLivingEntities().stream().filter(Monster.class::isInstance).forEach(entity -> {
                counter.incrementAndGet();
                entity.remove();
            });
            sender.sendMessage(String.format("%s 월드의 엔티티가 %s 개 삭제되었습니다.", world.getName(), counter.get()));
        }
    }
}
