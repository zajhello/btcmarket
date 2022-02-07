package cn.bizzan.ui.currency_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.bizzan.R;
import cn.bizzan.adapter.CountryAdapter;
import cn.bizzan.adapter.CurrencyAdapter;
import cn.bizzan.app.GlobalConstant;
import cn.bizzan.app.Injection;
import cn.bizzan.base.BaseActivity;
import cn.bizzan.entity.Country;
import cn.bizzan.entity.CurrencyEntity;
import cn.bizzan.events.CurrencyEvent;
import cn.bizzan.ui.home.MainActivity;
import cn.bizzan.utils.SharedPreferenceInstance;
import cn.bizzan.utils.WonderfulCodeUtils;

public class CurrencyListActivity extends BaseActivity implements CurrencyListContract.View {

    public static final int RETURN_CURRENCY = 0;

    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ibHelp)
    ImageButton ibHelp;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.rvCountry)
    RecyclerView rvCountry;
    private List<CurrencyEntity> currency = new ArrayList<>();
    private CurrencyAdapter adapter;
    @BindView(R.id.view_back)
    View view_back;

    String key;
    private CurrencyListContract.Presenter presenter;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, CurrencyListActivity.class);
        activity.startActivityForResult(intent, RETURN_CURRENCY);
    }

    public static void actionStart(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), CurrencyListActivity.class);
        fragment.startActivityForResult(intent, RETURN_CURRENCY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_currency;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new CurrencyListPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {
        initRvCountry();
    }

    private void initRvCountry() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCountry.setLayoutManager(manager);
        adapter = new CurrencyAdapter(R.layout.adapter_currency, currency);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 返回全局法币名
                key = currency.get(position).getName();
                presenter.CurrentRate(key);
            }
        });
        rvCountry.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        presenter.CurrencyList();
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
    public void setPresenter(CurrencyListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void CurrencyListSuccess(Map<String, CurrencyEntity> obj) {
        if (obj == null) return;

        // 设置全局法币
//        GlobalConstant.setGlobalCurrencyMap(obj);
        this.currency.clear();
        this.currency.addAll(obj.values());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void CurrencyListFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void CurrencyRateSuccess(double obj) {
        MainActivity.rate = obj;
        MainActivity.symbol = key;
        SharedPreferenceInstance.getInstance().setSymbol(key);

        back();
    }

    @Override
    public void CurrencyRatetFail(Integer code, String toastMessage) {

        MainActivity.rate = 0;
        MainActivity.symbol = key;
        SharedPreferenceInstance.getInstance().setSymbol(key);

        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
        back();
    }

    private void back() {
        EventBus.getDefault().post(new CurrencyEvent());
        Intent intent = new Intent();
        CurrencyListActivity.this.setResult(RESULT_OK, intent);
        finish();
    }
}
