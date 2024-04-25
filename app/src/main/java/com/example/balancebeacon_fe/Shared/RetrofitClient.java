package com.example.balancebeacon_fe.Shared;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * class to generate API connections in the backend
 */
public class RetrofitClient {

    public static Retrofit getRetrofitInstance() {
//         String BASE_URL = "http://172.20.10.9:8080";
        String BASE_URL = "http://10.0.2.2:8080";
        System.out.println("establishing the backend connection: " + BASE_URL);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
