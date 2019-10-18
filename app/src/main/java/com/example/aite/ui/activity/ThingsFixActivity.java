package com.example.aite.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.aite.R;
import com.example.aite.ui.fragment.GoHouseFragment;
import com.example.aite.ui.fragment.OutHouseFragment;
import com.example.aite.utils.StatusBarUtils;
import com.example.aite.utils.TimeUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThingsFixActivity extends BaseActivity implements View.OnClickListener {
    //      viewHolder.textView.setText (String.format(mContext.getString(R.string.xxx), mDatas.get ( position ) ));
    private long time;
    private TextView get_choice_time_txt;
    private TimePickerView pvTime;
    private OptionsPickerView pvOptions;
    private LinearLayout choice_timer_son;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ThingsFixActivityViewPagerApdapter viewPagerAdapter;
    private View[] views;
    @BindView(R.id.date_choice_center)
    LinearLayout date_choice_center;
    @BindView(R.id.iv_back_mythings)
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thingsfix_layout);
        ButterKnife.bind((Activity) context);
        init();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        StatusBarUtils.setColor(context, Color.WHITE);
        iv_back.setOnClickListener((View.OnClickListener) this);

        viewPager = findViewById(R.id.thingfix_viewpager);
        get_choice_time_txt = findViewById(R.id.get_choice_time_txt);
        choice_timer_son = findViewById(R.id.choice_timer_son);
        initChoiceTime();
        initFragment();
//        date_choice_center.setVisibility(View.GONE);
        get_choice_time_txt.setText(TimeUtils.getCurrentYYMMDD());

//        initAreaChoice();
        choice_timer_son.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show(get_choice_time_txt, true);
            }
        });
    }

    private void initFragment() {
        tabLayout = findViewById(R.id.thingsfix_tabMode);
        views = new View[2];
        LayoutInflater layoutInflater = LayoutInflater.from(ThingsFixActivity.this);
        views[0] = layoutInflater.inflate(R.layout.gohouse_layout, null);
        views[1] = layoutInflater.inflate(R.layout.outhouse_layout, null);
        viewPagerAdapter = new ThingsFixActivityViewPagerApdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {


                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
//地区选择 未完成
//    private void initAreaChoice() {
//        pvOptions = new OptionsPickerBuilder(this, new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
//                //返回的分别是三个级别的选中位置
//                String tx = options1Items.get(options1).getPickerViewText()
//                        + options2Items.get(options1).get(option2)
//                        + options3Items.get(options1).get(option2).get(options3).getPickerViewText();
//                ToastUtils.showToast(ThingsFixActivity.this,tx);
//            }
//        }) .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
//            @Override
//            public void onOptionsSelectChanged(int options1, int options2, int options3) {
//                String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//
////                ToastUtils.showToast(ThingsFixActivity.this,str);
//
//            }
//        })
//                .setSubmitText("确定")//确定按钮文字
//                .setCancelText("取消")//取消按钮文字
//                .setTitleText("城市选择")//标题
//                .setSubCalSize(18)//确定和取消文字大小
//                .setTitleSize(20)//标题文字大小
//                .setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
//                .setCancelColor(Color.BLUE)//取消按钮文字颜色
//                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
//                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
//                .setContentTextSize(18)//滚轮文字大小
//                .setLinkage(false)//设置是否联动，默认true
//                .setLabels("省", "市", "区")//设置选择的三级单位
//                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setCyclic(false, false, false)//循环与否
//                .setSelectOptions(1, 1, 1)  //设置默认选中项
//                .setOutSideCancelable(false)//点击外部dismiss default true
//                .isDialog(true)//是否显示为对话框样式
//                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
//                .build();
//
//        pvOptions.setPicker(options1Items, options2Items, options3Items);//添加数据源
//    }

    private void initChoiceTime() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2017, 5, 21);
        endDate.set(2050, 11, 31);
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                time = date.getTime();
                String tim = TimeUtils.stampToDate(time);
                if (!TextUtils.isEmpty(tim))
                    get_choice_time_txt.setText(tim);
//                date_choice_center.setVisibility(View.VISIBLE);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
//                .setContentSize(18)//滚轮文字大小
//                .setTitleSize(20)//标题文字大小
//                .setTitleText("")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
//                .isCyclic(true)//是否循环滚动
                .setTitleColor(0xFFF9731E)//标题文字颜色
                .setSubmitColor(0xFFF9731E)//确定按钮文字颜色
                .setCancelColor(0xFFF9731E)//取消按钮文字颜色
//                //.setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
//                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
//                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
//                //.setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .isDialog(true)//是否显示为对话框样式
                .build();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_mythings:
                onBackPressed();
                break;

        }
    }

    class ThingsFixActivityViewPagerApdapter extends FragmentPagerAdapter {
        private volatile int num;
        GoHouseFragment goh;
        OutHouseFragment outh;

        public ThingsFixActivityViewPagerApdapter(FragmentManager fm, int num) {
            super(fm);
            this.num = num;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    if (goh == null) {
                        return new GoHouseFragment();
                    }
                case 1:
                    if (outh == null) {
                        return new OutHouseFragment();
                    }
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return num;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
