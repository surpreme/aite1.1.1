package com.example.jianancangku.ui.activity;

import android.app.Activity;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.jianancangku.R;
import com.example.jianancangku.args.Constant;
import com.example.jianancangku.bean.BaseData;
import com.example.jianancangku.bean.ThingfixBean;
import com.example.jianancangku.callback.ICallback;
import com.example.jianancangku.ui.fragment.GoHouseFragment;
import com.example.jianancangku.ui.fragment.OutHouseFragment;
import com.example.jianancangku.utils.BeanConvertor;
import com.example.jianancangku.utils.LogUtils;
import com.example.jianancangku.utils.OkgoUtils;
import com.example.jianancangku.utils.StatusBarUtils;
import com.example.jianancangku.utils.TimeUtils;
import com.example.jianancangku.utils.ToastUtils;
import com.example.jianancangku.view.PopWindowsUtils;
import com.example.jianancangku.view.adpter.ThingfixAdapter;
import com.google.android.material.tabs.TabLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private List<ThingfixBean.ListBean> recydatalist;
    private ThingfixAdapter thingfixAdapter;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.date_choice_center)
    LinearLayout date_choice_center;
    @BindView(R.id.iv_back_mythings)
    ImageView iv_back;
    @BindView(R.id.things_fix_recy)
    RecyclerView things_fix_recy;
    @BindView(R.id.toolbar_txt)
    TextView toolbar_txt;


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
        toolbar_txt.setOnClickListener((View.OnClickListener) context);
        viewPager = findViewById(R.id.thingfix_viewpager);
        get_choice_time_txt = findViewById(R.id.get_choice_time_txt);
        choice_timer_son = findViewById(R.id.choice_timer_son);
        //下拉刷新
        smartRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        smartRefreshLayout.setRefreshHeader(new WaterDropHeader(context));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDatas();
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
            }
        });
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

    @Override
    protected void onStart() {
        super.onStart();
        getDatas();

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

    private void getDatas() {
        HttpParams params = new HttpParams();
        params.put("key", Constant.key);
        params.put("p", 1);
        params.put("size", 5);
//        params.put("start_time", 10);
//        params.put("end_time", Constant.key);
//        params.put("province_id", 2);
//        params.put("city_id", 10);
//        params.put("package_sn", 2);
//        OkgoUtils<ThingfixBean> okgoUtils = new OkgoUtils<ThingfixBean>();
//        okgoUtils.post(Constant.LogInAddress, context, params, new ICallback() {
//            @Override
//            public void onSuccess(String result, List list) {
//                if (list == null) {
//                    ToastUtils.showToast(context, "数据错误");
//                    return;
//                }
//                thingfixAdapter = new ThingfixAdapter(context, list);
//                things_fix_recy.setAdapter(thingfixAdapter);
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//                things_fix_recy.setLayoutManager(linearLayoutManager);
//                things_fix_recy.setItemAnimator(new DefaultItemAnimator());
//            }
//
//            @Override
//            public void onFailure(String e) {
//                ToastUtils.showToast(context,e);
//
//            }
//        });

        OkGo.<BaseData<ThingfixBean.ListBean>>post(Constant.thingsFixAddress)
                .tag(context)
                .params(params)
                .execute(new AbsCallback<BaseData<ThingfixBean.ListBean>>() {
                    @Override
                    public BaseData<ThingfixBean.ListBean> convertResponse(okhttp3.Response response) throws Throwable {
                        LogUtils.d(response);
                        final BaseData baseData = BeanConvertor.convertBean(response.body().string(), BaseData.class);
                        final ThingfixBean thingfixBean = BeanConvertor.convertBean(baseData.getDatas(), ThingfixBean.class);
                        recydatalist = thingfixBean.getList();
                        LogUtils.d(recydatalist);
                        return null;
                    }

                    @Override
                    public void onStart(Request<BaseData<ThingfixBean.ListBean>, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<BaseData<ThingfixBean.ListBean>> response) {
                        LogUtils.d(recydatalist);
                        if (recydatalist == null) {
                            ToastUtils.showToast(context, "数据错误");
                            return;
                        }
                        thingfixAdapter = new ThingfixAdapter(context, recydatalist);
                        things_fix_recy.setAdapter(thingfixAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        things_fix_recy.setLayoutManager(linearLayoutManager);
                        things_fix_recy.setItemAnimator(new DefaultItemAnimator());

                    }
                });

    }
//        ║ {"hasmore":1,"list":[{"create_time":"2019-10-12 10:09:51","business_address":"湖北省\t十堰市\t张湾区123","package_sn":"2000000000104001","type":"2"},{"create_time":"2019-10-11 19:28:40","business_address":"安徽省\t芜湖市\t三山区tgj","package_sn":"2000000000105201","type":"2"},{"create_time":"2019-10-11 19:27:50","business_address":"安徽省\t芜湖市\t三山区tgj","package_sn":"2000000000105201","type":"1"},{"create_time":"2019-10-11 19:08:32","business_address":"安徽省\t芜湖市\t三山区tgj","package_sn":"2000000000105001","type":"2"},{"create_time":"2019-10-11 19:08:21","business_address":"安徽省\t芜湖市\t三山区tgj","package_sn":"2000000000105001","type":"1"}]}

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
            case R.id.toolbar_txt:
                showpopwindow();
                break;

        }
    }

    private void showpopwindow() {
        View.OnClickListener v = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toolbar_txt.getText().toString().equals("员工订单"))
                    toolbar_txt.setText("我的订单");
                if (toolbar_txt.getText().toString().equals("我的订单"))
                    toolbar_txt.setText("员工订单");
                PopWindowsUtils.getmInstance().dismissPopWindow();

            }
        };
        PopWindowsUtils.getmInstance().showfixthingPopupWindow(
                context,
                toolbar_txt,
                R.layout.thingsfix_layout,
                0.9f,
                "员工订单",
                v);

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
