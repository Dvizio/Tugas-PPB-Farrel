package com.example.facereguser.server;


import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private UserData data;

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }
}
