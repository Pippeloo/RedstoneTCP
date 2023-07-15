package org.pippeloo.redstonetcp.handlers;

import com.google.gson.Gson;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.pippeloo.redstonetcp.RedstoneTCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

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

    public void toggleSigns(String message) {

        // Get the channel and status from the JSON
        String channel = message.split(":")[0];
        Boolean status = message.split(":")[1].equals("true");

        List<Location> signLocations = RedstoneTCP.getInstance().getSignStorage().getSignLocations(channel);

        if (signLocations != null) {
            // Loop through all the signs with the channel
            for (Location signLocation : signLocations) {
                // If the status is true, place a new block next to the sign
                if (status) {
                    RedstoneTCP.getInstance().getLogger().info("Placing");

                    // Schedule the block placement task to run synchronously on the main server thread
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // Get the block next to the sign
                            Block adjacentBlock = signLocation.getBlock().getRelative(BlockFace.SOUTH); // Replace SOUTH with the desired direction

                            // Set the adjacent block to a new material (e.g., DIAMOND_BLOCK)
                            adjacentBlock.setType(Material.REDSTONE_TORCH);
                        }
                    }.runTask(RedstoneTCP.getInstance());
                } else {
                    // If the status is false, set air next to the sign

                    // Schedule the block removal task to run synchronously on the main server thread
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // Get the block next to the sign
                            Block adjacentBlock = signLocation.getBlock().getRelative(BlockFace.SOUTH); // Replace SOUTH with the desired direction

                            // Set the adjacent block to air
                            adjacentBlock.setType(Material.AIR);
                        }
                    }.runTask(RedstoneTCP.getInstance());
                }
            }
        }
    }
}
