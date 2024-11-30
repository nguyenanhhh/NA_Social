package com.kt.na_social.ultis;

import android.util.Log;

import com.kt.na_social.viewmodel.AuthStore;

public class ApiUtils {
    public static String getApiToken() {
        Log.d("token ::: ", AuthStore.getInstance().getLoggedUser().getToken());
        return String.format("Bearer %s", AuthStore.getInstance().getLoggedUser().getToken());
    }

    public static String mediaUri(long Id) {
        return RetrofitApi.API_BASE_URL + "files/" + Id;
    }
}
