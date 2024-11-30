package com.kt.na_social.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kt.na_social.R;
import com.kt.na_social.adapters.FeedAdapter;
import com.kt.na_social.api.FeedApi;
import com.kt.na_social.api.response.BaseResponse;
import com.kt.na_social.api.response.PageableResponse;
import com.kt.na_social.model.Feed;
import com.kt.na_social.ultis.ApiUtils;
import com.kt.na_social.ultis.Navigator;
import com.kt.na_social.ultis.RetrofitApi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFeedFragment extends Fragment {
    private FeedAdapter feedAdapter;
    private RecyclerView feedCycle;
    ImageButton mBtnGoNewFeed;
    ImageButton mBtnGoSearch;
    ImageButton mBtnGoNoti;
    private int LIMIT = 10;
    private int PAGE = 0;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_feed, container, false);
        initElement(root);
        return root;
    }

    public int getStartRecord(int page) {
        return ((page - 1) * LIMIT);
    }

    public int getEndRecord(int page) {
        return getStartRecord(page) + LIMIT;
    }
    // page , limit
    // PAGE += 1;
    // get feed by page

    public void initElement(View root) {
        feedCycle = root.findViewById(R.id.rcy_feed);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        mBtnGoNewFeed = root.findViewById(R.id.btn_go_new_feed);
        mBtnGoNewFeed.setOnClickListener(v -> {
            Navigator.navigateTo(R.id.link_go_to_new_feed, null);
        });
        mBtnGoSearch = root.findViewById(R.id.btn_go_search);
        mBtnGoSearch.setOnClickListener(v -> {
            Navigator.navigateTo(R.id.link_go_to_search, null);
        });
        mBtnGoNoti = root.findViewById(R.id.btn_go_noti);
        mBtnGoNoti.setOnClickListener(v -> {
            Navigator.navigateTo(R.id.link_go_to_noti, null);
        });
        FeedAdapter.IFeedAction iFeedAction = new FeedAdapter.IFeedAction() {
            @Override
            public void likeFeed(long feedId) {

            }

            @Override
            public void openFeedComment(long feedId) {

            }

            @Override
            public void shareFeed(long feedId) {

            }

            @Override
            public void bookMarkFeed(long feedId) {

            }
        };
        feedAdapter = new FeedAdapter(new ArrayList<>(), requireContext(), iFeedAction);
        feedCycle.setAdapter(feedAdapter);
        feedCycle.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        loadFeed(PAGE);
        feedCycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    PAGE += 1;
                    loadFeed(PAGE);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGE = 0;
                loadFeed(PAGE);
            }
        });
    }

    private static List<Feed> removeDuplicates(List<Feed> feeds) {
        List<Feed> result = new ArrayList<>();
        for (Feed feed : feeds) {
            if (result.stream().noneMatch((f) -> f.getId() == feed.getId())) {
                result.add(feed);
            }
        }
        return new ArrayList<>(result);
    }

    public void loadFeed(int offsets) {
        // fetch from server
        FeedApi feedApi = RetrofitApi.getInstance().create(FeedApi.class);
        Call<BaseResponse<PageableResponse<List<Feed>>>> call = feedApi.getNewsFeed(ApiUtils.getApiToken(), getStartRecord(offsets), LIMIT);
        call.enqueue(new Callback<BaseResponse<PageableResponse<List<Feed>>>>() {
            @Override
            public void onResponse(Call<BaseResponse<PageableResponse<List<Feed>>>> call, Response<BaseResponse<PageableResponse<List<Feed>>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("fetch success", "ascasc");
                    List<Feed> allFeed = feedAdapter.getFeedList();
                    allFeed.addAll(response.body().getData().getContent());
                    feedAdapter.updateData(removeDuplicates(allFeed));
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Log.d("fetch error1 :: ", response.message());
                    Toast.makeText(requireActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<PageableResponse<List<Feed>>>> call, Throwable t) {
                Log.d("fetch error :: ", t.getMessage());
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}