package org.example;

import org.example.Web.sql.Plmoney_SQL_Menage;
import org.example.Web.sql.Plmoney_SQL_Util;
import org.example.Web.ws.Webrtc_Server_Management;
import org.example.config.Config_YAML;

import java.io.File;

import static org.example.config.Config_Json.creat_file_json;
import static org.example.config.Config_Json.filePath;
import static org.example.config.Config_YAML.*;
import static org.example.config.Config_YAML.readConfigFile;

public class Main {
    static File file = new File(filePath);
    static File configFile_yml=new File(configFile);
    public static void main(String[] args) {
        if (file.exists()&&configFile_yml.exists()) {
            //数据库连接
            conn();
        }else {
            creat_file_yaml();
            creat_file_json();
            conn();
        }
    }
    private static void conn() {
        //阅读yml启动ws服务端
        int port= Config_YAML.readConfigFile_port(configFile_yml);
        if (port!=0) {
            Webrtc_Server_Management.getInstance();
            System.out.println("检测MySQL数据库表");
            readConfigFile();//同时完成启动连接数据库
            Plmoney_SQL_Menage plmoney_sql_util=Plmoney_SQL_Util.getInstance().getPlmoneySqlMenage();
            //检查玩家数据表是否存在
            iMysqlTb_Player(plmoney_sql_util);
            //检查玩家聊天记录表是否存在
            iMysqlTb_message(plmoney_sql_util);
        } else {
            System.out.println("ws服务端开启失败");
        }
    }
//玩家数据表判定与生成
    private static void iMysqlTb_Player(Plmoney_SQL_Menage plmoney_sql_util) {
        if(!plmoney_sql_util.isTableExists(Plmoney_SQL_Util.getConnection(), plmoney_sql_util.mysql_tb_name))
        {
            System.out.println("检查玩家数据表不存在");
            Plmoney_SQL_Util.getInstance().getPlmoneySqlMenage().creatTables();
        }
    }
    //玩家聊天记录判定与生成
    private static void iMysqlTb_message(Plmoney_SQL_Menage plmoney_sql_util) {
        if(!plmoney_sql_util.isTableExists(Plmoney_SQL_Util.getConnection(), "chat_messages"))
        {
            System.out.println("检查玩家聊天记录表不存在");
            Plmoney_SQL_Util.getInstance().getPlmoneySqlMenage().creatTables_message();
        }
    }
}