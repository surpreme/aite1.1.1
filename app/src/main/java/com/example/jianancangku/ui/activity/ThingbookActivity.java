package com.example.jianancangku.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.jianancangku.R;
import com.example.jianancangku.ui.fragment.GoHouseFragment;
import com.example.jianancangku.ui.fragment.OutHouseFragment;
import com.example.jianancangku.view.adpter.FragmentAdpter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThingbookActivity extends BaseActivity {
    @BindView(R.id.thingscar_viewpager)
    ViewPager viewPager;
    @BindView(R.id.thingscar_tabMode)
    TabLayout tabLayout;
    private FragmentAdpter fragmentAdpter;
    private View[] views;
    @BindView(R.id.iv_back_thingbook)
    ImageView iv_back_thingbook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thingscar_layout);
        ButterKnife.bind((Activity) context);
        init();
    }

    private void init() {
        iv_back_thingbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        views = new View[2];
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        views[0] = layoutInflater.inflate(R.layout.gohouse_layout, null);
        views[1] = layoutInflater.inflate(R.layout.outhouse_layout, null);
        fragmentAdpter = new FragmentAdpter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(fragmentAdpter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}
