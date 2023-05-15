package org.example.sql;

import java.sql.Connection;

public class Mysql_Management {
    private static Mysql_Management instance;
    static Mysql_DBUtil mysqlDbUtil;
    static Connection connection;
    static Mysql_WS_E mysql_ws_e;

    public Mysql_WS_E getMysql_ws_e() {
        return mysql_ws_e;
    }

    public static void setMysql_ws_e(Mysql_WS_E mysql_ws_ea) {
        mysql_ws_e = mysql_ws_ea;
    }

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
