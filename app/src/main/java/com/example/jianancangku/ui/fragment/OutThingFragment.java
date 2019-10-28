package com.example.jianancangku.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jianancangku.R;
import com.example.jianancangku.args.Constant;
import com.example.jianancangku.bean.BaseData;
import com.example.jianancangku.bean.GothingBean;
import com.example.jianancangku.utils.BeanConvertor;
import com.example.jianancangku.utils.LogUtils;
import com.example.jianancangku.utils.ToastUtils;
import com.example.jianancangku.view.adpter.ThingAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class OutThingFragment extends BaseFragment {
    private RecyclerView outed_house_recy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.outhouse_layout, container, false);
        outed_house_recy = view.findViewById(R.id.thingbook_out_recy);
        getDatas(null, null, null, null, null, null);
        return view;
    }

    private void getDatas(String start_time, String end_time, String province_id, String city_id, String package_sn, String type) {
        HttpParams params = new HttpParams();
        params.put("key", Constant.key);
        params.put("p", 1);//1 入库员 2 出库员
        params.put("size", 20);

        params.put("type", 2);
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

        OkGo.<BaseData<GothingBean.ListBean>>post(Constant.workerthingAdrress)
                .tag(getContext())
                .params(params)
                .execute(new AbsCallback<BaseData<GothingBean.ListBean>>() {
                    @Override
                    public BaseData<GothingBean.ListBean> convertResponse(okhttp3.Response response) throws Throwable {
                        LogUtils.d(response);
                        final BaseData baseData = BeanConvertor.convertBean(response.body().string(), BaseData.class);
                        final GothingBean gothingBean = BeanConvertor.convertBean(baseData.getDatas(), GothingBean.class);
                        if (!baseData.isSuccessed())
                            ToastUtils.showToast(getActivity(), baseData.getErrorMsg());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ThingAdapter recyAdapter = new ThingAdapter(getActivity(), gothingBean.getList(),"out");
                                outed_house_recy.setAdapter(recyAdapter);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                outed_house_recy.setLayoutManager(linearLayoutManager);
                                outed_house_recy.setItemAnimator(new DefaultItemAnimator());
                            }
                        });
                        return null;
                    }

                    @Override
                    public void onStart(Request<BaseData<GothingBean.ListBean>, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<BaseData<GothingBean.ListBean>> response) {

                    }
                });

    }

}
