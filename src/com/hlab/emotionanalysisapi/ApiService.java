package com.hlab.emotionanalysisapi;


import com.hlab.emotionanalysisapi.models.FaceAnalysis;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by David Pacioianu on 1/12/16.
 */
public interface ApiService {

    @Headers("Content-Type: application/octet-stream")
    @POST("recognize")
    Call<FaceAnalysis[]> analyzePicture(
            @Header("Ocp-Apim-Subscription-Key") String subscriptionKey,
            @Body RequestBody image
    );
}
