package com.kt.na_social.api;

import com.kt.na_social.api.requests.feed.CreateNewFeed;
import com.kt.na_social.api.response.BaseResponse;
import com.kt.na_social.api.response.PageableResponse;
import com.kt.na_social.model.Feed;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FeedApi {
    @GET("feeds/news-feed")
    Call<BaseResponse<PageableResponse<List<Feed>>>> getNewsFeed(@Header("Authorization") String token, @Query("page") int limit, @Query("size") int offset);

    @POST("feeds")
    Call<Feed> createNewFeed(@Header("Authorization") String token, @Body CreateNewFeed createNewFeed);
// add header for api request  using  this  : @Header("Authorization") String authHeader , example :
//    Call<List<Feed>> getNewsFeed( @Header("Authorization") String authHeader,@Query("limit") int limit, @Query("offset") int offset);
}
