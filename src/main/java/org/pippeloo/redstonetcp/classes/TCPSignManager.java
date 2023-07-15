package org.pippeloo.redstonetcp.classes;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.pippeloo.redstonetcp.RedstoneTCP;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TCPSignManager {
//    private static FileConfiguration config;
//
//    public TCPSignManager() {
//        File file = new File(RedstoneTCP.getInstance().getDataFolder(), getStorageFileName());
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//                RedstoneTCP.getInstance().getLogger().severe("Could not create" + getStorageFileName());
//            }
//        }
//        config = YamlConfiguration.loadConfiguration(file);
//    }
//
//    public void saveTCPSign(TCPSign sign) {
//        List<TCPSign> signs = loadTCPSigns();
//        signs.add(sign);
//        config.set("signs", signs);
//        saveConfig();
//    }
//
//    public List<TCPSign> loadTCPSigns() {
//        List<TCPSign> signs = new ArrayList<>();
//        if (config.contains("signs")) {
//            List<?> signList = config.getList("signs");
//            for (Object obj : signList) {
//                if (obj instanceof TCPSign) {
//                    signs.add((TCPSign) obj);
//                }
//            }
//        }
//        return signs;
//    }
//
//    public void saveConfig() {
//        try {
//            config.save(new File(RedstoneTCP.getInstance().getDataFolder(), getStorageFileName()));
//        } catch (IOException e) {
//            e.printStackTrace();
//            RedstoneTCP.getInstance().getLogger().severe("Could not save " + getStorageFileName());
//        }
//    }
//
//    private String getStorageFileName() {
//        return RedstoneTCP.getPluginConfig().getString("storage.file");
//    }
}
