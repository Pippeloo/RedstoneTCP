package org.pippeloo.redstonetcp.handlers;

import org.pippeloo.redstonetcp.RedstoneTCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.bukkit.Bukkit.getLogger;

public class TCPConnectionHandler {
    public Object handleClientConnection(Socket clientSocket) {
        try {
            // Get input and output streams for the client connection
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Send a welcome message to the client
            out.println("Welcome to the TCP server!");

            RedstoneTCP.getInstance().setClientSocket(clientSocket);

            RedstoneTCP.getInstance().getLogger().info("Client connected from " + clientSocket.getInetAddress().getHostAddress());

            // Receive and log messages from the client
            String message;
            while ((message = in.readLine()) != null) {
                getLogger().info("Received message from client: " + message);
                // Check if the message is a JSON string containing the channel and status
                if (message.contains(":")) {
                    toggleSigns(message);
                }
            }

            // Close the client connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendToClient(Socket clientSocket, String message) {
        try {
            // Get input and output streams for the client connection
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Send a welcome message to the client
            out.println(message);
            RedstoneTCP.getInstance().getLogger().info("Sent message to client: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toggleSigns(String json) {
        // Get the channel and status from the JSON
        String channel = json.split(":")[0];
        Boolean status = json.split(":")[1].equals("true");

        // Check all signs if the first line matches [TCPR] and the second line matches the channel
        RedstoneTCP.getInstance().getServer().getWorlds().forEach(world -> {
            world.getEntities().forEach(entity -> {
                if (entity.getType().toString().equals("SIGN")) {
                    if (((org.bukkit.block.Sign) entity).getLine(0).equals("[TCPR]") && ((org.bukkit.block.Sign) entity).getLine(1).equals(channel)) {
                        // If the sign matches, set a redstone torch on the right side of the sign
                        if (status) {
                            entity.getWorld().getBlockAt(entity.getLocation().add(1, 0, 0)).setType(org.bukkit.Material.REDSTONE_TORCH);
                        } else {
                            entity.getWorld().getBlockAt(entity.getLocation().add(1, 0, 0)).setType(org.bukkit.Material.AIR);
                        }
                    }
                }
            });
        });
    }
}
