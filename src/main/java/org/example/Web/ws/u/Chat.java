package org.example.Web.ws.u;

public class Chat {
    public String sender;
    public String receiver;
    public String message;
    public String timestamp;

    public Chat(String sender, String receiver, String message, String timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
    }
}
