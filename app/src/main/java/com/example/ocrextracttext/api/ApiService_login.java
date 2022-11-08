package com.example.ocrextracttext.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.ocrextracttext.Post_login;
import com.example.ocrextracttext.Post_login;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService_login {
    //link: https://iot-service-1.vercel.app/login
Gson gson= new GsonBuilder()
            .setDateFormat("yyyy-MM-dd:mm:ss")
            .create();
ApiService_login apiserver= new Retrofit.Builder()
        .baseUrl("https://iot-service-1.vercel.app")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService_login.class);
@POST("/login")

//Call<post1> sendPost(@Body post1 post1);
Call<ResponePost_login> sendPost(@Body Post_login post);

}
