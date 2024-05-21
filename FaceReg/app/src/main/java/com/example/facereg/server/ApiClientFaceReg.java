package com.example.facereg.server;

import java.util.ArrayList;



import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.DELETE;


public interface ApiClientFaceReg {
    @POST("/users")
    Call<ResponseApi> kirim(@Body ResponseApi responseApi);

    @POST("/users/find/")
    Call<HeaderApiIMG> find(@Body ResponseApi responseApi);
    @POST("/users/verify/{Nrp}")
    Call<FaceVerificationResponseAPI> verify(@Path("Nrp") String nrp, @Body ResponseApi responseApi);

    @GET("/users")
    Call<HeaderApi> getUsers();
    @GET("/users/{Nrp}")
    Call<HeaderApiIMG> getUser(@Path("Nrp") String nrp);
    @DELETE("/users/{subjectId}")
    Call<HeaderApiIMG> deleteItem(@Path("subjectId") String subjectId);

// TODO    @POST("/users/verify/{Nrp}")
//    Call<HeaderApi> verifyface(@Path("Nrp") String subjectId);





//    @FormUrlEncoded
//    @Headers("Content-Type: application/json")
//    @POST("/sendImage")
//    Call<ResponseApi> kirim(@Field("idUser") String id,
//                            @Field("status") String status,
//                            @Field("password") String pass,
//                            @Field("image") String image);
}