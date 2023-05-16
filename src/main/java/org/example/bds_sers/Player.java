package org.example.bds_sers;


public class Player {

    String xuid;
    String serverName;
    boolean whitelist;
    String nbtData;

    public Player(String xuid, String serverName, boolean pos, String nbtData) {
        this.xuid = xuid;
        this.serverName = serverName;
        this.whitelist = pos;
        this.nbtData = nbtData;
    }

    public boolean getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(boolean whitelist) {
        this.whitelist = whitelist;
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
