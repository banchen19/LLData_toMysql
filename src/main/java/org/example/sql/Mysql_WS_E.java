package org.example.sql;

public class Mysql_WS_E {
   public static String ip;
   public static int port;
   public static String dataname;
   public static String username;
   public static String password;

    public Mysql_WS_E(String ip, int port, String dataname, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.dataname = dataname;
        this.username = username;
        this.password = password;
    }
}
