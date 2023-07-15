package org.pippeloo.redstonetcp;

import org.bukkit.plugin.java.JavaPlugin;
import org.pippeloo.redstonetcp.handlers.TCPConnectionHandler;
import org.pippeloo.redstonetcp.listeners.SignChangeListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public final class RedstoneTCP extends JavaPlugin {

    private static RedstoneTCP instance;
    private ServerSocket serverSocket;
    private final int port = 2356;
    private boolean acceptingConnections = true;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        getLogger().info("RedstoneTCP is starting up...");
        startTCPServer();

        getServer().getPluginManager().registerEvents(new SignChangeListener(), this);

        getLogger().info("RedstoneTCP has started up!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("RedstoneTCP is shutting down...");
        stopTCPServer();
        getLogger().info("RedstoneTCP has shut down!");
    }

    private void startTCPServer() {
        try {
            serverSocket = new ServerSocket(port);
            new Thread(() -> {
                while (acceptingConnections) {
                    try {
                        Socket clientSocket = serverSocket.accept();

                        new Thread(() -> {
                            new TCPConnectionHandler().handleClientConnection(clientSocket);
                        }).start();
                    } catch (IOException e) {
                        if (!serverSocket.isClosed()) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            getLogger().info("The TCP server is now running on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopTCPServer() {
        try {
            getLogger().info("Closing the TCP server...");

            // Set the flag to stop accepting new connections
            acceptingConnections = false;

            // Close the server socket
            serverSocket.close();

            getLogger().info("TCP server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RedstoneTCP getInstance() {
        return instance;
    }

}
