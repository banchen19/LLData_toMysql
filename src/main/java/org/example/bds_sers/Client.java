package org.example.bds_sers;

public class Client {
    String server_name;
    String server_key;
    String type;
    String text;
    String values;

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public String getServer_key() {
        return server_key;
    }

    public void setServer_key(String server_key) {
        this.server_key = server_key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Client(String server_name, String server_key, String type, String text,String values) {
        this.server_name = server_name;
        this.server_key = server_key;
        this.type = type;
        this.text = text;
        this.values = values;
    }
}
