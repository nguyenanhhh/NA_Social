package com.kt.na_social.fragments.auth;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kt.na_social.R;
import com.kt.na_social.model.User;
import com.kt.na_social.ultis.FileUtils;
import com.kt.na_social.ultis.Navigator;
import com.kt.na_social.viewmodel.AuthStore;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {
    FirebaseAuth mAuth;
    private EditText txtEmail;
    private EditText txtPassword;
    LinearLayout btnPickAvt;
    private ImageView imvPreview;
    private EditText txtUsername;
    private Uri avatarUri;
    private MaterialButton btnRegister;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    Glide.with(requireContext()).load(uri).circleCrop()
                            .into(imvPreview);
                    avatarUri = uri;
                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        initElement(root);
        registerAction(root);
        mAuth = FirebaseAuth.getInstance();
        return root;
    }

    public void initElement(View root) {
        txtEmail = root.findViewById(R.id.edt_email);
        txtPassword = root.findViewById(R.id.edt_password);
        txtUsername = root.findViewById(R.id.edt_name);
        imvPreview = root.findViewById(R.id.imv_avt_preview);
        btnRegister = root.findViewById(R.id.btn_register_action);
        btnPickAvt = root.findViewById(R.id.btn_pick_avatar_action);
    }

    public void registerAction(View root) {
        btnRegister.setOnClickListener((e) -> signUp());
        btnPickAvt.setOnClickListener((e) -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));
    }

    public boolean valid(String email, String password, String name) {
        return isValidEmail(email) && isValidPassword(password) && isValidName(name);
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 50;
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasUpperCase = !password.equals(password.toLowerCase());
        boolean hasLowerCase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }

    public void signUp() {
        String email = String.valueOf(txtEmail.getText());
        String password = String.valueOf(txtPassword.getText());
        String name = String.valueOf(txtUsername.getText());
        if (!valid(email, password, name)) {
            Toast.makeText(requireContext(), "Register Data Not Valid Format", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
            if (!task.isSuccessful()) {
                Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference();
            StorageReference child = firebaseStorage.child("uploads/" + UUID.randomUUID().toString() + FileUtils.getFileExtension(requireContext(), avatarUri));
            UploadTask uploadTask = child.putFile(avatarUri);
            uploadTask.continueWithTask(task1 -> {
                if (!task1.isSuccessful()) {
                    throw Objects.requireNonNull(task1.getException());
                }
                return child.getDownloadUrl();
            }).addOnCompleteListener(task12 -> {
                if (task12.isSuccessful()) {
                    Uri downloadUri = task12.getResult();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(downloadUri)
                                .build();
                        currentUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(ts -> {
                                    if (ts.isSuccessful()) {
                                        Navigator.goBack();
                                        Toast.makeText(requireContext(), "Register Success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        });
    }
}