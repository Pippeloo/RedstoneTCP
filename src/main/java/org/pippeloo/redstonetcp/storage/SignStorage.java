package org.pippeloo.redstonetcp.storage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.pippeloo.redstonetcp.RedstoneTCP;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SignStorage {
    private final File configFile;
    private final FileConfiguration config;

    public SignStorage() {
        configFile = new File(RedstoneTCP.getInstance().getDataFolder(), "receivers.yaml");
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void addSign(String channel, Location location) {

        String path = getLocationPath(location);
        config.set(path, channel);
        saveConfig();
    }

    public void removeSign(Location location) {
        String path = getLocationPath(location);

        // Remove the sign from the configuration file
        config.set(path, null);

        saveConfig();
    }

    public List<Location> getSignLocations() {
        List<Location> signLocations = new ArrayList<>();
        ConfigurationSection section = config.getConfigurationSection("signs");
        if (section != null) {
            Set<String> keys = section.getKeys(false);
            for (String key : keys) {
                String[] coordinates = key.split(",");
                if (coordinates.length == 3) {
                    try {
                        int x = Integer.parseInt(coordinates[0]);
                        int y = Integer.parseInt(coordinates[1]);
                        int z = Integer.parseInt(coordinates[2]);
                        Location location = new Location(Bukkit.getWorlds().get(0), x, y, z);
                        signLocations.add(location);
                    } catch (NumberFormatException ignored) {
                        // Ignore invalid coordinates
                    }
                }
            }
        }
        return signLocations;
    }

    public List<Location> getSignLocations(String channel) {
        RedstoneTCP.getInstance().getLogger().info("Getting sign locations for channel " + channel);
        List<Location> signLocations = new ArrayList<>();
        ConfigurationSection section = config.getConfigurationSection("signs");
        if (section != null) {
            Set<String> keys = section.getKeys(false);
            for (String key : keys) {
                String[] coordinates = key.split(",");
                if (coordinates.length == 3) {
                    try {
                        int x = Integer.parseInt(coordinates[0]);
                        int y = Integer.parseInt(coordinates[1]);
                        int z = Integer.parseInt(coordinates[2]);
                        Location location = new Location(Bukkit.getWorlds().get(0), x, y, z);
                        if (section.getString(key).equals(channel)) {
                            signLocations.add(location);
                        }
                    } catch (NumberFormatException ignored) {
                        // Ignore invalid coordinates
                    }
                }
            }
        }
        return signLocations;
    }

    private String getLocationPath(Location location) {
        return "signs." + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}