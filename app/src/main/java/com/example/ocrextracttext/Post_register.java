package com.example.ocrextracttext;

public class Post_register {
    private  String deviceID, name,email,password;

    public Post_register(String deviceId, String name, String email, String password) {
        this.deviceID = deviceId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceId) {
        this.deviceID = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
