# LLBDS：多服玩家数据共享WS服务端

 ***

## llse插件：[bc_wsql.llse11.js](https://github.com/banchen19/LLData_toMysql/blob/master/plugin/)

## jar下载： [lldata_tomysql-1.2.jar](Download_jar%2Flldata_tomysql-1.1.jar)
***
# 版本
| 版本  | 已实现同步     |
|-----|-----------|
| 1.1 | 背包、末影箱、药水效果 |
| 1.2 | 聊天        |
***
## 已实现同步数据：

背包 **✓**

末影箱 **✓**

药水效果 **✓**

聊天 **✓**
## 未实现同步

白名单（感觉有点鸡肋，毕竟有云黑了）

Addon

（画个大饼：去中心化经济系统）
***

## 开发测试环境：

**JDK：16**

**MySQL：8.0.12** [使用小皮搭建MySQL数据库](xp.cn)

**LLBDS版本**： Version 1.19.81.01(ProtocolVersion 582) with LiteLoaderBDS 2.13.1+13d598e

***

# 如何启动？

准备一个MySQL数据库

如果是小白第一次玩MySQL数据库，那么推荐使用这个：[小皮](xp.cn)

打开一个你创建一个数据，记得版本选用跟开发测试版本一样的数据库。（MySQL8.0.12）

可以按照**config.yml**文件来，但一定要对应你建立的MySQL数据库：

## 非常可能出现的问题：数据库超时连接被数据库那边断开，改为0就行了。

如果你使用的是[小皮](xp.cn)，那么打开软件以后看到这个
![img.png](img%2Fimg.png)
点击**配置**，找到**性能配置**，把**等待超时**改为 0
***

```
mysql:
  password: '123456'
  port: 3306
  ip: localhost
  dataname: bc_mysql
  mysql_tb_name: bc_bds_players_data
  username: bc
myws:
  port: 8887
```

***

# 配置文件：

## MySQL配置：

ip：IP地址

port：端口号

password：数据库密码

dataname：数据库名

mysql_tb_name：数据库表名

username：数据库账号

## websocket服务端设置

```
myws:
  port: 8887
```

port：websocket端口

## data.json

这是一个对应llse的密钥接口的

```
{
  "ser01": {
    "server_licenseKey": "1234567890"
  },
  "ser02": {
    "server_licenseKey": "1234567890"
  },
  "ser03": {
    "server_licenseKey": "1234567890"
  }
}
```

ser01：服务端别名

server_licenseKey：密钥

***
win版本：于 lldata_tomysql-`(版本号)`.jar **一个目录文件夹中**创建一个bat后缀文件，添加如下内容

示例：

```
 @echo off
 java -jar lldata_tomysql-1.2.jar
 pause
```

其他系统：jdk16和MySQL数据库（注意看前面的：**非常可能出现的问题**），配置好环境，使用如下指令

```
 java -jar lldata_tomysql-1.2.jar
```

***

## llse插件自定义配置：

首先在data.json里面添加你想要添加服务端接口出来，一次只能连接一个的，防止有人捣乱。

配置好了以后呢，你打开llse插件。找到初始化服务端

```
const CONFIG_int = {
    server_licensename: "ser02",
    server_licenseKey: "1234567890"
};
```

把这里的server_licensename改的跟data.json里面的一样，密钥也是噢。不一样连接不上的。
## 到这里，启动与自定义自己就算是完成了。
# 原理解释

使用基于LLBDS加载器的API读取nbt，然后ws通讯，最后到MySQL去存放和读取数据。

***