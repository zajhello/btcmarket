package cn.bizzan.ui.wallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gyf.barlibrary.ImmersionBar;

import cn.bizzan.R;
import cn.bizzan.app.GlobalConstant;
import cn.bizzan.base.ActivityManage;
import cn.bizzan.ui.entrust.DropdownLayout;
import cn.bizzan.adapter.ShaiXuanAdapter;
import cn.bizzan.adapter.TiBiAdapter;
import cn.bizzan.base.BaseActivity;
import cn.bizzan.base.LinListView;
import cn.bizzan.entity.TiBiBean;
import cn.bizzan.app.UrlFactory;
import cn.bizzan.utils.SharedPreferenceInstance;
import cn.bizzan.utils.WonderfulLogUtils;
import cn.bizzan.utils.WonderfulToastUtils;
import cn.bizzan.utils.okhttp.StringCallback;
import cn.bizzan.utils.okhttp.WonderfulOkhttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/8/8.
 */
public class TiBiJLActivity extends BaseActivity {
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.ibCalendar)
    ImageButton ibCalendar;
    @BindView(R.id.dropdownLayout)
    DropdownLayout dropdownLayout;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.listview_1)
    LinListView listview_1;
    @BindView(R.id.tvMessage)
    LinearLayout tvMessage;
    @BindView(R.id.view_xianshi)
    View view_xianshi;

    private List<String> lists = new ArrayList<>();
    private List<TiBiBean> beans = new ArrayList<>();
    private int page = 0;
    private int page2 = 1;
    private String bizhong = "";

    private TiBiAdapter adapter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_tibijilu;
    }


    public static void actionStart(Context context, List<String> list) {
        Intent intent = new Intent(context, TiBiJLActivity.class);
        intent.putStringArrayListExtra("list", (ArrayList<String>) list);
        context.startActivity(intent);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        view_xianshi.setVisibility(View.GONE);
        ArrayList<String> list = getIntent().getStringArrayListExtra("list");
        lists = list;
        for (int i = 0; i < lists.size(); i++) {
            WonderfulLogUtils.logi("miao", lists.get(i));
        }
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropdownLayout.isOpen()) {
                    dropdownLayout.hide();
                    Drawable drawable = getResources().getDrawable(R.drawable.icon_filter_no);
                    ibCalendar.setBackgroundDrawable(drawable);
                    listview.setVisibility(View.GONE);
                    view_xianshi.setVisibility(View.GONE);
                } else {
                    dropdownLayout.show();
                    Drawable drawable = getResources().getDrawable(R.drawable.icon_filter_orange);
                    ibCalendar.setBackgroundDrawable(drawable);
                    listview.setVisibility(View.VISIBLE);
                    view_xianshi.setVisibility(View.VISIBLE);
                }

            }
        });

        listview.setAdapter(new ShaiXuanAdapter(TiBiJLActivity.this, lists));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = lists.get(position);
                bizhong = s;
                dropdownLayout.hide();
                Drawable drawable = getResources().getDrawable(R.drawable.icon_filter_no);
                ibCalendar.setBackgroundDrawable(drawable);
                listview.setVisibility(View.GONE);
                view_xianshi.setVisibility(View.GONE);
                page = 0;
                page2 = 1;
                qingQiu(s);
            }
        });

    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        if (GlobalConstant.isUdun()) {
            queryOut("");
        } else {
            qingQiu("");
        }


    }

    private void qingQiu(String symbol) {

        WonderfulOkhttpUtils.post().url(UrlFactory.getChaTiBi())
                .addHeader("x-auth-token", SharedPreferenceInstance.getInstance().getTOKEN())
                .addParams("page", page + "")
                .addParams("pageSize", "40")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                hideLoadingPopup();
                listview_1.stopFreshing();
                listview_1.setVisibility(View.GONE);
                tvMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("miao", "提币记录：" + response);
                if (page == 0) {
                    beans.clear();
                }
                if (listview_1 == null) {

                    return;
                }
                listview_1.stopFreshing();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                    JSONArray content = jsonObject1.optJSONArray("content");

                    if (content.length() == 0 && page == 0) {
                        listview_1.setVisibility(View.GONE);
                        tvMessage.setVisibility(View.VISIBLE);
                        return;
                    }
                    for (int i = 0; i < content.length(); i++) {
                        JSONObject jsonObject3 = content.optJSONObject(i);
                        JSONObject jsonObject2 = jsonObject3.optJSONObject("coin");
                        TiBiBean bean = new TiBiBean();
                        bean.name = jsonObject2.optString("unit");
                        bean.dizhi = jsonObject3.optString("address");
                        bean.time = jsonObject3.optString("createTime");
                        bean.number = new BigDecimal(jsonObject3.optDouble("totalAmount")).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                        bean.shouxufei = new BigDecimal(jsonObject3.optDouble("fee")).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                        int status = jsonObject3.optInt("status");

                        if (status == 0) {
                            bean.zhuangtai = WonderfulToastUtils.getString(TiBiJLActivity.this, R.string.creditting);
                        } else if (status == 1) {
                            bean.zhuangtai = WonderfulToastUtils.getString(TiBiJLActivity.this, R.string.Waiting_money);
                        } else if (status == 2) {
                            bean.zhuangtai = WonderfulToastUtils.getString(TiBiJLActivity.this, R.string.fail);
                        } else if (status == 3) {
                            bean.zhuangtai = WonderfulToastUtils.getString(TiBiJLActivity.this, R.string.success);
                        }

                        beans.add(bean);
                    }

                    tvMessage.setVisibility(View.GONE);
                    listview_1.setVisibility(View.VISIBLE);
                    adapter = new TiBiAdapter(TiBiJLActivity.this, beans);
                    listview_1.setEveryPageItemCount(40);
                    listview_1.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listview_1.setOnRefreshListener(new LinListView.OnRefreshListener() {
                        @Override
                        public void onLoadMore() {
                            page = page + 1;
                            qingQiu(bizhong);
                        }

                        @Override
                        public void onRefresh() {
                            page = 0;
                            beans.clear();
                            qingQiu(bizhong);
                        }
                    });

                } catch (Exception e) {
                    listview_1.setVisibility(View.GONE);
                    tvMessage.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }

            }
        });
    }


    private void queryOut(String symbol) {


        int id1 = SharedPreferenceInstance.getInstance().getID();
        WonderfulOkhttpUtils.get().url(UrlFactory.getqueryO())
                .addParams("x-auth-token", SharedPreferenceInstance.getInstance().getTOKEN())
                .addParams("pageNo", page2 + "")
                .addParams("pageSize", "40")
                .addParams("memberId", id1 + "")
//                .addParams("symbol", "" + symbol)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                hideLoadingPopup();
                listview_1.stopFreshing();
                listview_1.setVisibility(View.GONE);
                tvMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("miao", "提币记录：" + response);
                if (page2 == 1) {
                    beans.clear();
                }
                if (listview_1 == null) {

                    return;
                }
                listview_1.stopFreshing();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optInt("code") == 200) {

                        JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                        JSONArray content = jsonObject1.optJSONArray("data");

                        if (content.length() == 0 && page2 == 1) {
                            listview_1.setVisibility(View.GONE);
                            tvMessage.setVisibility(View.VISIBLE);
                            return;
                        }
                        for (int i = 0; i < content.length(); i++) {
                            JSONObject jsonObject3 = content.optJSONObject(i);

                            TiBiBean bean = new TiBiBean();
                            bean.name = jsonObject3.optString("symbol");
                            bean.dizhi = jsonObject3.optString("address");
                            bean.time = jsonObject3.optString("createTime");


                            String amount = jsonObject3.getString("actualAmount");
                            if (TextUtils.isEmpty(amount) || TextUtils.equals("null", amount)) {
                                amount = "0";
                            }
                            bean.number = new BigDecimal(amount).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();

                            String fee = jsonObject3.getString("myFee");
                            if (TextUtils.isEmpty(fee) || TextUtils.equals("null", fee)) {
                                fee = "0";
                            }
                            bean.shouxufei = new BigDecimal(fee).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                            int status = jsonObject3.optInt("payStatus");

                            if (status == 0) {
                                bean.zhuangtai = WonderfulToastUtils.getString(TiBiJLActivity.this, R.string.creditting);
                            } else if (status == 1) {
                                bean.zhuangtai = WonderfulToastUtils.getString(TiBiJLActivity.this, R.string.Waiting_money);
                            } else if (status == 2) {
                                bean.zhuangtai = WonderfulToastUtils.getString(TiBiJLActivity.this, R.string.fail);
                            } else if (status == 3) {
                                bean.zhuangtai = WonderfulToastUtils.getString(TiBiJLActivity.this, R.string.success);
                            }

                            beans.add(bean);
                        }

                        tvMessage.setVisibility(View.GONE);
                        listview_1.setVisibility(View.VISIBLE);
                        adapter = new TiBiAdapter(TiBiJLActivity.this, beans);
                        listview_1.setEveryPageItemCount(40);
                        listview_1.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        listview_1.setOnRefreshListener(new LinListView.OnRefreshListener() {
                            @Override
                            public void onLoadMore() {
                                page2 = page2 + 1;
                                queryOut(bizhong);
                            }

                            @Override
                            public void onRefresh() {
                                page2 = 1;
                                beans.clear();
                                queryOut(bizhong);
                            }
                        });
                    }

                } catch (Exception e) {
                    listview_1.setVisibility(View.GONE);
                    tvMessage.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        });
    }
}
