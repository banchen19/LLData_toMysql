package org.example;

import org.example.bds_sers.Message_Utils;
import org.java_websocket.WebSocket;

public class Webrtc_Server_Management {
    Webrtc_Server webrtcServer;
    static int Prot;
    static WebSocket webSocket;
    static Message_Utils messageUtils;

    public Message_Utils getMessageUtils() {
        return messageUtils;
    }

    public static void setMessageUtils(Message_Utils messageUtilsa) {
        messageUtils = messageUtilsa;
    }

    public static void setProt(int prot) {
        Prot = prot;
    }

    private static Webrtc_Server_Management instance;
    Webrtc_Server_Management()
    {
        webrtcServer=new Webrtc_Server(Prot);
        setMessageUtils(new Message_Utils());
        webrtcServer.start();
        setServer(true);
    }
    public static Webrtc_Server_Management getInstance()
    {
        if (instance == null) {
            instance = new Webrtc_Server_Management();
        }
        return instance;
    }
    Boolean isServer;

    public Boolean getServer() {
        return isServer;
    }

    public void setServer(Boolean server) {
        isServer = server;
    }

}
