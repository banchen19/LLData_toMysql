package org.example.sql;

import org.example.Webrtc_Server;
import org.example.Webrtc_Server_Management;

import java.sql.Connection;

public class Mysql_Management {
    private static Mysql_Management instance;
    static Mysql_DBUtil mysqlDbUtil;
    static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connectiona) {
        connection = connectiona;
        mysqlDbUtil=new Mysql_DBUtil();
    }

    public Mysql_DBUtil getMysqlDbUtil() {
        return mysqlDbUtil;
    }

    public static Mysql_Management getInstance()
    {
        if (instance == null) {
            instance = new Mysql_Management();
        }
        return instance;
    }
}
