package com.example.ocrextracttext;

public class Post_login {
    private String email;
    private String deviceID;
    private String data;

    private String password;

    public Post_login(String email, String deviceID, String data, String password) {
        this.email = email;
        this.deviceID = deviceID;
        this.data = data;
        this.password = password;
    }

    public Post_login(String email, String deviceID, String password) {
        this.email = email;
        this.deviceID = deviceID;
        this.password = password;
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Post{" +
                ", email='" + email + '\'' +
                ", deviceID='" + deviceID + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

