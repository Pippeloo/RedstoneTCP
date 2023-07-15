package org.pippeloo.redstonetcp.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.pippeloo.redstonetcp.RedstoneTCP;
import org.pippeloo.redstonetcp.classes.TCPSign;
import org.pippeloo.redstonetcp.classes.TCPSignManager;
import org.pippeloo.redstonetcp.storage.TCPSignStorage;

public class SignChangeListener implements Listener {

    TCPSignStorage signStorage = new TCPSignStorage();

    @EventHandler
    public void onSignChange(SignChangeEvent event) {

        if (!event.getEventName().equals("SignChangeEvent")) {
            return;
        }

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

        TCPSign sign = new TCPSign(channel, x, y, z, isReceiver);

        signStorage.addTCPSign(sign);

        RedstoneTCP.getInstance().getLogger().info("Sign placed at " + x + ", " + y + ", " + z + " with channel " + channel);
    }

    @EventHandler
    public void onSignBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!(block.getState() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) block.getState();

        RedstoneTCP.getInstance().getLogger().info("Sign broken");
        // Retrieve the configuration file
        FileConfiguration config = RedstoneTCP.getPluginConfig();

        // Retrieve the values from the configuration file
        String tcpReceiver = config.getString("sign-text.tcp-receiver");
        String tcpTransmitter = config.getString("sign-text.tcp-transmitter");


        if (!sign.getLine(0).equals(tcpTransmitter) && !sign.getLine(0).equals(tcpReceiver)) {
            return;
        }

        if (sign.getLine(1).isEmpty()) {
            return;
        }

        int x = event.getBlock().getX();
        int y = event.getBlock().getY();
        int z = event.getBlock().getZ();
        boolean isReceiver = sign.getLine(0).equals(tcpReceiver);
        String channel = sign.getLine(1);

        TCPSign tcpSign = new TCPSign(channel, x, y, z, isReceiver);

        signStorage.removeTCPSign(tcpSign);


        RedstoneTCP.getInstance().getLogger().info("Sign removed at " + x + ", " + y + ", " + z + " with channel " + channel);
    }
}
