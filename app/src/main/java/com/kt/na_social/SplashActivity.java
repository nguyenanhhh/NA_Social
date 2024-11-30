package com.kt.na_social;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kt.na_social.api.UserApi;
import com.kt.na_social.api.requests.LoginRequestBody;
import com.kt.na_social.api.response.BaseResponse;
import com.kt.na_social.enums.AuthKeyType;
import com.kt.na_social.model.User;
import com.kt.na_social.ultis.KeyStore;
import com.kt.na_social.ultis.Navigator;
import com.kt.na_social.ultis.RetrofitApi;
import com.kt.na_social.ultis.StaticStore;
import com.kt.na_social.viewmodel.AuthStore;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView loader = findViewById(R.id.loader);
        Glide.with(getApplicationContext()).load(R.drawable.loader).fitCenter().into(loader);
        SVGImageView svgImageView = findViewById(R.id.svgImageView);
        loadSVG(svgImageView, R.raw.cuate);
        userApi = RetrofitApi.getInstance().create(UserApi.class);
        tryAutoLogin();
    }

    public void finishInit() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1000);
    }


    public void tryAutoLogin() {
        // first login with google
        // get data
        mAuth = FirebaseAuth.getInstance();
        String idToken = StaticStore.getByKey(KeyStore.AUTH_KEY, this);
        String email = StaticStore.getByKey(KeyStore.AUTH_EMAIL, this);
        String authType = StaticStore.getByKey(KeyStore.AUTH_KEY_TYPE, this);
        if (Objects.equals(authType, String.valueOf(AuthKeyType.GOOGLE))) {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
                if (!task.isSuccessful()) {
                    finishInit();
                    return;
                }
                handleFirebaseUser(null, idToken);
            }).addOnFailureListener((f) -> {
                Toast.makeText(this, "Credential expired", Toast.LENGTH_LONG).show();
            });
        } else if (Objects.equals(authType, AuthKeyType.PASSWORD.toString())) {
            mAuth.signInWithEmailAndPassword(email, idToken).addOnCompleteListener(this, task -> {
                if (!task.isSuccessful()) {
                    finishInit();
                    return;
                }
                handleFirebaseUser(idToken, null);
            });
        } else {
            finishInit();
        }
    }

    public void handleFirebaseUser(String password, String token) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) return;

        LoginRequestBody requestBody = new LoginRequestBody();
        requestBody.setDisplayName(firebaseUser.getDisplayName());
        requestBody.setPassword(password);
        requestBody.setUsername(firebaseUser.getEmail());
        requestBody.setAvatar(String.valueOf(firebaseUser.getPhotoUrl()));
        requestBody.setUid(firebaseUser.getUid());
        requestBody.setToken(token);
        // call api to login
        Call<BaseResponse<User>> responseCall = userApi.login(requestBody);
        responseCall.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User responseUser = response.body().getData();
                    User user = new User();
                    user.setEmail(firebaseUser.getEmail());
                    user.setUsername(firebaseUser.getDisplayName());
                    user.setProfileAvatar(String.valueOf(firebaseUser.getPhotoUrl()));
                    user.setUid(firebaseUser.getUid());

                    user.setToken(responseUser.getToken());
                    AuthStore.getInstance().setLoggedUser(user);
                    finishInit();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                finishInit();
            }
        });
        finishInit();
    }

    private void loadSVG(SVGImageView imageView, int resourceId) {
        try {
            SVG svg = SVG.getFromResource(getResources(), resourceId);
            imageView.setSVG(svg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}