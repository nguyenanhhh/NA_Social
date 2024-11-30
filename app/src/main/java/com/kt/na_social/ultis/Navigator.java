package com.kt.na_social.ultis;

import androidx.navigation.NavOptions;

import com.kt.na_social.MainActivity;

public class Navigator {
    public static void navigateTo(Integer toId, NavOptions options) {
        MainActivity.NAV_CONTROLLER.navigate(toId, null, options);
    }

    public static void goBack() {
        MainActivity.NAV_CONTROLLER.popBackStack();
    }
}
