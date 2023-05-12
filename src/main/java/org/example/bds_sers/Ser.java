package org.example.bds_sers;

public class Ser {
    String server_licensename;
    String server_licenseKey;

    public String getServer_licensename() {
        return server_licensename;
    }

    public void setServer_licensename(String server_licensename) {
        this.server_licensename = server_licensename;
    }

    public String getServer_licenseKey() {
        return server_licenseKey;
    }

    public void setServer_licenseKey(String server_licenseKey) {
        this.server_licenseKey = server_licenseKey;
    }

    public Ser(String server_licensename, String server_licenseKey) {
        this.server_licensename = server_licensename;
        this.server_licenseKey = server_licenseKey;
    }
}
