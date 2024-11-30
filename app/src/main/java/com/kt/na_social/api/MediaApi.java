package com.kt.na_social.api;

import com.kt.na_social.api.response.BaseResponse;
import com.kt.na_social.api.response.media.MediaResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MediaApi {

    @Multipart
    @POST("files/uploads")
    Call<BaseResponse<List<MediaResponse>>> uploadMedia(@Header("Authorization") String token, @Part List<MultipartBody.Part> files);
}
