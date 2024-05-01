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
    @POST("/sendImage")
    Call<ResponseApi> kirim(@Body ResponseApi responseApi);

    @GET("/image")
    Call<ArrayList<ResponseApi>> getUsers();
    @GET("/image/{Nrp}")
    Call<ArrayList<ResponseApi>> getUser(@Path("Nrp") String nrp);
    @DELETE("/image/{subjectId}")
    Call<ArrayList<ResponseApi>> deleteItem(@Path("subjectId") String subjectId);





//    @FormUrlEncoded
//    @Headers("Content-Type: application/json")
//    @POST("/sendImage")
//    Call<ResponseApi> kirim(@Field("idUser") String id,
//                            @Field("status") String status,
//                            @Field("password") String pass,
//                            @Field("image") String image);
}