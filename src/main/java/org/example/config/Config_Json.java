package org.example.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config_Json {
    //首次创建初始化json文件
    public static String filePath = "./data.json";
    //接口data.json创建
     public static void creat_file_json() {
        JsonObject serverObject = new JsonObject();
        serverObject.addProperty("key", "1234567890");
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
     * 接口密钥验证
     */
    public static boolean key_is_key(String filePath, String name, String key) throws IOException {
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
        boolean ser;
        JsonObject itemObject = jsonObject.getAsJsonObject(name);
        String licenseKey = itemObject.get("key").getAsString();
        if (licenseKey.equals(key)) {
            System.out.println("密钥验证成功");
            ser=true;
        }else {
            System.out.println("密钥验证失败");
            ser=false;
        }
        return ser;
    }
}
