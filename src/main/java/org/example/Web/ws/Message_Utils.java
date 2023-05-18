package org.example.Web.ws;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.Web.sql.Plmoney_SQL_Menage;
import org.example.Web.sql.Plmoney_SQL_Util;
import org.example.Web.ws.u.Chat;
import org.example.Web.ws.u.Player;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import static org.example.config.Config_Json.filePath;
import static org.example.config.Config_Json.key_is_key;

public class Message_Utils {
    private static Player player;

    //接收json数据
    public static void Message(WebSocket webSocket, String ws_message) throws IOException, SQLException {
        //验证这个ws与账号和密钥是否已经被绑定
        System.out.println("接收消息来自：" + webSocket.getRemoteSocketAddress().toString());
        Request request = json_Request(ws_message);
        System.out.println("请求类型："+request.type);
        if (Webrtc_Server_Management.getInstance().getGenericDatabase().findById(request.name) == null) {
            Webrtc_Server_Management.getInstance().getGenericDatabase().save(request.name, webSocket);
        }
        if (Webrtc_Server_Management.getInstance().getGenericDatabase().findById(request.name) == webSocket) {
            boolean serverData = key_is_key(filePath, request.name, request.key);
            if (serverData) {
                String sql_data = request.data;
                if (request.receiver != null && request.receiver.trim() != "") {
                    re_chat_send(webSocket, request);
                }
                switch (request.type) {
                    case "chat":
                        chat_add(sql_data,request.receiver);
                        break;
                    case "Select":
                        player = js_arr_toPlayer(sql_data);
                        String xuid = player.xuid; // 用于查询的xuid值
                        String selectSql = "SELECT * FROM " + Plmoney_SQL_Menage.mysql_tb_name + " WHERE xuid = ?";
                        try (PreparedStatement statement = Plmoney_SQL_Util.getConnection().prepareStatement(selectSql)) {
                            statement.setString(1, xuid);
                            ResultSet rs = statement.executeQuery();
                            send(webSocket, rs, rs.next()); // 执行你的 send 方法，传入结果集 rs
                        } catch (SQLException e) {
                            e.printStackTrace();
                            // 处理查询操作的异常
                        }
                        break;
                    case "Update":
                        player = js_arr_toPlayer_Updata(sql_data);
                        String updateSql = "UPDATE " + Plmoney_SQL_Menage.mysql_tb_name + " SET status = ?,pl_json_str = ? WHERE xuid = ?";
                        try (PreparedStatement statement = Plmoney_SQL_Util.getConnection().prepareStatement(updateSql)) {
                            // 设置更新语句的参数值
                            statement.setString(1, player.status);
                            statement.setString(2, player.pl_json_str);
                            //条件
                            statement.setString(3, player.xuid);
                            statement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            // 处理更新操作的异常
                        }
                        break;
                    case "Delete":
                        System.out.println("尚未开发删除语句");
                        break;
                }
            }
        } else {
            webSocket.close();
        }
    }

    private static void re_chat_send(WebSocket webSocket, Request request) throws SQLException {
        for (Chat mysql_chat : queryTable()) {
            String mysql_sender = mysql_chat.sender;
            String mysql_receiver = mysql_chat.receiver;
            String mysql_message = mysql_chat.message;
            String mysql_timestamp = mysql_chat.timestamp;
            // 移除方括号并去除额外空格
            String cleanedString = mysql_receiver.replace("[", "").replace("]", "").replaceAll("\\s+", "");
            String[] array = cleanedString.split(",");
            List<String> arrayList = new ArrayList<>(Arrays.asList(array));
            boolean containsElement = arrayList.stream().anyMatch(s -> s.equals(request.receiver));
            if (!containsElement) {
                arrayList.add(request.receiver);
                updata_chat(arrayList.toString(), mysql_timestamp);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("sender", mysql_sender);
                jsonObject.addProperty("message", mysql_message);
                webSocket.send(re_json(jsonObject.toString(), true, "chat"));
            }
        }
    }



    //添加数据
    private static void updata_chat(String receiver, String timestamp) {
        String updateSql = "UPDATE chat_messages SET receiver = ? WHERE timestamp = ?";
        try (PreparedStatement statement = Plmoney_SQL_Util.getConnection().prepareStatement(updateSql)) {
            // 设置更新语句的参数值
            statement.setString(1, receiver);
            statement.setString(2, timestamp);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理更新操作的异常
        }
    }

    //玩家数据添加
    private static void player_add() {
        String insertSql = "INSERT INTO " + Plmoney_SQL_Menage.mysql_tb_name + " (xuid, status, whitelist, pl_json_str) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = Plmoney_SQL_Util.getConnection().prepareStatement(insertSql)) {
            statement.setString(1, player.getXuid());
            statement.setString(2, player.getStatus());
            statement.setBoolean(3, player.isWhlitelist());
            statement.setString(4, player.getPl_json_str());
            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("请勿重复插入玩家消息数据：" + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //聊天数据插入
    private static void chat_add(String sql_data,String toreceiver) {
        Chat chat = js_arr_toChat(sql_data,toreceiver);
        String sender = chat.sender;
        String receiver = chat.receiver;
        String message = chat.message;
        String timestamp = chat.timestamp;
        String[] receiver_array = {receiver};
        List<String> receiver_array_list = new ArrayList<>(Arrays.asList(receiver_array));
        String insertSql = "INSERT INTO chat_messages (sender, receiver, message, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = Plmoney_SQL_Util.getConnection().prepareStatement(insertSql)) {
            statement.setString(1, sender);
            statement.setString(2, receiver_array_list.toString());
            statement.setString(3, message);
            statement.setString(4, timestamp);
            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("请勿重复插入消息数据：" + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static List<Chat> queryTable() throws SQLException {
        List<Chat> chatList = new ArrayList<>();
        String sql = "SELECT * FROM chat_messages";  // 查询整个表
        try (Statement statement = Plmoney_SQL_Util.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                // 获取每一行的数据
                String sender = resultSet.getString("sender");
                String receiver = resultSet.getString("receiver");
                String message = resultSet.getString("message");
                String timestamp = resultSet.getString("timestamp");
                Chat chat = new Chat(sender, receiver, message, timestamp);
                chatList.add(chat);
            }
            return chatList;
        }

    }


    /**
     * 接收服务端的消息
     *
     * @param message 文本
     * @return Ser对象
     */
    static Request json_Request(String message) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(message).getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String key = jsonObject.get("key").getAsString();
        String type = jsonObject.get("type").getAsString();
        String data = jsonObject.get("data").getAsString();
        String receiver = jsonObject.get("receiver").getAsString();
        Request request = new Request(name, key, type, data, receiver);
        return request;
    }

    /**
     * @param value 解析玩家
     * @return 数据集
     */
    static Player js_arr_toPlayer(String value) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(value).getAsJsonObject();
        String xuid = jsonObject.get("xuid").getAsString();
        String status = jsonObject.get("status").getAsString();
        boolean whlite_list = jsonObject.get("whitelist").getAsBoolean();
        String pl_json_str = jsonObject.get("pl_json_str").getAsString();
        return new Player(xuid, status, whlite_list, pl_json_str);
    }
    /**
     * @param value 解析玩家
     * @return 数据集
     */
    static Player js_arr_toPlayer_Updata(String value) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(value).getAsJsonObject();
        String xuid = jsonObject.get("xuid").getAsString();
        String status = jsonObject.get("status").getAsString();
        String pl_json_str = jsonObject.get("pl_json_str").getAsString();
        return new Player(xuid, status, true, pl_json_str);
    }
    /**
     * @param value 解析玩家
     * @return 数据集
     */
    static Chat js_arr_toChat(String value,String receiver) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(value).getAsJsonObject();
        String sender = jsonObject.get("sender").getAsString();
        String message = jsonObject.get("message").getAsString();
        String timestamp = jsonObject.get("timestamp").getAsString();
        return new Chat(sender, receiver, message, timestamp);
    }

    // 发送消息
    static void send(WebSocket webSocket, ResultSet resultSet, boolean next) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();

        while (next) {
            Map<String, Object> rowMap = new HashMap<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                Object value = resultSet.getObject(columnName);
                rowMap.put(columnName, value);
            }
            resultList.add(rowMap);
            next = resultSet.next(); // 移动结果集指针
        }
        if (!resultList.isEmpty()) {
            Map<String, Object> firstRow = resultList.get(0);
            String xuid = (String) firstRow.get("xuid");
            String status = (String) firstRow.get("status");
            boolean whitelist =(boolean) firstRow.get("whitelist");
            String data_text = (String) firstRow.get("pl_json_str");

            JsonObject serverObject = new JsonObject();
            serverObject.addProperty("xuid", xuid);
            serverObject.addProperty("status", status);
            serverObject.addProperty("whitelist", whitelist);
            serverObject.addProperty("pl_json_str", data_text);
            webSocket.send(re_json(serverObject.toString(), true, "updata"));
        } else {
            player_add();
        }
        resultSet.close();
    }

    static String re_json(String text, boolean tf, String type) {
        JsonObject serverObject = new JsonObject();
        serverObject.addProperty("text", text);
        serverObject.addProperty("tf", tf);
        serverObject.addProperty("type", type);
        return serverObject.toString();
    }
}
