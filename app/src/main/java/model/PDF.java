package model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PDF {

    @SerializedName("text")
    public String text;

    @SerializedName("statusCode")
    public int statusCode;
//
    @SerializedName("data")
    public dataPDF[] data;

    @SerializedName("other")
    public String other;
//
    public class dataPDF {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("webContentLink")
        public String webContentLink;
    }
}
