package org.pippeloo.redstonetcp.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.pippeloo.redstonetcp.RedstoneTCP;
import org.pippeloo.redstonetcp.classes.TCPSign;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TCPSignStorage {
//    This class handles everything related to the storage of TCPSigns.
//    Each channel gets stored in a separate file. name of the file is the channel name. With the extension .yml
//    there will be Transmitter and Receivers in the file.
    private String channelsFolderName = "channels";

    public TCPSignStorage() {
        File channelsFolder = new File(RedstoneTCP.getInstance().getDataFolder(), channelsFolderName);
        if (!channelsFolder.exists()) {
            channelsFolder.mkdir();
        }
    }

    public void addTCPSign(TCPSign sign) {
        File channelFile = new File(RedstoneTCP.getInstance().getDataFolder() + "/" + channelsFolderName, sign.getChannel() + ".yml");
        FileConfiguration channelConfig;

        // Check if the file exists, if not create it.
        if (!channelFile.exists()) {
            try {
                channelFile.createNewFile();
                // Add default values to the file.
                channelConfig = YamlConfiguration.loadConfiguration(channelFile);
                channelConfig.set(sign.getChannel() + ".transmitters", null);
                channelConfig.set(sign.getChannel() + ".receivers", null);
                channelConfig.save(channelFile);

            } catch (IOException e) {
                e.printStackTrace();
                RedstoneTCP.getInstance().getLogger().severe("Could not create" + sign.getChannel() + ".yml");
            }
        }

        channelConfig = YamlConfiguration.loadConfiguration(channelFile);

        // Check if the sign is a transmitter or a receiver.
        if (sign.isReceiver()) {
            List<TCPSign> receivers = loadReceivers(sign.getChannel());

            // Check if the receiver is already in the list.
            if (!receivers.contains(sign)) {
                receivers.add(sign);
                channelConfig.set(sign.getChannel() + ".receivers", receivers);
                try {
                    channelConfig.save(channelFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    RedstoneTCP.getInstance().getLogger().severe("Could not save " + sign.getChannel() + ".yml");
                }
            }

        } else {
            List<TCPSign> transmitters = loadTransmitters(sign.getChannel());

            // Check if the transmitter is already in the list.
            if (!transmitters.contains(sign)) {
                transmitters.add(sign);
                channelConfig.set(sign.getChannel() + ".transmitters", transmitters);
                try {
                    channelConfig.save(channelFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    RedstoneTCP.getInstance().getLogger().severe("Could not save " + sign.getChannel() + ".yml");
                }
            }


        }
    }

    public void removeTCPSign(TCPSign sign) {
        File channelFile = new File(RedstoneTCP.getInstance().getDataFolder() + "/" +channelsFolderName, sign.getChannel() + ".yml");

        // Check if the file exists.
        if (!channelFile.exists()) {
            RedstoneTCP.getInstance().getLogger().severe("Could not find " + sign.getChannel() + ".yml");
            return;
        }

        FileConfiguration channelConfig = YamlConfiguration.loadConfiguration(channelFile);

        // Check if the sign is a transmitter or a receiver.
        if (sign.isReceiver()) {
            // Check if the receiver is in the list.
            if (loadReceivers(sign.getChannel()).contains(sign)) {
                List<TCPSign> receivers = loadReceivers(sign.getChannel());
                receivers.remove(sign);
                channelConfig.set(sign.getChannel() + ".receivers", receivers);
                try {
                    channelConfig.save(channelFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    RedstoneTCP.getInstance().getLogger().severe("Could not save " + sign.getChannel() + ".yml");
                }
            }
        } else {
            // Check if the transmitter is in the list.
            if (loadTransmitters(sign.getChannel()).contains(sign)) {
                List<TCPSign> transmitters = loadTransmitters(sign.getChannel());
                transmitters.remove(sign);
                channelConfig.set(sign.getChannel() + ".transmitters", transmitters);

                try {
                    channelConfig.save(channelFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    RedstoneTCP.getInstance().getLogger().severe("Could not save " + sign.getChannel() + ".yml");
                }
            }
        }
    }

    private List<TCPSign> loadReceivers(String channel) {
        List<TCPSign> receivers = new ArrayList<>();
        File channelFile = new File(RedstoneTCP.getInstance().getDataFolder() + channelsFolderName, channel + ".yml");
        FileConfiguration channelConfig = YamlConfiguration.loadConfiguration(channelFile);
        if (channelConfig.contains(channel + ".receivers")) {
            List<?> receiverList = channelConfig.getList(channel + ".receivers");
            for (Object obj : receiverList) {
                if (obj instanceof TCPSign) {
                    receivers.add((TCPSign) obj);
                }
            }
        }
        return receivers;
    }

    private List<TCPSign> loadTransmitters(String channel) {
        List<TCPSign> transmitters = new ArrayList<>();
        File channelFile = new File(RedstoneTCP.getInstance().getDataFolder() + channelsFolderName, channel + ".yml");
        FileConfiguration channelConfig = YamlConfiguration.loadConfiguration(channelFile);
        if (channelConfig.contains(channel + ".transmitters")) {
            List<?> transmitterList = channelConfig.getList(channel + ".transmitters");
            for (Object obj : transmitterList) {
                if (obj instanceof TCPSign) {
                    transmitters.add((TCPSign) obj);
                }
            }
        }
        return transmitters;
    }

}
