package com.example.facereguser.server;

// LogEntry.java
import com.google.gson.annotations.SerializedName;

public class LogEntry {
    @SerializedName("userNrp")
    private String userNrp;

    @SerializedName("id")
    private int id;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    public String getUserNrp() {
        return userNrp;
    }

    public void setUserNrp(String userNrp) {
        this.userNrp = userNrp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
