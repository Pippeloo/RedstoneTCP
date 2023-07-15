package org.pippeloo.redstonetcp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.pippeloo.redstonetcp.RedstoneTCP;

public class SignChangeListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getPlayer() != null) {
            int x = event.getBlock().getX();
            int y = event.getBlock().getY();
            int z = event.getBlock().getZ();

            RedstoneTCP.getInstance().getLogger().info("Sign placed at " + x + ", " + y + ", " + z);
        }
    }
}
