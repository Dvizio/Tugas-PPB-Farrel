package com.example.facereguser.server;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class serverAPI {
    private static Retrofit retrofit = null;
    //    public final static String BASE_URL = "http://etc.if.its.ac.id/";
//    private final static String API_BASE_URL = BASE_URL+"api/v1/";
    //public final static String BASE_URL = "http://192.168.0.110/";
    public final static String BASE_URL = "https://13b2-103-94-190-18.ngrok-free.app";

    public static Retrofit builder() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }
}
