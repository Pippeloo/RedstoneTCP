package org.pippeloo.redstonetcp.classes;

public class TCPSignReceiver {
    private int x;
    private int y;
    private int z;
    private String channel;

    public TCPSignReceiver(int x, int y, int z, String channel) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
