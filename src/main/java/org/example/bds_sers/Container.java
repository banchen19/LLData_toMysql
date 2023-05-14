package org.example.bds_sers;

import java.util.ArrayList;
import java.util.List;

public class Container {
    private int size;
    private String nbt;

    public Container(int size, String nbt) {
        this.size = size;
        this.nbt = nbt;
    }

    public int getSize() {
        return size;
    }

    public String getNbt() {
        return nbt;
    }


    public boolean removeItem(int index, int count) {
        // Add logic to remove specified amount of item from the container at the given index
        // Return whether the removal was successful
        return true;
    }


    public boolean removeAllItems() {
        // Add logic to remove all items from the container
        // Return whether the removal was successful
        return true;
    }

    public boolean isEmpty() {
        // Add logic to check if the container is empty
        // Return whether the container is empty
        return true;
    }
    String xuid;
    String serverName;
    String pos;

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

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
