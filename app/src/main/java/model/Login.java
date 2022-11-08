package model;

import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("name")
    private String name;
    @SerializedName("deviceID")
    private String deviceID;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public Login(String name, String deviceID, String email, String password) {
        this.name = name;
        this.deviceID = deviceID;
        this.email = email;
        this.password = password;
    }
}
