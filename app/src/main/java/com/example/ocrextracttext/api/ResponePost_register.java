package com.example.ocrextracttext.api;
import com.google.gson.annotations.SerializedName;

public class ResponePost_register {
    @SerializedName("text")
    public String text;
    @SerializedName("statusCode")
    public String statusCode;
    @SerializedName("other")
    public String other;
    @SerializedName("data")
    public String data;

}
