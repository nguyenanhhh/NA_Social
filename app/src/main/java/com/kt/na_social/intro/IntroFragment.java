package com.kt.na_social.intro;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kt.na_social.R;
import com.kt.na_social.adapters.IntroSlideAdapter;
import com.kt.na_social.model.IntroItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IntroFragment extends Fragment {

    private ViewPager2 mIntroPager;

    private IntroSlideAdapter introSlideAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_intro_fragment, container, false);
        mIntroPager = root.findViewById(R.id.slide_container);
        List<IntroItem> items = new ArrayList<>();
        items.add(new IntroItem(UUID.randomUUID().toString(), "Find Friends & Get Inspiration", "Meet and interact with individuals from various backgrounds who can help ignite your creativity.", R.drawable.rafiki));
        items.add(new IntroItem(UUID.randomUUID().toString(), "Meet Awesome People & Enjoy yourself", "Join fun activities, workshops, and social gatherings designed to foster connections and spark creativity.", R.drawable.amico));
        items.add(new IntroItem(UUID.randomUUID().toString(), "Hangout with with Friends", "Enjoy a relaxed atmosphere that encourages open conversations and laughter.", R.drawable.cuate));
        introSlideAdapter = new IntroSlideAdapter(items, requireContext());
        mIntroPager.setAdapter(introSlideAdapter);
        mIntroPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        return root;
    }
}