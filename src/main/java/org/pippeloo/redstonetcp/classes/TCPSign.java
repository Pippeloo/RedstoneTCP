package org.pippeloo.redstonetcp.classes;

import org.bukkit.configuration.serialization.SerializableAs;

import java.io.Serializable;

@SerializableAs("TCPSign")
public class TCPSign implements Serializable {
    private String channel;
    private int x;
    private int y;
    private int z;
    private boolean isReceiver;

    public TCPSign(String channel, int x, int y, int z, boolean isReceiver) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.isReceiver = isReceiver;
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public boolean isReceiver() {
        return isReceiver;
    }

    public void setReceiver(boolean receiver) {
        isReceiver = receiver;
    }

    @Override
    public String toString() {
        return "TCPSign{" +
                "channel='" + channel + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TCPSign) {
            TCPSign sign = (TCPSign) obj;
            return sign.getChannel().equals(this.getChannel()) && sign.getX() == this.getX() && sign.getY() == this.getY() && sign.getZ() == this.getZ();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getChannel().hashCode() + this.getX() + this.getY() + this.getZ();
    }

    public String serialize() {
        return this.getChannel() + ";" + this.getX() + ";" + this.getY() + ";" + this.getZ();
    }

    public static TCPSign deserialize(String serialized) {
        String[] split = serialized.split(";");
        return new TCPSign(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), false);
    }
}

