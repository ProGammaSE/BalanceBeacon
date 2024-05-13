package com.example.balancebeacon_fe.Shared;
import java.util.ArrayList;
import java.util.Random;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * class to generate API connections in the backend
 */
public class RetrofitClient {

    // function to pick a backend using round robin theory
    public static String roundRobin() {
        Random random = new Random();
        String URL = "http://10.0.2.2:";

        ArrayList<String> randomNumberArray = new ArrayList<String>();
        randomNumberArray.add("8080");
        randomNumberArray.add("8081");

        int number = random.nextInt(2);
        String port = randomNumberArray.get(number);
        return URL + port;
    }

    public static Retrofit getRetrofitInstance() {
        System.out.println("Round Robin: " + roundRobin());
//         String BASE_URL = "http://172.20.10.9:8080";
//         String BASE_URL = "http://192.168.0.103:8080";
        String BASE_URL = "http://10.0.2.2:8080";
//        String BASE_URL = roundRobin();
        System.out.println("establishing the backend connection: " + BASE_URL);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
