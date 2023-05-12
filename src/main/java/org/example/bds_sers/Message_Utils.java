package org.example.bds_sers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.sql.Mysql_Management;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.ini.File_Json.filePath;
import static org.example.ini.File_Json.read_file_json;

public class Message_Utils {
    static ResultSet rs = null;

    //接收json数据
    public static void Message(WebSocket webSocket, String message) throws IOException, SQLException {

        /**
         * 许可密钥
         */
        Client jsonObject = message_to_jsonObkect(message);
        Ser serverData = read_file_json(filePath, jsonObject.server_name);
        if (serverData.server_licensename.equals(jsonObject.server_name)) {
            System.out.println("服务器别名验证完成");
        }
        if (serverData.server_licenseKey.equals(jsonObject.server_key)) {
            System.out.println("许可密钥验证完成");
        }
        if (serverData.server_licensename.equals(jsonObject.server_name) &&
                serverData.server_licenseKey.equals(jsonObject.server_key)) {
            System.out.println("正在处理 MySQL 语句");
            String sqltext = jsonObject.text;
            String values = jsonObject.values;
            switch (jsonObject.type) {
                case "Insert":
                    // SQL插入语句
                    System.out.println("内敛笑死"+values);
//                    try (
//                         PreparedStatement statement = Mysql_Management.getConnection().prepareStatement(sqltext)) {
//                        // 设置插入参数
//                        statement.setString(1, xuid);
//                        statement.setString(2, server_name);
//                        statement.setString(3, last_world);
//                        statement.setString(4, pos);
//                        statement.setString(5, nbt_data);
//                        // 执行插入语句
//                        int rowsInserted = Mysql_Management.getInstance().getMysqlDbUtil().executeUpdate(sqltext);
//                        if (rowsInserted > 0) {
//                            System.out.println("数据插入成功！");
//                        } else {
//                            System.out.println("数据插入失败！");
//                        }
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
                    break;
                case "Delete":
                    break;
                case "Update":
                    break;
                case "Select":
                    System.out.println(sqltext);
                    rs = Mysql_Management.getInstance().getMysqlDbUtil().executeQuery(sqltext);
                    send(webSocket, rs, sqltext);
                    break;
            }
        }
        //许可密钥验证
    }

    /**
     * 接收服务端的消息
     *
     * @param message 文本
     * @return Ser对象
     */
    static Client message_to_jsonObkect(String message) {
        //发送数据标准：
/*
        JsonObject serverObject = new JsonObject();
        serverObject.addProperty("server_licenseKey", "远程密钥");
        serverObject.addProperty("server_licensename", "远程别名");
        serverObject.addProperty("type", "类型");
        serverObject.addProperty("text", "远程别名");

 */
// 创建一个json对象来存储所有的服务器数据


        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject1 = jsonParser.parse(message).getAsJsonObject();

        String server_licenseKey = jsonObject1.get("server_licenseKey").getAsString();
        String server_licensename = jsonObject1.get("server_licensename").getAsString();
        String type = jsonObject1.get("type").getAsString();
        String sql = jsonObject1.get("sql").getAsString();
        String values=jsonObject1.get("values").getAsString();

        Client client = new Client(server_licensename, server_licenseKey, type, sql,values);
        return client;
    }
    //发送消息
    static void send(WebSocket webSocket, ResultSet resultSet, String s) throws SQLException {
        System.out.println(resultSet);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        // 假设resultSet是已经执行了查询操作的ResultSet对象
        List<Map<String, Object>> resultList = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> rowMap = new HashMap<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                Object value = resultSet.getObject(columnName);
                rowMap.put(columnName, value);
            }
            resultList.add(rowMap);
        }
        webSocket.send(re_json(resultList.toString(), resultSet.next()));
    }

    static String re_json(String type_text, boolean tf) {
        JsonObject serverObject = new JsonObject();
        serverObject.addProperty("typetext", type_text);
        serverObject.addProperty("tf", tf);
        return serverObject.toString();
    }
    //解析json
/*
  json数据Player：
  xuid
  uuid
  所在服务器密钥
  当前游戏模式
  所在服务器位置信息（世界维度、腿部坐标）
  末影箱
  背包
  经验值
 */
}
