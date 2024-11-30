package com.kt.na_social.feed;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kt.na_social.R;
import com.kt.na_social.api.FeedApi;
import com.kt.na_social.api.MediaApi;
import com.kt.na_social.api.requests.feed.CreateNewFeed;
import com.kt.na_social.api.response.BaseResponse;
import com.kt.na_social.api.response.media.MediaResponse;
import com.kt.na_social.enums.FeedPrivacy;
import com.kt.na_social.model.Feed;
import com.kt.na_social.model.User;
import com.kt.na_social.ultis.FileUtils;
import com.kt.na_social.ultis.Navigator;
import com.kt.na_social.ultis.RetrofitApi;
import com.kt.na_social.viewmodel.AuthStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewFeedFragment extends Fragment {

    private Uri mediaUri;

    private String privacy;
    private ImageView btnClose;
    private Button btnPost;

    private TextView txtUsername;

    Spinner spn_privacy;
    EditText edtCaption;
    ImageView imvPreview;

    ImageView avatar;
    Button btnChoseImage;
    MediaApi mediaApi;

    FeedApi feedApi;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    mediaUri = uri;
                    Glide.with(requireContext()).load(uri).centerCrop().into(imvPreview);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_feed, container, false);
        onElement(root);
        actions(root);
        mediaApi = RetrofitApi.getInstance().create(MediaApi.class);
        feedApi = RetrofitApi.getInstance().create(FeedApi.class);
        return root;
    }

    public void onElement(View root) {
        User currentUser = AuthStore.getInstance().getLoggedUser();
        btnClose = root.findViewById(R.id.close_button);
        btnChoseImage = root.findViewById(R.id.album_button);
        btnPost = root.findViewById(R.id.post_button);

        txtUsername = root.findViewById(R.id.user_name);
        spn_privacy = root.findViewById(R.id.privacy_spinner);
        edtCaption = root.findViewById(R.id.caption_input);
        imvPreview = root.findViewById(R.id.photo_preview);
        avatar = root.findViewById(R.id.user_avatar);
        txtUsername.setText(currentUser.getUsername());
        Glide.with(requireContext()).load(currentUser.getProfileAvatar()).into(avatar);
    }

    public void actions(View root) {
        btnClose.setOnClickListener((v) -> {
            Navigator.navigateTo(R.id.link_go_to_feed, null);
        });
        btnChoseImage.setOnClickListener((v) -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build());
        });
        btnPost.setOnClickListener((v -> handlePostNewFeed()));
        spn_privacy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                privacy = selectedValue;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void goToFeed() {
        Navigator.navigateTo(R.id.link_go_to_feed, null);
    }

    public void handlePostNewFeed() {
        try {
            // post feed image first
            String caption = String.valueOf(edtCaption.getText());
            if (mediaUri == null) {
                Toast.makeText(requireActivity(), "U have to add media for your feed", Toast.LENGTH_LONG).show();
                return;
            }
            if (Objects.equals(caption, "")) {
                Toast.makeText(requireActivity(), "U have to write caption for your feed", Toast.LENGTH_LONG).show();
                return;
            }
            FeedPrivacy feedPrivacy = FeedPrivacy.PUBLIC;
            if (Objects.equals(privacy, "Private")) {
                feedPrivacy = FeedPrivacy.PRIVATE;
            }
            if (Objects.equals(privacy, "Friend")) {
                feedPrivacy = FeedPrivacy.ONLY_FRIEND;
            }
            Log.d("DEVV ::: ", AuthStore.getInstance().getLoggedUser().getToken());
//        upload image first
            List<MultipartBody.Part> fileParts = FileUtils.beforeUpload(requireContext(), mediaUri);
            Call<BaseResponse<List<MediaResponse>>> uploadMedia = mediaApi.uploadMedia(String.format("Bearer %s", AuthStore.getInstance().getLoggedUser().getToken()), fileParts);
            FeedPrivacy finalFeedPrivacy = feedPrivacy;
            uploadMedia.enqueue(new Callback<BaseResponse<List<MediaResponse>>>() {
                @Override
                public void onResponse(Call<BaseResponse<List<MediaResponse>>> call, Response<BaseResponse<List<MediaResponse>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<MediaResponse> mediaResponses = response.body().getData();
                        List<Long> medias = mediaResponses.stream().map(MediaResponse::getId).collect(Collectors.toList());
                        CreateNewFeed createNewFeed = new CreateNewFeed();
                        createNewFeed.setPrivacy(finalFeedPrivacy);
                        createNewFeed.setCaption(caption);
                        createNewFeed.setMediaIds(medias);
                        // call api to create new feed
                        Call<Feed> feedCall = feedApi.createNewFeed(String.format("Bearer %s", AuthStore.getInstance().getLoggedUser().getToken()), createNewFeed);
                        feedCall.enqueue(new Callback<Feed>() {
                            @Override
                            public void onResponse(Call<Feed> call, Response<Feed> response) {
                                if (response.isSuccessful()) {
                                    goToFeed();
                                }
                            }

                            @Override
                            public void onFailure(Call<Feed> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                    if (!response.isSuccessful()) {
                        Log.d("DEV ::: ", response.message());
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<List<MediaResponse>>> call, Throwable t) {
                    Log.d("DEVVV ::: ", t.getMessage());
                    Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Log.d("DEV", e.getMessage());
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}