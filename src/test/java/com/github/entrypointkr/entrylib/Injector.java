package com.github.entrypointkr.entrylib;

import org.bukkit.Bukkit;
import org.bukkit.Server;

/**
 * Created by JunHyeong Lim on 2018-06-01
 */
public class Injector {
    public static void injectBukkit() {
        Server server = MockFactory.createServer();
        Bukkit.setServer(server);
    }
}
