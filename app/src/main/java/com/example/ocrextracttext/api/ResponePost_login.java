package com.example.ocrextracttext.api;
import com.google.gson.annotations.SerializedName;

public class ResponePost_login {
    @SerializedName("text")
    public String text;
    @SerializedName("statusCode")
    public String statusCode;
    @SerializedName("other")
    public String other;
    @SerializedName("data")
    public Data data_post;
    public class Data{
        @SerializedName("key_user")
        public String key_user;
        public String folderId;
    }
}

