package org.example;

import org.java_websocket.WebSocket;

import java.security.PublicKey;
import java.util.List;

public class WebSocketUtils_Server {
    /**
     * 建立连接
     * 发送消息
     * 接收消息
     */
    public static Boolean start_Server(int Prot) {
        Webrtc_Server_Management.setProt(Prot);
        return   Webrtc_Server_Management.getInstance().getServer();
    }
}
