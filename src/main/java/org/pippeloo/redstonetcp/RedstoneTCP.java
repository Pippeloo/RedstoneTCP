package org.pippeloo.redstonetcp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.pippeloo.redstonetcp.listeners.SignChangeListener;


public final class RedstoneTCP extends JavaPlugin {

    private static RedstoneTCP instance;
    private static FileConfiguration config;
    TCPServer tcpServer;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        getLogger().info("RedstoneTCP is starting up...");

        // Save the default config if it doesn't exist
        saveDefaultConfig();

        // Load the config
        config = getConfig();

        // Start the TCP server
        tcpServer = new TCPServer();

        getServer().getPluginManager().registerEvents(new SignChangeListener(), this);

        getLogger().info("RedstoneTCP has started up!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("RedstoneTCP is shutting down...");
        // Stop the TCP server
        tcpServer.stopTCPServer();

        getLogger().info("RedstoneTCP has shut down!");
    }

    public static RedstoneTCP getInstance() {
        return instance;
    }

    public static FileConfiguration getPluginConfig() {
        return config;
    }

}
