package com.example.ocrextracttext;

import android.content.SharedPreferences;

public class Preferences {
    private String id, key_user;

    public Preferences(String id, String key_user) {
        this.id = id;
        this.key_user = key_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey_user() {
        return key_user;
    }

    public void setKey_user(String key_user) {
        this.key_user = key_user;
    }

}


