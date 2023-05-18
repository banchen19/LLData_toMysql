package org.example.Web.sql;

import java.sql.Connection;

public class Plmoney_SQL_Util {
    static Plmoney_SQL_Menage plmoneySqlMenage;
    static Connection connection;
    public Plmoney_SQL_Menage getPlmoneySqlMenage() {
        return plmoneySqlMenage;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connectiona) {
        connection = connectiona;
        plmoneySqlMenage=new Plmoney_SQL_Menage();
    }

    private static Plmoney_SQL_Util instance;
    public static Plmoney_SQL_Util getInstance()
    {
        if (instance == null) {
            instance = new Plmoney_SQL_Util();
        }
        return instance;
    }

}
