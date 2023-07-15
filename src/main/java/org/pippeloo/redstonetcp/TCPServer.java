package org.pippeloo.redstonetcp;

import org.pippeloo.redstonetcp.handlers.TCPConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    ServerSocket serverSocket;
    private final int port = RedstoneTCP.getPluginConfig().getInt("tcp.port");
    private boolean acceptingConnections = true;


    public TCPServer() {
        startTCPServer();
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
            RedstoneTCP.getInstance().getLogger().info("The TCP server is now running on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopTCPServer() {
        try {
            RedstoneTCP.getInstance().getLogger().info("Closing the TCP server...");

            // Set the flag to stop accepting new connections
            acceptingConnections = false;

            // Close the server socket
            serverSocket.close();

            RedstoneTCP.getInstance().getLogger().info("TCP server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
