package org.example.bds_sers;

import com.google.gson.JsonObject;

import java.util.Arrays;

public class Client {
    String server_name;
    String server_key;
    String type;
    String sql;
    public Client(String server_name, String server_key, String type, String text) {
        this.server_name = server_name;
        this.server_key = server_key;
        this.type = type;
        this.sql = text;
    }
}
