package com.example.facereguser.server;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiClientFaceReg {
    @POST("/users")
    Call<ResponseApi> kirim(@Body ResponseApi responseApi);

    @POST("/users/find/")
    Call<UserResponse> find(@Body ResponseApi responseApi);

    @POST("/users/verify/{Nrp}")
    Call<FaceVerificationResponseAPI> verify(@Path("Nrp") String nrp, @Body ResponseApi responseApi);

    @GET("/users/{Nrp}")
    Call<HeaderApiIMG> getUser(@Path("Nrp") String nrp);

    @DELETE("/users/{subjectId}")
    Call<HeaderApiIMG> deleteItem(@Path("subjectId") String subjectId);
    @PATCH("/users/{Nrp}")
    Call<HeaderApiIMG> editUser(@Path("Nrp") String nrp, @Body ResponseApi responseApi);
    @PATCH("/users/{Nrp}")
    Call<HeaderApiIMG> editUsername(@Path("Nrp") String nrp, @Body ResponseApiName responseApi);

}
