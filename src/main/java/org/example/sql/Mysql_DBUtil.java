package org.example.sql;

import java.sql.*;

public class Mysql_DBUtil {
    static Connection conn ;
    public static Connection connect(String url, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("MySQL数据库连接完成");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("MySQL数据库连接失败，请检查MySQL数据库是否已经开启");
            e.printStackTrace();
        }
        return conn;
    }


    //创建表语句
    public void creatTables() {
        String createTableSql = "CREATE TABLE bc_bds_players_data (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "xuid VARCHAR(50) NOT NULL," +
                "server_name VARCHAR(100) NOT NULL," +
                "last_world VARCHAR(100) NOT NULL," +
                "pos VARCHAR(100) NOT NULL," +
                "nbt_data TEXT" +
                ")";
        int result = executeUpdate(createTableSql);
        if (result == 0) {
            System.out.println("表创建成功");
        } else {
            System.out.println("表创建失败");
        }
    }

    //检测表是否存在
    public boolean isTableExists(Connection connection,String tableName) {
        boolean exists = false;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, null);
            if (tables.next()) {
                exists = true;
            }
            tables.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    //查询操作
    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        try {
            Statement stmt = Mysql_Management.getConnection().createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    //执行操作
    public int executeUpdate(String sql) {
        int result = 0;
        try {
            Statement stmt = Mysql_Management.getConnection().createStatement();
            result = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void close() {
        try {
            Mysql_Management.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

