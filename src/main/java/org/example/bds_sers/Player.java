package org.example.bds_sers;


public class Player {

    String xuid;
    String serverName;
    String pos;
    String nbtData;

    public Player(String xuid, String serverName, String pos, String nbtData) {
        this.xuid = xuid;
        this.serverName = serverName;
        this.pos = pos;
        this.nbtData = nbtData;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getXuid() {
        return xuid;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getNbtData() {
        return nbtData;
    }

    public void setNbtData(String nbtData) {
        this.nbtData = nbtData;
    }
}
