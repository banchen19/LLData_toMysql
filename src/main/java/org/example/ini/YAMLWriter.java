package org.example.ini;

import org.example.Webrtc_Server_Management;
import org.example.sql.Mysql_DBUtil;
import org.example.sql.Mysql_Management;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class YAMLWriter {

    public static String configFile="config.yml";
    public static void creat_file_yaml(String configFile) {
        // 创建数据映射
        Map<String, Object> data = new HashMap<>();
        data.put("mysql", createMySQLConfig());
        data.put("myws", createMySQLConfig_ws());
        // 创建DumperOptions，用于配置输出格式
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 设置为块格式
        options.setPrettyFlow(true); // 设置为漂亮的格式（带缩进和换行）

        // 创建YAML实例并传递DumperOptions
        Yaml yaml = new Yaml(options);

        // 将数据写入YAML文件
        try (FileWriter writer = new FileWriter("config.yml")) {
            yaml.dump(data, writer);
            System.out.println("初次加载可能未生成文件，可重新启动");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> createMySQLConfig_ws() {
        Map<String, Object> config = new HashMap<>();
//        config.put("url", "jdbc:mysql://localhost:3306/mydatabase");
        config.put("port", 8887);
        return config;
    }

    private static Map<String, Object> createMySQLConfig() {
        Map<String, Object> config = new HashMap<>();
//        config.put("url", "jdbc:mysql://localhost:3306/mydatabase");
        config.put("ip", "localhost");
        config.put("port", 3306);
        config.put("dataname", "bc_mysql");
        config.put("username", "bc");
        config.put("password", "123456");
        config.put("mysql_tb_name", "bc_bds_players_data");
        return config;
    }

    public static void readConfigFile(File configFile) {
        // 创建Yaml实例
        Yaml yaml = new Yaml();

        try (FileInputStream fis = new FileInputStream(configFile)) {
            // 加载配置文件内容为Map
            Map<String, Object> data = yaml.load(fis);

            // 读取MySQL配置
            Map<String, Object> mysqlConfig = (Map<String, Object>) data.get("mysql");
            if (mysqlConfig != null) {
                String ip = (String) mysqlConfig.get("ip");
                int port = (int) mysqlConfig.get("port");
                String dataname = (String) mysqlConfig.get("dataname");
                String username = (String) mysqlConfig.get("username");
                String password = (String) mysqlConfig.get("password");

                //jdbc:mysql://localhost:3306/mydatabase
                String url = "jdbc:mysql://" + ip + ":" + port + "/" + dataname;
                Mysql_Management.setConnection(Mysql_DBUtil.connect(url, username, password));
            } else {
                System.out.println("未找到MySQL配置");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int readConfigFile_port(File configFile) {
        int port = 0;
        Yaml yaml = new Yaml();
        try (FileInputStream fisa = new FileInputStream(configFile)) {
            // 加载配置文件内容为Map
            Map<String, Object> data = yaml.load(fisa);

            Map<String, Object> mysqlConfigws = (Map<String, Object>) data.get("myws");
            if (mysqlConfigws != null) {
                port = (int) mysqlConfigws.get("port");
                Webrtc_Server_Management.setProt(port);
            } else {
                System.out.println("未找到MyWs配置");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return port;
    }

}
