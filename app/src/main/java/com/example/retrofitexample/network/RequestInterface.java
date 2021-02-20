package com.example.retrofitexample.network;



import com.example.retrofitexample.model.RetroData;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
public interface RequestInterface {
   @GET("/theme/list")
   Call<RetroData> getData(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Query("all") String all);
//http://api.rocksplayer.com/theme/list?all=1(GET)
}