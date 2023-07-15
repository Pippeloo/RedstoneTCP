package org.pippeloo.redstonetcp.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.pippeloo.redstonetcp.RedstoneTCP;

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

        int x = event.getBlock().getX();
        int y = event.getBlock().getY();
        int z = event.getBlock().getZ();

        RedstoneTCP.getInstance().getLogger().info("Sign placed at " + x + ", " + y + ", " + z);
    }
}
