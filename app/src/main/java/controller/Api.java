package controller;
import java.util.List;

import model.Login;
import model.PDF;
import model.Register;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface Api {

    @POST("register")
    Call<Register> register(@Body Register register);

    @POST("login")
    Call<Login> login(@Body Login login);

    @GET("client/listallfile/{id}")
    Call<PDF> getFolder(@Path("id") String folderID);




}
