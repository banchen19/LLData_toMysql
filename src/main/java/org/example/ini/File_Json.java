package org.example.ini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.bds_sers.Ser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class File_Json {
    //首次创建初始化json文件
    public static String filePath = "./data.json";
    public static void creat_file_json() {
// 创建一个新的json对象，包含服务器的相关信息
        JsonObject serverObject = new JsonObject();
        serverObject.addProperty("server_licenseKey", "1234567890");
// 创建一个json对象来存储所有的服务器数据
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("ser01",serverObject);
        try (BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonObject, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param filePath    json的文件路径
     * @param server_name
     * @return 返回Ser对象
     * @throws IOException
     */
    public static Ser read_file_json(String filePath, String server_name) throws IOException {
        JsonObject jsonObject = null;
        BufferedReader br = new BufferedReader(
                new InputStreamReader(Files.newInputStream(Paths.get(filePath)), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        JsonParser parser = new JsonParser();
        jsonObject = parser.parse(sb.toString()).getAsJsonObject();
        return file_to_json(jsonObject,server_name);
    }

    /**
     *
     * @param jsonObject   传入json对象
     * @return  返回Ser对象
     */
    static Ser file_to_json(JsonObject jsonObject,String server_name_key) {
        Ser ser = null;
        for (String key : jsonObject.keySet()) {
            System.out.println(jsonObject.get(server_name_key));
            JsonObject itemObject = jsonObject.getAsJsonObject(server_name_key);
            String licenseKey = itemObject.get("server_licenseKey").getAsString();
            ser = new Ser(key, licenseKey);
        }
        return ser;
    }
}
