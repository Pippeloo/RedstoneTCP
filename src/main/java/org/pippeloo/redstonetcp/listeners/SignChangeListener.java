package org.pippeloo.redstonetcp.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.pippeloo.redstonetcp.RedstoneTCP;
import org.pippeloo.redstonetcp.TCPServer;
import org.pippeloo.redstonetcp.handlers.TCPConnectionHandler;

import java.net.Socket;
import java.util.function.Supplier;

public class SignChangeListener implements Listener {


    @EventHandler
    public void onSignChange(SignChangeEvent event) {

        // Retrieve the configuration file
        FileConfiguration config = RedstoneTCP.getPluginConfig();

        // Retrieve the values from the configuration file
        String tcpReceiver = config.getString("sign-text.tcp-receiver");
        String tcpTransmitter = config.getString("sign-text.tcp-transmitter");

        if (!event.getLine(0).equals(tcpTransmitter) && !event.getLine(0).equals(tcpReceiver)) {
            return;
        }

        if (event.getLine(1).isEmpty()) {
            return;
        }

        int x = event.getBlock().getX();
        int y = event.getBlock().getY();
        int z = event.getBlock().getZ();
        boolean isReceiver = event.getLine(0).equals(tcpReceiver);
        String channel = event.getLine(1);

        RedstoneTCP.getInstance().getLogger().info("Sign placed at " + x + ", " + y + ", " + z + " with channel " + channel);

        // Check if the sign is a receiver
        if (isReceiver) {
            // The sign needs to be added to the list of receivers
            RedstoneTCP.getInstance().getSignStorage().addSign(channel, event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockState state = block.getState();

        if (state instanceof Sign) {
            Sign sign = (Sign) state;
            // The sign needs to be removed from the list of receivers
            RedstoneTCP.getInstance().getSignStorage().removeSign(block.getLocation());

            RedstoneTCP.getInstance().getLogger().info("Sign at " + block.getX() + ", " + block.getY() + ", " + block.getZ() + " with channel " + sign.getLine(1) + " removed");
        }
    }

    @EventHandler
    public void onRedstoneEvent(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        // Check if the block is next to a sign
        BlockFace[] blockFaces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.DOWN};

        for (BlockFace face : blockFaces) {
            Block relativeBlock = block.getRelative(face);
            BlockState state = relativeBlock.getState();

            if (state instanceof Sign) {
                Sign sign = (Sign) state;

                if (isTransmitter(sign) && hasChannel(sign)) {
                    // Check if the sign is indirectly powered by redstone
                    boolean isPowered = !isIndirectlyPowered(sign);
                    sendSignStatus(sign, isPowered);

                    RedstoneTCP.getInstance().getLogger().info("Sign at " + block.getX() + ", " + block.getY() + ", " + block.getZ() + " is powered: " + isPowered);
                }
            }
        }
    }

    private boolean isTransmitter(Sign sign) {
        FileConfiguration config = RedstoneTCP.getPluginConfig();
        String tcpTransmitter = config.getString("sign-text.tcp-transmitter");

        return sign.getLine(0).equals(tcpTransmitter);
    }

    private boolean hasChannel(Sign sign) {
        return !sign.getLine(1).isEmpty();
    }

    private boolean isIndirectlyPowered(Sign sign) {
        Block block = sign.getBlock();
        BlockFace[] blockFaces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.DOWN};

        for (BlockFace face : blockFaces) {
            if (indirectPower(face, block) > 0) {
                return true;
            }
        }

        return false;
    }

    private int indirectPower(BlockFace face, Block block) {
        return block.getRelative(face).getBlockPower(face.getOppositeFace());
    }

    private void sendSignStatus(Sign sign, boolean isPowered) {
        TCPConnectionHandler connectionHandler = new TCPConnectionHandler();
        Socket clientSocket = RedstoneTCP.getInstance().getClientSocket();

//        Prepare the message in json format
        String message = "{\"channel\":\"" + sign.getLine(1) + "\",\"status\":" + isPowered + "}";

        connectionHandler.sendToClient(clientSocket, message);
    }
}
