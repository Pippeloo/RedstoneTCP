package org.pippeloo.redstonetcp;

import org.bukkit.plugin.java.JavaPlugin;
import org.pippeloo.redstonetcp.handlers.TCPConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public final class RedstoneTCP extends JavaPlugin {

    private ServerSocket serverSocket;
    private final int port = 2356;
    private boolean acceptingConnections = true;

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

            getLogger().info("RedstoneTCP has started up!");
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

}
