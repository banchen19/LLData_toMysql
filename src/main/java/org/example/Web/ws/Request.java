package org.example.Web.ws;

public class Request {
    String name;
    String key;
    String type;
    String data;
    String receiver;
    public Request(String name, String key, String type, String data, String receiver) {
        this.name = name;
        this.key = key;
        this.type = type;
        this.data = data;
        this.receiver = receiver;
    }


}
