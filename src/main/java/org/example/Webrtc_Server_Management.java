package org.example;

public class Webrtc_Server_Management {
    Webrtc_Server webrtcServer;
    static int Prot;

    public Webrtc_Server getWebrtcServer() {
        return webrtcServer;
    }

    public void setWebrtcServer(Webrtc_Server webrtcServer) {
        this.webrtcServer = webrtcServer;
    }

    public static void setProt(int prot) {
        Prot = prot;
    }

    private static Webrtc_Server_Management instance;
    Webrtc_Server_Management()
    {
        webrtcServer=new Webrtc_Server(Prot);
        webrtcServer.start();
        setWebrtcServer(webrtcServer);
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
