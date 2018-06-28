package com.giphy.upstackpractical.presenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static  String BASE_URL = "http://api.giphy.com/v1/";
    //http://api.giphy.com/v1/gifs/search?q=funny+cat&api_key=YOUR_API_KEY
    private static Retrofit retrofit = null;



    public static Retrofit getClient() {

        if (retrofit==null) {
            retrofit = createAdapter().build();
        }
        return retrofit;
        }

    private static Retrofit.Builder createAdapter() {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
         /*OkHttpClient httpClient = new OkHttpClient();

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);
*/

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!


        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson));
    }


}