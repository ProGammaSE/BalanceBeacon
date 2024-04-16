package com.example.balancebeacon_fe.Shared;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * class to generate API connections in the backend
 */
public class RetrofitClient {
    private static Retrofit retrofit;
//    private static String BASE_URL = "http://192.168.1.218:8080";
    private static String BASE_URL = "http://10.0.2.2:8080";

    public static Retrofit getRetrofitInstance() {
        System.out.println("establishing the backend connection: " + BASE_URL);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
