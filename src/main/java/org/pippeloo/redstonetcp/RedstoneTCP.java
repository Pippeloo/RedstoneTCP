package org.pippeloo.redstonetcp;

import org.bukkit.plugin.java.JavaPlugin;
import org.pippeloo.redstonetcp.handlers.TCPConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class RedstoneTCP extends JavaPlugin {

    private ServerSocket serverSocket;
    private final int port = 2356;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("RedstoneTCP is starting up...");
        startTCPServer();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("RedstoneTCP is shutting down...");
        stopTCPServer();
    }

    private void startTCPServer() {
        try {
            serverSocket = new ServerSocket(port);
            new Thread(() -> {
                while(!serverSocket.isClosed()) {
                    try {
                        Socket clientSocket = serverSocket.accept();

                        new Thread(() -> {
                            new TCPConnectionHandler().handleClientConnection(clientSocket);
                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            getLogger().info("RedstoneTCP has started up!");
            getLogger().info("The tcp server is now running on port " + port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopTCPServer() {
        try {
            getLogger().info("Closing the tcp server...");
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
