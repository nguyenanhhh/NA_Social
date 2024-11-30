package com.kt.na_social.fragments.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.credentials.CredentialManager;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kt.na_social.MainActivity;
import com.kt.na_social.R;
import com.kt.na_social.api.UserApi;
import com.kt.na_social.api.requests.LoginRequestBody;
import com.kt.na_social.api.response.BaseResponse;
import com.kt.na_social.api.response.UserResponse;
import com.kt.na_social.enums.AuthKeyType;
import com.kt.na_social.model.User;
import com.kt.na_social.ultis.KeyStore;
import com.kt.na_social.ultis.Navigator;
import com.kt.na_social.ultis.RetrofitApi;
import com.kt.na_social.ultis.StaticStore;
import com.kt.na_social.viewmodel.AuthStore;

import java.security.Key;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    public String AUTH_TOKEN_KEY = "GOOGLE_AUTH_TOKEN";

    CredentialManager credentialManager;
    private CardView mGoogleLoginBtn;
    private MaterialButton btnLogin;
    private TextView btnGotoSignUp;

    private TextView txtEmail;
    private TextView txtPassword;

    private TextView btnForgotPassword;
    MaterialCheckBox cbRememberMe;

    private final int RC_SIGN_IN = 66;

    private GoogleSignInClient googleSignInClient;
    UserApi userApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        initElement(root);
        registerAction(root);
        mAuth = FirebaseAuth.getInstance();
        checkIsLoggedIn();
        credentialManager = CredentialManager.create(requireContext());
        userApi = RetrofitApi.getInstance().create(UserApi.class);
        return root;
    }

    public void checkIsLoggedIn() {
        if (AuthStore.getInstance().getLoggedUser() != null) {
            goToFeed();
        }
    }

    public void initElement(View root) {
        mGoogleLoginBtn = root.findViewById(R.id.btn_google_login);
        txtEmail = root.findViewById(R.id.edt_email);
        txtPassword = root.findViewById(R.id.edt_password);
        cbRememberMe = root.findViewById(R.id.cb_remember);
        btnGotoSignUp = root.findViewById(R.id.txt_sign_up_action);
        btnForgotPassword = root.findViewById(R.id.txt_forgot_action);
        btnLogin = root.findViewById(R.id.btn_login_action);
    }


    public void registerAction(View root) {
        mGoogleLoginBtn.setOnClickListener((e) -> signInWithGoogle());
        btnLogin.setOnClickListener((e) -> signInWithEmailAndPassword());
        btnGotoSignUp.setOnClickListener((e) -> Navigator.navigateTo(R.id.link_go_to_register, null));
        btnForgotPassword.setOnClickListener(e -> forgotPassword());
    }

    public void forgotPassword() {

    }


    public void signInWithGoogle() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    public void signInWithEmailAndPassword() {
        String email = String.valueOf(txtEmail.getText());
        String password = String.valueOf(txtPassword.getText());
        if (!valid(email, password)) {
            Toast.makeText(requireActivity(), "Please Enter Email And Password", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), (task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(requireActivity(), "Email or Password Incorrect!", Toast.LENGTH_LONG).show();
                return;
            }
            handleFirebaseUser(password, null);
            if (cbRememberMe.isChecked()) {
                saveCredentials(null, password, email, AuthKeyType.PASSWORD);
            }
        }));
    }

    public boolean valid(String email, String password) {
        if (email.trim().equals("")) {
            return false;
        }
        return !password.trim().equals("");
    }


    public void goToFeed() {
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build();
        Navigator.navigateTo(R.id.link_go_to_feed, navOptions);
    }

    public void handleFirebaseUser(String password, String token) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
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
                        User userResp = response.body().getData();
                        User user = new User();
                        user.setEmail(firebaseUser.getEmail());
                        user.setUsername(firebaseUser.getDisplayName());
                        user.setProfileAvatar(String.valueOf(firebaseUser.getPhotoUrl()));
                        user.setUid(firebaseUser.getUid());
                        user.setToken(userResp.getToken());
                        AuthStore.getInstance().setLoggedUser(user);
                        goToFeed();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                    Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(authCredential).addOnCompleteListener((task) -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(requireActivity(), "Login failed , Pls Try Later", Toast.LENGTH_LONG).show();
                        return;
                    }
                    handleFirebaseUser(null, account.getIdToken());
                    saveCredentials(account, null, null, AuthKeyType.GOOGLE);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveCredentials(@Nullable GoogleSignInAccount account, String password, String cEmail, AuthKeyType authKeyType) {
        if (authKeyType == AuthKeyType.GOOGLE) {
            if (account == null) return;
            String email = account.getEmail();
            String idToken = account.getIdToken();
            if (email == null || idToken == null) return;
            StaticStore.setByKey(KeyStore.AUTH_KEY, idToken, requireContext());
            StaticStore.setByKey(KeyStore.AUTH_EMAIL, email, requireContext());
            StaticStore.setByKey(KeyStore.AUTH_KEY_TYPE, String.valueOf(AuthKeyType.GOOGLE), requireContext());
            return;
        }
        if (authKeyType == AuthKeyType.PASSWORD) {
            if (cEmail == null || password == null) return;
            StaticStore.setByKey(KeyStore.AUTH_KEY, password, requireContext());
            StaticStore.setByKey(KeyStore.AUTH_EMAIL, cEmail, requireContext());
            StaticStore.setByKey(KeyStore.AUTH_KEY_TYPE, String.valueOf(AuthKeyType.PASSWORD), requireContext());
        }

    }
}