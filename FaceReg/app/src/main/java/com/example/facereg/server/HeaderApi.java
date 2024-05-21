package com.example.facereg.server;

import java.util.List;

public class HeaderApi {
    private String servermessage;
    private List<ResponseApiUserData> data;

    public String getServermessage() {
        return servermessage;
    }

    public List<ResponseApiUserData> getData() {
        return data;
    }

    public HeaderApi(String servermessage, List<ResponseApiUserData> data) {
        this.servermessage = servermessage;
        this.data = data;
    }
}

