// LiteLoader-AIDS automatic generated
/// <reference path="e:\bds_api/dts/helperlib/src/index.d.ts"/> 

const wsc = new WSClient();
var pl = null;
var tb_name;
var tb_key;
var ws_uri;
var all_msg;
var path = ".\\plugins\\BC\\bc_mysql\\config_data.json";
var config = ".\\plugins\\BC\\bc_mysql\\config.json";
let config_data = {
    "ws://127.0.0.1:8887": {
        name: "ser01",
        key: "1234567890"
    }
}
//唯一标识
let config_config_data = {
    "Unique_identifier": system.randomGuid()
}
var CONFIG_int = data.openConfig(path, "json", JSON.stringify(config_data));
var CONFIG = data.openConfig(config, "json", JSON.stringify(config_config_data));

function bc_log(s) {
    colorLog("green", "[同步系统]" + s);
}

//服务器启动
var WsArray = [];
mc.listen("onServerStarted", () => {
    bc_log("作者qq:738270846,邮箱: banchen8964@gmail.com");
    colorLog("red", "[同步系统]警告：本插件不支持热加载，请正确使用关服重启");
    var fileContent = File.readFrom(path);
    var wsData = JSON.parse(fileContent);
    for (var ws_uri in wsData) {
        var ws_data = wsData[ws_uri];
        var wss = {
            ws: ws_uri,
            name: ws_data.name,
            key: ws_data.key
        };
        WsArray.push(wss);
    }
    start_ser()

});


//连接请求
function connectWebSocket(ser_uri) {
    if (wsc.connect(ser_uri)) {
        colorLog("green", "[同步系统] 连接成功 " + ser_uri);
        return true;
    } else {
        colorLog("green", "[同步系统] 连接失败:  " + ser_uri);
        return false;
    }
}

//启动连接器
function start_ser() {
    var connected = false;
    while (!connected) {
        for (var i = 0; i < WsArray.length; i++) {
            if (connectWebSocket(WsArray[i].ws)) {
                //连接成功
                ws_uri = WsArray[i].ws;
                tb_name = WsArray[i].name;
                tb_key = WsArray[i].key;
                connected = true;
                break;
            } else {
                connected = false;
            }
        }
    }
}

//发生报错
wsc.listen("onError", (msg) => {
    log("发生错误: " + msg);
    start_ser()
});

//连接丢失
wsc.listen("onLostConnection", (code) => {
    log("连接丢失，错误码: " + code);
    start_ser()
});
/******************************************************************初始化********************* */
/**收到消息 */
wsc.listen("onTextReceived", (msg) => {

    const js_msg = JSON.parse(msg);
    if (js_msg.tf) {
        let jsonArray_t = JSON.parse(js_msg.text);
        //同步筛选
        switch (js_msg.type) {
            case "updata":

                const play_xuid = jsonArray_t.xuid;
                var str_data = JSON.parse(jsonArray_t.pl_json_str);
                let players = mc.getPlayer(play_xuid)
                //如果玩家在线就进行更新
                if (jsonArray_t.whitelist) {
                    if (jsonArray_t.status == CONFIG.get("Unique_identifier")) {
                        pl_con_s(players, str_data.Inventory, str_data.OffHand, str_data.EnderChest, str_data.Armor, str_data.AllEffects, str_data.xp);
                        pl.refreshItems()
                    } else if (jsonArray_t.status == "null") {
                        pl_con_s(players, str_data.Inventory, str_data.OffHand, str_data.EnderChest, str_data.Armor, str_data.AllEffects, str_data.xp);
                        pl.refreshItems()
                    } else {
                        //少数情况
                        pl_con_s(players, str_data.Inventory, str_data.OffHand, str_data.EnderChest, str_data.Armor, str_data.AllEffects, str_data.xp);
                        pl.refreshItems()
                        updata(players);
                        players.kick()
                    }
                } else {
                    pl.kick("您已成为黑名单玩家，不可参与游戏");
                }
                break;
            case "chat":
                if (all_msg != jsonArray_t.message) {
                    //广播消息
                    mc.broadcast("<" + jsonArray_t.sender + "> " + jsonArray_t.message)
                }
                break;
        }
    }
});

/*********************************************************************发送消息 */
//初步查询判断插入
function inpl(play) {
    const data = {
        xuid: play.xuid,
        status: CONFIG.get("Unique_identifier"),
        whitelist: true,
        pl_json_str: pl_json(play)
    };
    sendQuery("Select", JSON.stringify(data));
    pl = play;
}

//发送设定
function sendQuery(type, sql) {
    const json = {
        name: tb_name,
        key: tb_key,
        type: type,
        data: sql,
        receiver: CONFIG.get("Unique_identifier")
    };
    wsc.send(JSON.stringify(json));
}

//消息格式化
function send_json(pl, msg) {
    const json = {
        sender: pl.realName,
        message: msg,
        timestamp: system.getTimeStr(),
    };
    return JSON.stringify(json)
}

//更新方法
function updata(play) {
    const data = {
        xuid: play.xuid,
        status: CONFIG.get("Unique_identifier"),
        pl_json_str: pl_json(play)
    };
    sendQuery("Update", JSON.stringify(data));
}

/*******************************获取信息************************************************* */
/**
 *
 * @param {玩家对象} pl
 * @returns 包含背包、副手、末影箱、盔甲栏
 * //玩家数据格式化
 */
function pl_json(pl) {
    let pl_data = {
        "Inventory": pl_getInventory(pl),
        "OffHand": pl_OffHand(pl),
        "EnderChest": pl_EnderChest(pl),
        "Armor": pl_Armor(pl),
        "AllEffects": pl_AllEffects(pl),
        "xp": pl.getTotalExperience()
    }
    return JSON.stringify(pl_data);
}
//获取背包json数据
function pl_getInventory(pl) {
    let jsonArr = [];
    let ct = pl.getInventory();
    let ct_all = ct.getAllItems();
    for (var cts in ct_all) {
        let snbt = ct_all[cts].getNbt().toSNBT();
        jsonArr.push(snbt);
    }
    let s = JSON.stringify(jsonArr)
    return s;
}
//获取副手json数据
function pl_OffHand(pl) {
    if (pl.getOffHand() != null) {
        return JSON.stringify(pl.getOffHand().getNbt().toSNBT());
    }
}
//获取末影箱json数据
function pl_EnderChest(pl) {
    let jsonStr = [];
    //容器对象转json
    let ct = pl.getEnderChest()
    let ct_all = ct.getAllItems()
    for (var cts in ct_all) {
        jsonStr[cts] = ct_all[cts].getNbt().toSNBT();
    }
    return JSON.stringify(jsonStr);
}
//获取玩家盔甲栏json数据
function pl_Armor(pl) {
    let jsonStr = [];
    //容器对象转json
    let ct = pl.getArmor()
    let ct_all = ct.getAllItems()
    for (var cts in ct_all) {
        jsonStr[cts] = ct_all[cts].getNbt().toSNBT();
    }
    return JSON.stringify(jsonStr);
}

//获取玩家药水效果大全
function pl_AllEffects(pl) {
    let pl_nbt = pl.getNbt()
    if (pl_nbt.getTag("ActiveEffects") != null) {
        return pl_nbt.getTag("ActiveEffects").toString();
    }
    return 'null';
}

/*****************************************更新器****************************** */

//玩家数据同步
function pl_con_s(pl, play_ct_Inventory, play_ct_OffHand, play_ct_EnderChest, play_ct_Armor, pl_alleff, pl_xp) {
    //背包
    pl_setct_Inventory(pl, play_ct_Inventory);
    //副手
    pl_setct_OffHand(pl, play_ct_OffHand);
    //末影箱
    pl_setct_EnderChest(pl, play_ct_EnderChest);
    //玩家物品栏
    pl_setct_Armor(pl, play_ct_Armor);
    //药水效果
    pl_setalleff(pl, pl_alleff);
    //玩家经验值
    pl_setxp(pl, pl_xp);
}

//设置玩家经验总值
function pl_setxp(pl, pl_xp) {

    pl.setTotalExperience(pl_xp)
}

//设置同步背包
function pl_setct_Inventory(pl, play_ct_Inventory) {
    let pl_inv_bks = JSON.parse(play_ct_Inventory);
    let pl_ct = pl.getInventory();
    let ct_all = pl_ct.getAllItems()
    for (var cts in pl_inv_bks) {
        let nbt = NBT.parseSNBT(pl_inv_bks[cts])
        ct_all[cts].setNbt(nbt)
    }
}

//设置同步副手
function pl_setct_OffHand(pl, play_ct_OffHand) {
    //下载的数据
    let nbt = JSON.parse(play_ct_OffHand)
    let item = mc.newItem(NBT.parseSNBT(nbt))
    let it = pl.getOffHand();
    if (!it.isNull()) {
        it.setNbt(nbt);
    }
}

//设置同步末影箱
function pl_setct_EnderChest(pl, play_ct_EnderChest) {
    let pl_inv_bks = JSON.parse(play_ct_EnderChest);
    let pl_ct = pl.getEnderChest();
    let ct_all = pl_ct.getAllItems()
    for (var cts in ct_all) {
        let nbt = NBT.parseSNBT(pl_inv_bks[cts])
        ct_all[cts].setNbt(nbt)
    }
}

//设置同步装备栏
function pl_setct_Armor(pl, play_ct_Armor) {
    let pl_inv_bks = JSON.parse(play_ct_Armor);
    let pl_ct = pl.getArmor();
    let ct_all = pl_ct.getAllItems()
    for (var cts in ct_all) {
        let nbt = NBT.parseSNBT(pl_inv_bks[cts])
        ct_all[cts].setNbt(nbt)
    }
}

//设置玩家药水效果同步
function pl_setalleff(pl, pl_alleff) {
    //清除全部药水效果
    let eff_str = pl_alleff.toString()
    if (eff_str != "null") {
        mc.runcmdEx('effect "' + pl.realName + '" clear')
        var dataArray = JSON.parse(eff_str);
        // 遍历数组中的每个对象
        for (var i in dataArray) {
            var obj = dataArray[i];
            var tick = obj.Duration;
            var id = obj.Id;
            var showParticles = obj.ShowParticles;
            var level = obj.Amplifier;
            var showParticlesValue = !showParticles;
            pl.addEffect(id, tick, level, showParticlesValue)
        }

    }
}

/*********************************llbds内监听器*********************** */
//玩家发送消息
mc.listen("onChat", (pl, msg) => {
    sendQuery("chat", send_json(pl, msg))
})
//玩家加入游戏
mc.listen("onJoin", (play) => {
    inpl(play)
});
//玩家退出游戏
mc.listen("onLeft", function (play) {
    try {
        const data = {
            xuid: play.xuid,
            status: "null",
            pl_json_str: pl_json(play)
        };
        sendQuery("Update", JSON.stringify(data));
    } catch {

    }
})
//玩家物品栏变化
mc.listen("onInventoryChange", (player) => updata(player))
//玩家丢东西
mc.listen("onDropItem", (player) => updata(player))
//玩家死亡
mc.listen("onPlayerDie", (player) => updata(player))
//效果获得药水效果
mc.listen("onEffectAdded", (pl) => {
    updata(pl)
})