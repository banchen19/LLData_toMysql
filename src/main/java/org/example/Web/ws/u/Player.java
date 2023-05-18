package org.example.Web.ws.u;

import com.google.gson.JsonObject;

public class Player {
   public String xuid;
   public String status;
   public boolean whlitelist;
   public String pl_json_str;

    public String getXuid() {
        return xuid;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isWhlitelist() {
        return whlitelist;
    }

    public void setWhlitelist(boolean whlitelist) {
        this.whlitelist = whlitelist;
    }

    public String getPl_json_str() {
        return pl_json_str;
    }

    public void setPl_json_str(String pl_json_str) {
        this.pl_json_str = pl_json_str;
    }

    public Player(String xuid, String status, boolean whlitelist, String pl_json) {
        this.xuid = xuid;
        this.status = status;
        this.whlitelist = whlitelist;
        this.pl_json_str = pl_json;
    }
}
