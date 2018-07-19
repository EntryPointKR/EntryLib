package com.github.entrypointkr.entrylib;

import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by JunHyeong Lim on 2018-06-01
 */
public class MockFactory {
    public static Server createServer() {
        Server server = mock(Server.class);
        Logger logger = Logger.getLogger("test");
        SimplePluginManager manager = new SimplePluginManager(server, new SimpleCommandMap(server));
        when(server.getPluginManager()).thenReturn(manager);
        when(server.getLogger()).thenReturn(logger);
        return server;
    }
}
