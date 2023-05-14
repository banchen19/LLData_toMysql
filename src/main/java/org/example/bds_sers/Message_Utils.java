package org.example.bds_sers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.Webrtc_Server;
import org.example.Webrtc_Server_Management;
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
    static Player player;
    //接收json数据

    public static void Message(WebSocket webSocket,String message) throws IOException, SQLException {

        System.out.println("接收消息来自：" + webSocket.getRemoteSocketAddress().toString());
        Client client = message_to_jsonObject(message);
        Ser serverData = read_file_json(filePath, client.server_name);
        if (serverData.server_licensename.equals(client.server_name)&&serverData.server_licenseKey.equals(client.server_key)) {
        }
        if (serverData.server_licensename.equals(client.server_name) &&
                serverData.server_licenseKey.equals(client.server_key)) {
            String sqltext = client.sql;
            switch (client.type) {
                case "Insert":
                    player = js_arr_toPlayer(sqltext);
                    String insertSql = "INSERT INTO bc_bds_players_data (xuid, server_name, pos, nbt_data) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement statement = Mysql_Management.getConnection().prepareStatement(insertSql)) {
                        statement.setString(1, player.getXuid());
                        statement.setString(2, player.getServerName());
                        statement.setString(3, player.getPos());
                        statement.setString(4, player.getNbtData());
                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("插入成功");
                        } else {
                            System.out.println("插入失败");
                        }
                    } catch (SQLIntegrityConstraintViolationException e) {
                        // 处理唯一性约束冲突的情况
                        webSocket.send(re_json(null, true));
                        // 或者执行其他处理逻辑
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // 处理插入操作的异常
                    }
                    break;
                case "Update":
                    // SQL插入语句
                    player = js_arr_toPlayer(sqltext);
                    String updateSql = "UPDATE bc_bds_players_data SET server_name = ?, pos = ?, nbt_data = ? WHERE xuid = ?";

                    try (PreparedStatement statement = Mysql_Management.getConnection().prepareStatement(updateSql)) {
                        // 设置更新语句的参数值
                        statement.setString(1, player.getServerName());
                        statement.setString(2, player.getPos());
                        statement.setString(3, player.getNbtData());
                        statement.setString(4, player.getXuid());

                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("更新成功");
                        } else {
                            System.out.println("更新失败");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // 处理更新操作的异常
                    }
                    break;
                case "Delete":
                    // SQL删除语句
                    if (Mysql_Management.getInstance().getMysqlDbUtil().executeUpdate(sqltext) != 0) {
                        System.out.println("已执行来自客户端：" + webSocket.getRemoteSocketAddress());
                    } else {
                    }
                    break;
                case "Select":
                    player = js_arr_toPlayer(sqltext);
                    String xuid = player.xuid; // 用于查询的xuid值
                    String SelectSql = "SELECT * FROM bc_bds_players_data WHERE xuid = ?";
                    try (PreparedStatement statement = Mysql_Management.getConnection().prepareStatement(SelectSql)) {
                        statement.setString(1, xuid);
                        ResultSet rs = statement.executeQuery();
                        if (rs.next()) {
                            // 数据库返回了结果集
                            send(webSocket,rs); // 执行你的send方法，传入结果集rs
                        } else {
                            System.out.println("没有找到匹配的记录");
                            send(webSocket,rs); // 执行你的send方法，传入结果集rs
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // 处理查询操作的异常
                    }
                    break;
            }
        }
        //许可密钥验证
    }

    /**
     * @param value 解析玩家
     * @return 数据集
     */
    static Player js_arr_toPlayer(String value) {

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(value).getAsJsonObject();
        String xuid = jsonObject.get("xuid").getAsString();
        String server_name = jsonObject.get("server_name").getAsString();
        String pos = jsonObject.get("pos").getAsString();
        String nbt_data = jsonObject.get("nbt_data").getAsString();
        return new Player(xuid, server_name, pos, nbt_data);
    }

    /**
     * 接收服务端的消息
     *
     * @param message 文本
     * @return Ser对象
     */
    static Client message_to_jsonObject(String message) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(message).getAsJsonObject();
        String server_licensename = jsonObject.get("server_licensename").getAsString();
        String server_licenseKey = jsonObject.get("server_licenseKey").getAsString();
        String type = jsonObject.get("type").getAsString();
        String sql = jsonObject.get("sql").getAsString();

        Client client = new Client(server_licensename, server_licenseKey, type, sql);
        return client;
    }

    // 发送消息
    static void send(WebSocket webSocket, ResultSet resultSet) throws SQLException {
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
        resultSet.close();
        boolean hasNext = !resultList.isEmpty();
        if (hasNext) {
            String xuid = (String) resultList.get(0).get("xuid");
            String server_name = (String) resultList.get(0).get("server_name");
            String pos = (String) resultList.get(0).get("pos");
            String nbt_data = (String) resultList.get(0).get("nbt_data");
            JsonObject serverObject = new JsonObject();
            serverObject.addProperty("xuid", xuid);
            serverObject.addProperty("server_name", server_name);
            serverObject.addProperty("pos", pos);
            serverObject.addProperty("nbt_data", nbt_data);
            webSocket.send(re_json(serverObject.toString(), hasNext));
        } else {
            webSocket.send(re_json("无数据", hasNext));
        }
    }
    static String re_json(String text, boolean tf) {
        JsonObject serverObject = new JsonObject();
        serverObject.addProperty("text", text);
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
