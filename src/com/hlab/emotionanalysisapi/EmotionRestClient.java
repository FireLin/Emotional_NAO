package com.hlab.emotionanalysisapi;


import com.hlab.emotionanalysisapi.models.FaceAnalysis;
import com.hlab.emotionanalysisapi.util.FileUtils;

import java.io.IOException;
import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by David Pacioianu on 1/12/16.
 */
public class EmotionRestClient {

    private static final String API_BASE_URL = "https://api.projectoxford.ai/emotion/v1.0/";
    private ApiService apiService = null;
    private String subscriptionKey;

    private static EmotionRestClient client = null;

    public static EmotionRestClient getInstance() {
        return client;
    }

    public synchronized static void init(String subscriptionKey){
        if( client == null ){
            client = new EmotionRestClient(subscriptionKey);
        }
    }

    private EmotionRestClient(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Service setup
        apiService = retrofit.create(ApiService.class);
    }

   
    public Response<FaceAnalysis[]> detect(String uri) throws IOException, URISyntaxException {
        // convert the image to bytes array
        byte[] data = FileUtils.toBinary(uri);

        return detect(data);
    }

    public void detect(String uri, final ResponseCallback callback){
        // convert the image to bytes array
        byte[] data;

        try {
            data = FileUtils.toBinary(uri);
        } catch (Exception e) {
            callback.onError(e.getMessage());
            return;
        }

        detect(data,callback);
    }


    public void detect(byte[] data, final ResponseCallback callback){
    

        getOctetStreamCall(data).enqueue(new Callback<FaceAnalysis[]>() {
            @Override
            public void onResponse(Response<FaceAnalysis[]> response) {
                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    FaceAnalysis[] result = response.body();
                    if (result != null) {
                        callback.onSuccess(result);
                    } else {
                        callback.onError(response.message());
                    }
                } else {
                    //request not successful (like 400,401,403 etc)
                    try {
                        callback.onError(response.errorBody().string());
                    } catch (IOException e) {
                        callback.onError(response.message());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public Response<FaceAnalysis[]> detect(byte[] data) throws IOException {
        return getOctetStreamCall(data).execute();
    }
    
    private Call<FaceAnalysis[]> getOctetStreamCall(byte[] data){
        RequestBody requestBody = RequestBody
        		.create(MediaType.parse("application/octet-stream"), data);

        Call<FaceAnalysis[]> call = apiService.analyzePicture(subscriptionKey, requestBody);

        return call;
    }

}
