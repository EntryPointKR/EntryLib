package com.github.entrypointkr.entrylib.command;

import com.github.entrypointkr.entrylib.bukkit.Players;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.Set;

/**
 * Created by JunHyeong Lim on 2018-05-28
 */
public class EntryLibCommandSender implements CommandSenderEx {
    private final CommandSender delegate;

    public EntryLibCommandSender(CommandSender delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<Player> getPlayer() {
        return Players.getPlayer(getName());
    }

    @Override
    public Player getPlayerOrThrow(String message) {
        return getPlayer().orElseThrow(() -> new IllegalStateException(message));
    }

    @Override
    public void sendMessage(String message) {
        delegate.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        delegate.sendMessage(messages);
    }

    @Override
    public Server getServer() {
        return delegate.getServer();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Spigot spigot() {
        return delegate.spigot();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return delegate.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return delegate.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return delegate.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return delegate.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return delegate.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return delegate.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return delegate.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return delegate.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        delegate.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        delegate.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return delegate.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return delegate.isOp();
    }

    @Override
    public void setOp(boolean value) {
        delegate.setOp(value);
    }
}
