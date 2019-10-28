package com.example.jianancangku.ui.activity;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.jianancangku.R;
import com.example.jianancangku.args.Constant;
import com.example.jianancangku.bean.AreaBean;
import com.example.jianancangku.bean.BaseData;
import com.example.jianancangku.bean.ThingfixBean;
import com.example.jianancangku.ui.fragment.GoHouseFragment;
import com.example.jianancangku.ui.fragment.GoThingFragment;
import com.example.jianancangku.ui.fragment.OutHouseFragment;
import com.example.jianancangku.ui.fragment.OutThingFragment;
import com.example.jianancangku.utils.BeanConvertor;
import com.example.jianancangku.utils.LogUtils;
import com.example.jianancangku.utils.StatusBarUtils;
import com.example.jianancangku.utils.TimeUtils;
import com.example.jianancangku.utils.ToastUtils;
import com.example.jianancangku.view.PopWindowsUtils;
import com.example.jianancangku.view.adpter.ThingfixAdapter;
import com.example.jianancangku.view.adpter.ThingsFixActivityViewPagerApdapter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.jianancangku.args.Constant.choiceareaAdrress;

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
    private List<ThingfixBean.ListBean> morerecydatalist;

    private ThingfixAdapter thingfixAdapter;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.date_choice_center)
    LinearLayout date_choice_center;
    @BindView(R.id.choice_area)
    LinearLayout choice_area;
    @BindView(R.id.iv_back_mythings)
    ImageView iv_back;
    @BindView(R.id.things_fix_recy)
    RecyclerView things_fix_recy;
    @BindView(R.id.toolbar_txt)
    TextView toolbar_txt;


    @BindView(R.id.choice_date_txt)
    TextView choice_date_txt;
    @BindView(R.id.choice_area_txt)
    TextView choice_area_txt;
    @BindView(R.id.choice_worker_txt)
    TextView choice_worker_txt;
    @BindView(R.id.choice)
    ImageView choice;
    @BindView(R.id.seacher)
    ImageView seacher;

    @BindView(R.id.choice_worker)
    LinearLayout choice_worker;

    @BindView(R.id.choice_layout)
    LinearLayout choice_layout;

    Unbinder unbinder;
    //  省
    private List<String> options1Items = new ArrayList<>();
    List<String> city;
    //  市
    private List<List<String>> options2Items = new ArrayList<>();
    //  区
    private List<List<List<String>>> options3Items = new ArrayList<>();
    //  省地理
    private List<String> options1Itemsnumber = new ArrayList<>();
    List<String> citynumber;
    //  市地理
    private List<List<String>> options2Itemsnumber = new ArrayList<>();
    //  区地理
    private List<List<List<String>>> options3Itemsnumber = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thingsfix_layout);
        unbinder = ButterKnife.bind((Activity) context);
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        smartRefreshLayout.finishRefresh(3000);//延迟3000毫秒后结束刷新

    }


    private void sendRegion() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        getMyDatas(null, null, null, null, null);
        getAreasDatas();
    }

    private int page = 1;

    private void init() {
        StatusBarUtils.setColor(context, Color.WHITE);
        date_choice_center.setVisibility(View.GONE);
        iv_back.setOnClickListener((View.OnClickListener) this);
        choice_area.setOnClickListener(this);
        choice_worker.setOnClickListener((View.OnClickListener) context);
        toolbar_txt.setOnClickListener((View.OnClickListener) context);
        viewPager = findViewById(R.id.thingfix_viewpager);
        get_choice_time_txt = findViewById(R.id.get_choice_time_txt);
        choice_timer_son = findViewById(R.id.choice_timer_son);
        choice.setOnClickListener((View.OnClickListener) context);

        choice_layout.setVisibility(View.GONE);
        choice_area.setVisibility(View.GONE);
        choice_worker.setVisibility(View.GONE);
        choice_timer_son.setVisibility(View.GONE);
        seacher.setOnClickListener((View.OnClickListener) context);

        //下拉刷新
        smartRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        smartRefreshLayout.setRefreshHeader(new WaterDropHeader(context));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
//                if (page == 1)
//                    ToastUtils.showToast(context, "当前为顶部");
                getMyDatas(null, null, null, null, null);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                page++;
                getMyDatas(null, null, null, null, null);
            }
        });
        initChoiceTime();
        initFragment();
//        date_choice_center.setVisibility(View.GONE);
        get_choice_time_txt.setText(TimeUtils.getCurrentYYMMDD());

//        initAreaChoice();
        choice_timer_son.setOnClickListener((View.OnClickListener) context);
        initWokers();

    }

    private void initWokers() {
        tabLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
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

    private void getMyDatas(String start_time, String end_time, String province_id, String city_id, String package_sn) {
        HttpParams params = new HttpParams();
        params.put("key", Constant.key);
        params.put("p", page);
        params.put("size", 20);


        if (start_time != null)
            params.put("start_time", start_time);
        if (end_time != null)
            params.put("end_time", end_time);
        if (province_id != null)
            params.put("province_id", Constant.key);
        if (city_id != null)
            params.put("city_id", city_id);
        if (package_sn != null)
            params.put("package_sn", package_sn);

        OkGo.<BaseData<ThingfixBean.ListBean>>post(Constant.thingsFixAddress)
                .tag(context)
                .params(params)
                .execute(new AbsCallback<BaseData<ThingfixBean.ListBean>>() {
                    @Override
                    public BaseData<ThingfixBean.ListBean> convertResponse(okhttp3.Response response) throws Throwable {
                        LogUtils.d(response);
                        assert response.body() != null;
                        final BaseData baseData = BeanConvertor.convertBean(response.body().string(), BaseData.class);
                        assert baseData != null;
                        final ThingfixBean thingfixBean = BeanConvertor.convertBean(baseData.getDatas(), ThingfixBean.class);
                        LogUtils.d(thingfixBean.getHasmore() + "分页");
                        if (page != 1)
                            morerecydatalist = thingfixBean.getList();
                        else
                            recydatalist = thingfixBean.getList();


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
                        if (page != 1) {
                            recydatalist.addAll(morerecydatalist);
                            thingfixAdapter.notifyDataSetChanged();
                        } else
                            thingfixAdapter = new ThingfixAdapter(context, recydatalist);
                        things_fix_recy.setAdapter(thingfixAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        things_fix_recy.setLayoutManager(linearLayoutManager);
                        things_fix_recy.setItemAnimator(new DefaultItemAnimator());

                    }
                });

    }

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
                LogUtils.e(String.valueOf(TimeUtils.getTime(time)));
                getMyDatas(String.valueOf(TimeUtils.getTime(time)), null, null, null, null);
                date_choice_center.setVisibility(View.VISIBLE);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTitleColor(0xFFF9731E)//标题文字颜色
                .setSubmitColor(0xFFF9731E)//确定按钮文字颜色
                .setCancelColor(0xFFF9731E)//取消按钮文字颜色
                .setRangDate(startDate, endDate)//起始终止年月日设定
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
            case R.id.choice_area:
                initColor("choice_area");
                initAreas();
                break;
            case R.id.choice_worker:
                initColor("choice_worker");
                initAreas();
                break;
            case R.id.choice:
                initChoice();
                break;
            case R.id.choice_timer_son:
                initColor("choice_date");
                pvTime.show(get_choice_time_txt, true);
                break;
            case R.id.seacher:
                startActivity(new Intent(context, SearchActivity.class));
                break;


        }
    }

    boolean ischoice = false;

    private void initChoice() {
        if (!ischoice) {
            choice_area.setVisibility(View.VISIBLE);
//        choice_worker.setVisibility(View.VISIBLE);
            choice_timer_son.setVisibility(View.VISIBLE);
            choice_layout.setVisibility(View.VISIBLE);
            date_choice_center.setVisibility(View.GONE);
            ischoice = true;
        } else {
            choice_area.setVisibility(View.GONE);
//        choice_worker.setVisibility(View.VISIBLE);
            choice_timer_son.setVisibility(View.GONE);
            date_choice_center.setVisibility(View.GONE);
            choice_layout.setVisibility(View.GONE);
            getMyDatas(null, null, null, null, null);
            ischoice = false;
        }

    }

    private void initColor(String msg) {
        choice_area_txt.setTextColor(msg.equals("choice_area") ? getResources().getColor(R.color.yelllow) : getResources().getColor(R.color.glay));
//        choice_worker_txt.setTextColor(msg.equals("choice_worker") ? getResources().getColor(R.color.yelllow) : getResources().getColor(R.color.glay));
        choice_date_txt.setTextColor(msg.equals("choice_date") ? getResources().getColor(R.color.yelllow) : getResources().getColor(R.color.glay));
    }

    private void initAreas() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showPicker2();
                pvOptions.show();

            }
        });


    }

    private void getAreasDatas() {
        HttpParams params = new HttpParams();
        params.put("key", Constant.key);
        OkGo.<BaseData<AreaBean.ListBean>>post(choiceareaAdrress)
                .tag(context)
                .params(params)
                .execute(new com.example.jianancangku.callback.AbsCallback<BaseData<AreaBean.ListBean>>() {
                    @Override
                    public BaseData<AreaBean.ListBean> convertResponse(okhttp3.Response response) throws Throwable {
                        LogUtils.d(response.body());
                        assert response.body() != null;
                        final BaseData baseData = BeanConvertor.convertBean(response.body().string(), BaseData.class);
                        assert baseData != null;
                        final AreaBean areaBean = BeanConvertor.convertBean(baseData.getDatas(), AreaBean.class);
                        for (int i = 0; i < areaBean.getList().size(); i++) {
                            String provinceName = areaBean.getList().get(i).getArea_name();
                            String provinceNamenumber = areaBean.getList().get(i).getArea_id();
//                            LogUtils.d(provinceName);
                            options1Items.add(provinceName);
                            options1Itemsnumber.add(provinceNamenumber);
                            List<AreaBean.ListBean.CitylistBean> list = areaBean.getList().get(i).getCitylist();
//                            options2Items = new ArrayList<>();
                            city = new ArrayList<>();
                            citynumber = new ArrayList<>();
                            for (int j = 0; j < list.size(); j++) {
//                                LogUtils.e(list.get(j).getArea_name());
                                city.add(list.get(j).getArea_name());
                                citynumber.add(list.get(j).getArea_id());

                            }
                            options2Items.add(city);
                            options2Itemsnumber.add(citynumber);

                        }
                        return baseData;
                    }

                    @Override
                    public void onStart(Request<BaseData<AreaBean.ListBean>, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<BaseData<AreaBean.ListBean>> response) {


                    }
                });

    }

    private void showPicker2() {
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
////                    address = provinceBeanList.get(options1) + " " + cityList.get(options1).get(option2) + " " + districtList.get(options1).get(option2).get(options3);
//                    address = provinceBeanList.get(options1) + "-" + cityList.get(options1).get(options2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.d(options1Itemsnumber.get(options1) + "---");
                        getMyDatas(null, null, options1Itemsnumber.get(options1), null, null);
//                        options1Items.get(options1)+options2Items.get(options1).get(options2)!=null?options2Items.get(options1).get(options2):null+options2Itemsnumber.get(options1).get(options2)!=null?options2Itemsnumber.get(options1).get(options2):null
                        ToastUtils.showToast(context, options1Items.get(options1));

                    }
                });
            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {

            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(0xFFF9731E)//标题文字颜色
                .setSubmitColor(0xFFF9731E)//确定按钮文字颜色
                .setCancelColor(0xFFF9731E)//取消按钮文字颜色
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
//                .isDialog(true)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .build();
        if (options1Items != null && options1Items != null)
            pvOptions.setPicker(options1Items, options2Items);//添加数据源

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

        PopWindowsUtils.getmInstance().showbokbarPopupWindow(
                context,
                toolbar_txt, new PopWindowsUtils.Icallback() {
                    @Override
                    public String call(String msg) {
                        if (msg.equals("1")) {
                            toolbar_txt.setText("我的订单");
                            things_fix_recy.setVisibility(View.VISIBLE);
                            tabLayout.setVisibility(View.GONE);
                            viewPager.setVisibility(View.GONE);
                        } else if (msg.equals("2")) {
                            toolbar_txt.setText("员工订单");
                            things_fix_recy.setVisibility(View.GONE);
                            tabLayout.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.VISIBLE);
                        }
                        return null;
                    }
                });
//        PopWindowsUtils.getmInstance().showfixthingPopupWindow(
//                context,
//                toolbar_txt,
//                R.layout.thingsfix_layout,
//                0.9f,
//                "员工订单",
//                v);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
