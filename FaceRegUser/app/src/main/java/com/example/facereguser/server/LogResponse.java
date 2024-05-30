package com.example.facereguser.server;

// LogResponse.java
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LogResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<LogEntry> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LogEntry> getData() {
        return data;
    }

    public void setData(List<LogEntry> data) {
        this.data = data;
    }
}
