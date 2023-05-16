// LiteLoader-AIDS automatic generated
/// <reference path="e:\bds_api/dts/helperlib/src/index.d.ts"/> 

// LiteLoader-AIDS automatic generated
/// <reference path="e:\bds_api/dts/helperlib/src/index.d.ts"/>
/*************************************顶部初始化******************************* */
const wsc = new WSClient();
var pl = null;
var all_msg;
var tb_name;
var tb_key;
var path = ".\\plugins\\BC\\bc_mysqlconfig\\config.json";
let config_data = {
    "ws://127.0.0.1:8887": {
        server_licensename: "ser02",
        server_licenseKey: "1234567890"
    }
}
var CONFIG_int = data.openConfig(path, "json", JSON.stringify(config_data));
/***********************************初始化******************************************* */
/************************************监听组********************************* */
//服务器启动
mc.listen("onServerStarted", () => {
    bc_log("作者qq:738270846,邮箱: banchen8964@gmail.com");

    start_ser()

    // log(stockInfoArray[0].stockName)// 测试语句
});

function start_ser()
{
    var fileContent = File.readFrom(path);
    var stockData = JSON.parse(fileContent);
    var stockInfoArray = []; // 存放stockName和server_licensename的数组

    for (var stockName in stockData) {
        var stockInfo = stockData[stockName];
        var stockInfoEntry = {
            stockName: stockName,
            licensename: stockInfo.server_licensename,
            key: stockInfo.server_licenseKey
        };
        stockInfoArray.push(stockInfoEntry);
    }

    // 循环判定连接是否有效
    for (var i = 0; i < stockInfoArray.length; i++) {
        if (connectWebSocket(stockInfoArray[i].stockName)) {
            //连接成功跳出循环
            tb_name=stockInfoArray[i].licensename;
            tb_key=stockInfoArray[i].key;
            break;
        }
    }
}
//玩家加入游戏
mc.listen("onJoin", (play) => {
    inpl(play)
});
//玩家退出游戏
mc.listen("onLeft", function (pl) {
    updata(pl,true)
})
//玩家物品栏变化
mc.listen("onInventoryChange", (player) => updata(player,false))
//玩家丢东西
mc.listen("onDropItem", (player) => updata(player,false))
//玩家死亡
mc.listen("onPlayerDie", (player) => updata(player,false))
//效果获得
mc.listen("onEffectAdded", (pl) => {
    updata(player,false)
})
/**********************************ws监听组******************************************** */

function handleTextReceived(msg) {
    const js_msg = JSON.parse(msg);
    if (js_msg.tf) {
        switch (js_msg.type) {
            case "updata":
                let jsonArray_t = JSON.parse(js_msg.text);
                const play_xuid = jsonArray_t.xuid;
                var str_data = JSON.parse(jsonArray_t.nbt_data);
                let players = mc.getPlayer(play_xuid)
                if(jsonArray_t.whitelist)
                {
                    pl_con_s(players, str_data.Inventory, str_data.OffHand, str_data.EnderChest, str_data.Armor, str_data.AllEffects);
                    pl.refreshItems()
                    updata(players,false)
                }else
                {
                    pl.kick("请勿重复登录游戏")
                }

                break;
            case "chat":
                if (all_msg != js_msg.text) {
                    //广播消息
                    mc.broadcast(js_msg.text)
                }
                break;
        }
    } else {
        const data = {
            xuid: pl.xuid,
            server_name: tb_name,
            whitelist: true,
            nbt_data: pl_json(pl)
        };
        sendQuery("Insert", JSON.stringify(data));
    }
}
wsc.listen("onTextReceived", handleTextReceived);

wsc.listen("onError", (msg) => {
    log("发生错误: " + msg);
    start_ser()
});

wsc.listen("onLostConnection", (code) => {
    log("连接丢失，错误码: " + code);
    start_ser()
});

/***************************************监听组分割线*********************************** */


function connectWebSocket(ser_uri) {
    if (wsc.connect(ser_uri)) {
        colorLog("green", "[同步系统]:连接成功");
        return true;
    } else {
        colorLog("green", "[同步系统]:连接失败");
        start_ser()
        return false;
    }
}
/**************************************操作以及返回更新****************************************** */
//初步查询判断插入
function inpl(play) {
    const data = {
        xuid: play.xuid,
        server_name: tb_name,
        whitelist: true,
        nbt_data: pl_json(play)
    };
    sendQuery("Select", JSON.stringify(data));
    pl = play;
}
//发送语句
function sendQuery(type, sql) {
    const json = {
        server_licensename: tb_name,
        server_licenseKey: tb_key,
        type: type,
        sql: sql,
    };
    wsc.send(JSON.stringify(json));
}
//更新方法
function updata(player,whitelist) {
    try {
        const data = {
            xuid: player.xuid,
            server_name: tb_name,
            whitelist: whitelist,
            nbt_data: pl_json(player)
        };
        sendQuery("Update", JSON.stringify(data));
    } catch {

    }
}

//玩家数据同步
function pl_con_s(pl, play_ct_Inventory, play_ct_OffHand, play_ct_EnderChest, play_ct_Armor, pl_alleff, pl_xp) {
    pl_setct_Inventory(pl, play_ct_Inventory);
    pl_setct_OffHand(pl, play_ct_OffHand);
    pl_setct_EnderChest(pl, play_ct_EnderChest);
    pl_setct_Armor(pl, play_ct_Armor);
    pl_setalleff(pl, pl_alleff);
    pl_setxp(pl, pl_xp);
}
//设置玩家基岩总值
function pl_setxp(pl, pl_xp) {
    pl_setxp(pl, pl_xp)
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
/***************************************工具************************************** */
function bc_log(s) {
    colorLog("green", "[同步系统]" + s);
}
/*******************************获取信息************************************************* */
/**
 *
 * @param {玩家对象} pl
 * @returns 包含背包、副手、末影箱、盔甲栏
 */
function pl_json(pl) {
    let pl_data = {
        "Inventory": pl_getInventory(pl),
        "OffHand": pl_OffHand(pl),
        "EnderChest": pl_EnderChest(pl),
        "Armor": pl_Armor(pl),
        "AllEffects": pl_AllEffects(pl),
        "xp": pl.getTotalExperience(),
        "bmd": 0,
        "quanxian": 0
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
mc.listen("onChat", (pl, msg) => {
    all_msg = '[' + CONFIG_int.server_licensename + '] ' + pl.realName + "> " + msg
    sendQuery("chat", all_msg);
})