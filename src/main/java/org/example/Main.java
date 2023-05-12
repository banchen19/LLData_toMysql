package org.example;

import org.example.ini.YAMLWriter;
import org.example.sql.Mysql_DBUtil;
import org.example.sql.Mysql_Management;

import java.io.File;
import java.sql.Connection;

import static org.example.ini.File_Json.creat_file_json;
import static org.example.ini.File_Json.filePath;
import static org.example.ini.YAMLWriter.configFile;
import static org.example.ini.YAMLWriter.readConfigFile;

public class Main {
    static File file = new File(filePath);
    static File configFile_yml=new File(configFile);
    public static void main(String[] args) {
        if (file.exists()&&configFile_yml.exists()) {
            ws_mysql();
        }else {
            creat_file_json();
            YAMLWriter.creat_file_yaml(configFile);
            ws_mysql();
        }
    }

      static void ws_mysql() {
        int port=YAMLWriter.readConfigFile_port(configFile_yml);
            if (port!=0) {
              WebSocketUtils_Server.start_Server(port);
                System.out.println("检测MySQL数据库表");

                readConfigFile(configFile_yml);//同时完成启动连接数据库

                Mysql_Management mysqlManagement=Mysql_Management.getInstance();
                if(!mysqlManagement.getMysqlDbUtil().isTableExists(Mysql_Management.getConnection(), "bc_bds_players_data"))
                {
                    System.out.println("数据库表不存在");
                    Mysql_Management.getInstance().getMysqlDbUtil().creatTables();
                    System.out.println("表初始化完成");
                }
            } else {
                System.out.println("ws服务端开启失败");
            }
    }
}