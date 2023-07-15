package org.pippeloo.redstonetcp.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.bukkit.Bukkit.getLogger;

public class TCPConnectionHandler {
    public void handleClientConnection(Socket clientSocket) {
        try {
            // Get input and output streams for the client connection
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Send a welcome message to the client
            out.println("Welcome to the TCP server!");

            // Receive and log messages from the client
            String message;
            while ((message = in.readLine()) != null) {
                getLogger().info("Received message from client: " + message);
            }

            // Close the client connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(Socket clientSocket, String message) {
        try {
            // Get input and output streams for the client connection
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Send a welcome message to the client
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
