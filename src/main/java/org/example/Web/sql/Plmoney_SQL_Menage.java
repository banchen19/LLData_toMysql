package org.example.Web.sql;

import java.sql.*;

public class Plmoney_SQL_Menage {

    static Connection conn;
    public static String mysql_tb_name;

    //玩家数据表创建语句
    public void creatTables() {
        String createTableSql = "CREATE TABLE "+mysql_tb_name+" ("
                + "xuid VARCHAR(255) PRIMARY KEY,"
                + "status VARCHAR(255),"
                + "whitelist BOOLEAN,"
                + "pl_json_str JSON"
                + ")";
        int result = executeUpdate(createTableSql);
        if (result == 0) {
            System.out.println("表创建成功");
        } else {
            System.out.println("表创建失败");
        }
    }
//玩家聊天记录表创建语句
public void creatTables_message() {
    String createTableSql = "CREATE TABLE chat_messages (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "sender VARCHAR(50) NOT NULL," +
            "receiver TEXT NOT NULL," +
            "message TEXT NOT NULL," +
            "timestamp DATETIME NOT NULL" +
            ")";
    int result = executeUpdate(createTableSql);
    if (result == 0) {
        System.out.println("表创建成功");
    } else {
        System.out.println("表创建失败");
    }
}
    public static Connection connect(String ip,int port,String dataname,String mysql_tb_namea, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + dataname;
            mysql_tb_name=mysql_tb_namea;
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("已连接到MySQL数据库");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("MySQL数据库连接失败，请检查MySQL数据库是否已经开启，或检查数据库配置");
            e.printStackTrace();
        }
        return conn;
    }
    // 执行操作
    public int executeUpdate(String sql) {
        int result = 0;
        try {
            Statement stmt = conn.createStatement();
            System.out.println("执行操作");
            result = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    //检测表是否存在
    public boolean isTableExists(Connection connection, String tableName) {
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

}
