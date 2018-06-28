package com.giphy.upstackpractical.presenter;

import com.giphy.upstackpractical.model.GiphyBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    //http://api.giphy.com/v1/gifs/trending?api_key=zZiOoYjLuklDuCP9D4a7GIMB4CSnYFql&limit=5
    @GET("gifs/trending")
    Call<GiphyBean>getTrendingGiphyList(@Query("api_key") String apiKey,@Query("limit") String limit,@Query("offset") String offset);

    //http://api.giphy.com/v1/gifs/search?q=ryan+gosling&api_key=zZiOoYjLuklDuCP9D4a7GIMB4CSnYFql&limit=5
    @GET("gifs/search")
    Call<GiphyBean> getSearchGiphyList(@Query("q") String q, @Query("api_key") String apiKey,@Query("limit") String limit,@Query("offset") String offset);
}
