package com.example.facereguser.server;

public class HeaderApiIMG {
    private String servermessage;
    private ResponseApi data;

    public String getServermessage() {
        return servermessage;
    }

    public ResponseApi getDataWithImg() {
        return data;
    }

    public HeaderApiIMG(String servermessage, ResponseApi data) {
        this.servermessage = servermessage;
        this.data = data;
    }
}
