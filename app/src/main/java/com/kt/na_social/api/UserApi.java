package com.kt.na_social.api;

import com.kt.na_social.api.requests.LoginRequestBody;
import com.kt.na_social.api.response.BaseResponse;
import com.kt.na_social.api.response.UserResponse;
import com.kt.na_social.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("public/auth/login")
    Call<BaseResponse<User>> login(@Body LoginRequestBody requestBody);
}
