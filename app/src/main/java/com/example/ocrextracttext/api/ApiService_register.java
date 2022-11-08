package com.example.ocrextracttext.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.ocrextracttext.Post_register;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService_register {
    //link: https://iot-service-1.vercel.app/login
Gson gson= new GsonBuilder()
            .setDateFormat("yyyy-MM-dd:mm:ss")
            .create();
ApiService_register apiserver= new Retrofit.Builder()
        .baseUrl("https://iot-service-1.vercel.app")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService_register.class);
@POST("/register")

//Call<post1> sendPost(@Body post1 post1);
Call<ResponePost_register> sendPost(@Body Post_register post);

}
