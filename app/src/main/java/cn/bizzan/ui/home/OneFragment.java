package cn.bizzan.ui.home;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bizzan.R;
import cn.bizzan.adapter.BannerImageLoader;
import cn.bizzan.adapter.HomeAdapter;
import cn.bizzan.adapter.HomeOneAdapter;
import cn.bizzan.app.GlobalConstant;
import cn.bizzan.app.Injection;
import cn.bizzan.app.MyApplication;
import cn.bizzan.app.UrlFactory;
import cn.bizzan.base.BaseTransFragment;
import cn.bizzan.customview.intercept.WonderfulScrollView;
import cn.bizzan.entity.BannerEntity;
import cn.bizzan.entity.Coin;
import cn.bizzan.entity.Currency;
import cn.bizzan.entity.LunBoBean;
import cn.bizzan.entity.Message;
import cn.bizzan.entity.WalletConstract;
import cn.bizzan.ui.chatlist.ChatListActivity;
import cn.bizzan.ui.ctc.CTCActivity;
import cn.bizzan.ui.home.presenter.CommonPresenter;
import cn.bizzan.ui.home.presenter.ICommonView;
import cn.bizzan.ui.kline.KlineActivity;
import cn.bizzan.ui.login.LoginActivity;
import cn.bizzan.ui.message_detail.MessageDetailActivity;
import cn.bizzan.ui.myinfo.MyInfoActivity;
import cn.bizzan.ui.setting.GongGaoActivity;
import cn.bizzan.ui.setting.HelpActivity;
import cn.bizzan.ui.wallet.WalletActivity;
import cn.bizzan.utils.SharedPreferenceInstance;
import cn.bizzan.utils.WonderfulCodeUtils;
import cn.bizzan.utils.WonderfulLogUtils;
import cn.bizzan.utils.WonderfulMathUtils;
import cn.bizzan.utils.WonderfulStringUtils;
import cn.bizzan.utils.okhttp.StringCallback;
import cn.bizzan.utils.okhttp.WonderfulOkhttpUtils;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/1/7.
 */

public class OneFragment extends BaseTransFragment implements MainContract.OneView, ICommonView {
    public static final String TAG = OneFragment.class.getSimpleName();
    @BindView(R.id.ibMessage)
    ImageButton ibMessage;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.line_zichan)
    LinearLayout line_zichan;
    @BindView(R.id.line_ctc)
    LinearLayout line_ctc;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.main_linear)
    LinearLayout main_linear;
    @BindView(R.id.line_help)
    LinearLayout line_help;
    @BindView(R.id.text_gengduo)
    TextView text_gengduo;
    @BindView(R.id.line_gonggao)
    LinearLayout line_gonggao;

    @BindView(R.id.text_login)
    TextView text_login;

    @BindView(R.id.text_loginbtn)
    TextView text_loginbtn;


    @BindView(R.id.text_totalassets)
    TextView text_totalassets;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView; // ?????????
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.mRecyclerViewdown)
    RecyclerView mRecyclerViewdown;
    private HomeAdapter mHomeAdapter; // ???????????????
    private HomeAdapter mHomeAdapterdown; // ?????????????????????
    //    @BindView(R.id.refreshLayout)
//    TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.scrollView)
    WonderfulScrollView scrollView;
    @BindView(R.id.ivchatTip)
    ImageView ivchatTip;
    Unbinder unbinder;
    private List<String> imageUrls = new ArrayList<>();
    private List<LunBoBean> banners = new ArrayList<>();
    private List<Integer> localImageUrls = new ArrayList<Integer>() {{
        add(R.mipmap.icon_banner);
    }};
    private List<Currency> currencies = new ArrayList<>();
    private List<Currency> currenciesall = new ArrayList<>();
    private List<Currency> currenciesdown = new ArrayList<>();
    private List<Currency> currenciesTwo = new ArrayList<>();
    private HomeOneAdapter adapter;
    private MainContract.OnePresenter presenter;

    private CommonPresenter commonPresenter;
    private String sysAdvertiseLocation = "1";

    private List<Coin> coins = new ArrayList<>();
    private List<WalletConstract> contract = new ArrayList<>();

    public static double available_amount = 0;
    double availbleUsd = 0;
    double availbleUsd_c = 0;
    double sumUsd = 0;
    double sumCny = 0;
    double sumUsd_c = 0;
    double sumCny_c = 0;
    // ?????????
    private PopupWindow loadingPopup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_one;
    }

    /**
     * ???????????????dialog
     */
    private void initLoadingPopup() {
        View loadingView = getLayoutInflater().inflate(R.layout.pop_loading, null);
        loadingPopup = new PopupWindow(loadingView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingPopup.setFocusable(true);
        loadingPopup.setClippingEnabled(false);
        loadingPopup.setBackgroundDrawable(new ColorDrawable());
    }

    /**
     * ???????????????
     */
    @Override
    public void displayLoadingPopup() {
        loadingPopup.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * ???????????????
     */
    @Override
    public void hideLoadingPopup() {
        if (loadingPopup != null) {
            loadingPopup.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.startAutoPlay();
        loadData();
    }

    private void loginingViewText() {
        try {
            text_loginbtn.setVisibility(View.GONE);
            text_totalassets.setVisibility(View.VISIBLE);
            text_totalassets.setText("$ " + WonderfulMathUtils.getRundNumber(sumUsd + sumUsd_c, 4, null));
            text_login.setText(getActivity().getResources().getText(R.string.login_assets));
        } catch (Exception e) {

        }
    }

    private void notLoginViewText() {
        try {
            text_loginbtn.setVisibility(View.VISIBLE);
            text_totalassets.setVisibility(View.GONE);
            text_login.setText(getActivity().getResources().getText(R.string.login_view));
        } catch (Exception e) {

        }
    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay();
    }

    private void dialogShow2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.custom_dialog);
        final Dialog dialog = builder.create();
        dialog.show();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.dialog_lock, null);
        dialog.getWindow().setContentView(v);
        TextView content = v.findViewById(R.id.text_quxiao);
        final CheckBox checkbox = v.findViewById(R.id.checkbox);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()) {
                    SharedPreferenceInstance.getInstance().saveTishi(checkbox.isChecked());
                }
                dialog.dismiss();
            }
        });
        TextView text_queding = v.findViewById(R.id.text_queding);
        text_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    if (checkbox.isChecked()) {
                        SharedPreferenceInstance.getInstance().saveTishi(checkbox.isChecked());
                    }
                    dialog.dismiss();
                    MyInfoActivity.actionStart(getActivity());
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }
            }
        });

    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        initLoadingPopup();
        getMessage();
        new CommonPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        tab.addTab(tab.newTab().setText(R.string.up_announcement));
        tab.addTab(tab.newTab().setText(R.string.down_announcement));
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerViewdown.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) {
                    mRecyclerView.setVisibility(View.GONE);
                    mRecyclerViewdown.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        line_zichan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    WalletActivity.actionStart(getActivity());
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }
            }
        });
        line_ctc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CTCActivity.actionStart(getmActivity());
            }
        });
        line_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpActivity.actionStart(getmActivity());
            }
        });
        text_gengduo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GongGaoActivity.actionStart(getmActivity());
            }
        });
        line_gonggao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GongGaoActivity.actionStart(getmActivity());
            }
        });
        ibMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivchatTip.setVisibility(View.INVISIBLE);
                ChatListActivity.actionStart(getActivity());
            }
        });
        if (MyApplication.getApp().isLogin()) {
            if (WonderfulStringUtils.isEmpty(SharedPreferenceInstance.getInstance().getLockPwd())) {
                if (SharedPreferenceInstance.getInstance().getTishi()) {
                    return;
                }
                dialogShow2();
            }
        }
    }

    @Override
    protected void obtainData() {
    }

    @Override
    protected void fillWidget() {
        initRvContent();

    }

    class MyAdapter extends PagerAdapter {
        private List<Currency> lists = new ArrayList<>();

//        private int colorA = Color.parseColor("#1F2833");
//        private int colorC = Color.parseColor("#1A212A");

        private int colorA = Color.parseColor("#001F2833");
        private int colorC = Color.parseColor("#001A212A");

        @Override
        public int getCount() {
            int size = currenciesTwo.size();
            if (size == 0) {
                return 0;
            }
            int i = size % 3;
            int a = size / 3;
            if (i > 0) {
                a = a + 1;
            }
            return a;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_home_viewpage, null, false);
            LinearLayout line = inflate.findViewById(R.id.line);
            //?????????
            LinearLayout line_one = inflate.findViewById(R.id.line_one);
            TextView tvCurrencyName = inflate.findViewById(R.id.tvCurrencyName);
            ImageView ivCollect = inflate.findViewById(R.id.ivCollect);
            TextView tvClose = inflate.findViewById(R.id.tvClose);
            String oldClose = tvClose.getText().toString();
            TextView tvAddPercent = inflate.findViewById(R.id.tvAddPercent);
            TextView tvVol = inflate.findViewById(R.id.tvVol);

            //?????????
            LinearLayout line_two = inflate.findViewById(R.id.line_two);
            TextView tvCurrencyName1 = inflate.findViewById(R.id.tvCurrencyName1);
            ImageView ivCollect1 = inflate.findViewById(R.id.ivCollect1);
            TextView tvClose1 = inflate.findViewById(R.id.tvClose1);
            String oldClose1 = tvClose1.getText().toString();
            TextView tvAddPercent1 = inflate.findViewById(R.id.tvAddPercent1);
            TextView tvVol1 = inflate.findViewById(R.id.tvVol1);
            //?????????
            LinearLayout line_three = inflate.findViewById(R.id.line_three);
            TextView tvCurrencyName2 = inflate.findViewById(R.id.tvCurrencyName2);
            ImageView ivCollect2 = inflate.findViewById(R.id.ivCollect2);
            TextView tvClose2 = inflate.findViewById(R.id.tvClose2);
            String oldClose2 = tvClose2.getText().toString();
            TextView tvAddPercent2 = inflate.findViewById(R.id.tvAddPercent2);
            TextView tvVol2 = inflate.findViewById(R.id.tvVol2);
            int star = position * 3;
            int end = (position + 1) * 3;
            lists.clear();
            for (int i = 0; i <= currenciesTwo.size(); i++) {
                if (i >= star && i < end && i < currenciesTwo.size()) {
                    lists.add(currenciesTwo.get(i));
                }
            }
            for (int i = 0; i < lists.size(); i++) {
                if (i == 0) {
                    final Currency currency = lists.get(i);
                    line_one.setVisibility(View.VISIBLE);
                    ivCollect.setImageResource(currency.isCollect() ? R.mipmap.icon_collect_yes : R.mipmap.icon_collect_no);
                    tvCurrencyName.setText(currency.getSymbol());
                    String newClose = WonderfulMathUtils.getRundNumber(currency.getClose(), 2, null);

                    if (!newClose.equals(oldClose)) {
                        line.animate().setDuration(300).start();
                        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(line, "backgroundColor", colorA, colorC);
                        objectAnimator.setDuration(300);
                        objectAnimator.setEvaluator(new ArgbEvaluator());
                        objectAnimator.start();
                    }

                    tvClose.setText(newClose);
                    tvAddPercent.setText((currency.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(currency.getChg() * 100, 2, "########0.") + "%");

                    tvVol.setText("???" + WonderfulMathUtils.getRundNumber(currency.getClose() * currency.getBaseUsdRate() * MainActivity.rate, 2, null) + " " + MainActivity.symbol);

                    tvClose.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
                    tvAddPercent.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
                    line_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            KlineActivity.actionStart(getActivity(), currency.getSymbol(), "1");
                        }
                    });
                }
                if (i == 1) {
                    final Currency currency = lists.get(i);
                    line_two.setVisibility(View.VISIBLE);
                    ivCollect1.setImageResource(currency.isCollect() ? R.mipmap.icon_collect_yes : R.mipmap.icon_collect_no);
                    tvCurrencyName1.setText(currency.getSymbol());
                    String newClose = WonderfulMathUtils.getRundNumber(currency.getClose(), 2, null);

                    if (!newClose.equals(oldClose1)) {
                        line.animate().setDuration(300).start();
                        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(line, "backgroundColor", colorA, colorC);
                        objectAnimator.setDuration(300);
                        objectAnimator.setEvaluator(new ArgbEvaluator());
                        objectAnimator.start();
                    }

                    tvClose1.setText(newClose);
                    tvAddPercent1.setText((currency.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(currency.getChg() * 100, 2, "########0.") + "%");
                    tvVol1.setText("???" + WonderfulMathUtils.getRundNumber(currency.getClose() * currency.getBaseUsdRate() * MainActivity.rate, 2, null) + " " + MainActivity.symbol);
                    tvClose1.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
                    tvAddPercent1.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
                    line_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            KlineActivity.actionStart(getActivity(), currency.getSymbol(), "1");
                        }
                    });
                }
                if (i == 2) {
                    final Currency currency = lists.get(i);
                    line_three.setVisibility(View.VISIBLE);
                    ivCollect2.setImageResource(currency.isCollect() ? R.mipmap.icon_collect_yes : R.mipmap.icon_collect_no);
                    tvCurrencyName2.setText(currency.getSymbol());
                    String newClose = WonderfulMathUtils.getRundNumber(currency.getClose(), 2, null);

                    if (!newClose.equals(oldClose2)) {
                        line.animate().setDuration(300).start();
                        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(line, "backgroundColor", colorA, colorC);
                        objectAnimator.setDuration(300);
                        objectAnimator.setEvaluator(new ArgbEvaluator());
                        objectAnimator.start();
                    }

                    tvClose2.setText(newClose);
                    tvAddPercent2.setText((currency.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(currency.getChg() * 100, 2, "########0.") + "%");
                    tvVol2.setText("???" + WonderfulMathUtils.getRundNumber(currency.getClose() * currency.getBaseUsdRate() * MainActivity.rate, 2, null) + " " + MainActivity.symbol);
                    tvClose2.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
                    tvAddPercent2.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
                    line_three.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            KlineActivity.actionStart(getActivity(), currency.getSymbol(), "1");
                        }
                    });
                }

            }
            container.addView(inflate);
            return inflate;
        }

    }

    private void initRvContent() {
        // ?????????????????????
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        System.out.println("currencies" + currencies.toString());
        mHomeAdapter = new HomeAdapter(currencies);
        mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mHomeAdapter.isFirstOnly(true);
        mHomeAdapter.setLoad(true);
        mRecyclerView.setAdapter(mHomeAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                KlineActivity.actionStart(getActivity(), OneFragment.this.mHomeAdapter.getData().get(position).getSymbol(), "1");
            }
        });
        // ?????????????????????
        mRecyclerViewdown.setLayoutManager(new LinearLayoutManager(getActivity()));
//        ((SimpleItemAnimator) mRecyclerViewdown.getItemAnimator()).setSupportsChangeAnimations(false);
        System.out.println("currencies" + currenciesdown.toString());
        mHomeAdapterdown = new HomeAdapter(currenciesdown);
        mHomeAdapterdown.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mHomeAdapterdown.isFirstOnly(true);
        mHomeAdapterdown.setLoad(true);
        mRecyclerViewdown.setAdapter(mHomeAdapterdown);
        mRecyclerViewdown.setHasFixedSize(true);
        mRecyclerViewdown.setNestedScrollingEnabled(false);
        mHomeAdapterdown.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                KlineActivity.actionStart(getActivity(), OneFragment.this.mHomeAdapterdown.getData().get(position).getSymbol(), "1");
            }
        });
    }

    @Override
    protected void loadData() {
        notifyData();
        if (imageUrls == null || imageUrls.size() == 0) {
            presenter.banners(sysAdvertiseLocation);
        }

        if (MyApplication.getApp().isLogin()) {
            presenter.myWallet(getmActivity().getToken());
            presenter.myWallet_Constract(getmActivity().getToken());
            presenter.getUdunConf(getmActivity().getToken());
        } else {
            notLoginViewText();
        }
    }

    @Override
    public void myWalletSuccess(List<Coin> obj) {
        if (obj == null) return;
        this.coins.clear();
        this.coins.addAll(obj);
        calcuTotal();
    }

    @Override
    public void myWalletSuccess_Constract(List<WalletConstract> obj) {
        if (obj == null) return;
        this.contract.clear();
        this.contract.addAll(obj);
        calcuTotal();
    }

    @Override
    public void myWalletFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void myUdunConfSuccess(boolean isUdun) {
        SharedPreferenceInstance.getInstance().setUdun(isUdun);
    }

    @Override
    public void myUdunConfFail() {

    }

    private void calcuTotal() {
        if (coins.size() != 0 && contract.size() != 0) {
            available_amount = 0;
            sumUsd = 0;
            sumCny = 0;
            sumUsd_c = 0;
            sumCny_c = 0;
            availbleUsd = 0;
            availbleUsd_c = 0;

            for (Coin coin : coins) {
                // ??????????????????????????????????????????????????????
                sumUsd += ((coin.getBalance() + coin.getFrozenBalance()) * coin.getCoin().getUsdRate());
                sumCny += ((coin.getBalance() + coin.getFrozenBalance()) * coin.getCoin().getCnyRate());

                if (TextUtils.equals("USDT", coin.getCoin().getUnit())) {
                    availbleUsd += (coin.getBalance() * coin.getCoin().getUsdRate());
                }
            }
            for (WalletConstract constract : contract) {
                // ??????????????????????????????????????????????????????
                sumUsd_c += (constract.getUsdtBalance() + constract.getUsdtFrozenBalance() + constract.getUsdtBuyPrincipalAmount()
                        + constract.getUsdtSellPrincipalAmount() + constract.getUsdtTotalProfitAndLoss());

                sumCny_c += (constract.getUsdtBalance() + constract.getUsdtFrozenBalance() + constract.getUsdtBuyPrincipalAmount()
                        + constract.getUsdtSellPrincipalAmount() + constract.getUsdtTotalProfitAndLoss()) * constract.getContractCoin().getUsdtRate();

//                availbleUsd_c += (constract.getUsdtBalance() + constract.getUsdtBuyPrincipalAmount()
//                        + constract.getUsdtSellPrincipalAmount() + constract.getUsdtTotalProfitAndLoss()) * constract.getContractCoin().getUsdtRate();
            }

            available_amount = availbleUsd_c + availbleUsd;

            loginingViewText();
        }
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }

    @Override
    public void setPresenter(MainContract.OnePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setPresenter(CommonPresenter presenter) {
        this.commonPresenter = presenter;
    }

    private MyAdapter adapter2;

    public void dataLoadedall(List<Currency> currenciesall) {
        this.currenciesall = currenciesall;
        Collections.sort(currenciesall, new Comparator<Currency>() {
            @Override
            public int compare(Currency o1, Currency o2) {
                if (o1.getChg() > o2.getChg()) {
                    return -1;
                } else if (o1.getChg() < o2.getChg()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        List<Currency> top = new ArrayList<>();
        List<Currency> down = new ArrayList<>();
        for (int i = 0; i < currenciesall.size(); i++) {
            if (currenciesall.get(i).getChg() > 0) {
                top.add(currenciesall.get(i));
            }
            if (currenciesall.get(i).getChg() < 0) {
                down.add(currenciesall.get(i));
            }
        }
        this.currencies.clear();
        this.currenciesdown.clear();
        if (top.size() > 10) {
            this.currencies.addAll(top.subList(0, 10));
        } else {
            this.currencies.addAll(top);
        }
        Collections.reverse(down);
        if (down.size() > 10) {
            this.currenciesdown.addAll(down.subList(0, 10));
        } else {
            this.currenciesdown.addAll(down);
        }
        top = null;
        down = null;
        WonderfulLogUtils.logi("???????????????????????????top", this.currencies.size() + ":" + this.currencies.toString());
        WonderfulLogUtils.logi("???????????????????????????down", this.currenciesdown.size() + ":" + this.currenciesdown.toString());
        mHomeAdapter.notifyDataSetChanged();
        mHomeAdapterdown.notifyDataSetChanged();
    }

    public void dataLoaded(List<Currency> tow) {
        this.currenciesTwo.clear();
        this.currenciesTwo.addAll(tow);

        adapter2 = new MyAdapter();
        viewPager.setAdapter(adapter2);
        int size = currenciesTwo.size();
        int i1 = size % 3;
        int a = size / 3;
        if (i1 > 0) {
            a = a + 1;
        }
        main_linear.removeAllViews();
        WonderfulLogUtils.logi("miao", "a:" + a);
        for (int c = 0; c < a; c++) {
            View view = new View(getActivity());
            view.setBackgroundResource(R.drawable.zhishiqi);
            if (c == 0) {
                view.setEnabled(true);
            } else {
                view.setEnabled(false);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(35, 3);
            layoutParams.leftMargin = 10;
            main_linear.addView(view, layoutParams);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                main_linear.getChildAt(mNum).setEnabled(false);
                main_linear.getChildAt(position).setEnabled(true);
                mNum = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int mNum = 0;

    public void setChatTip(boolean hasNew) {
        if (hasNew) {
            ivchatTip.setVisibility(View.VISIBLE);
        } else {
            ivchatTip.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void deleteFail(Integer code, String toastMessage) {
        if (code == GlobalConstant.TOKEN_DISABLE1) {
            LoginActivity.actionStart(getActivity());
        } else {
            WonderfulCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
        }
    }

    @Override
    public void deleteSuccess(String obj, int position) {
        this.currencies.get(position).setCollect(false);
        adapter.notifyDataSetChanged();
        if (adapter2 != null) {
            adapter2.notifyDataSetChanged();
        }
    }

    @Override
    public void addFail(Integer code, String toastMessage) {
        if (code == GlobalConstant.TOKEN_DISABLE1) {
            LoginActivity.actionStart(getActivity());
        } else {
            WonderfulCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
        }
    }

    @Override
    public void addSuccess(String obj, int position) {
        this.currencies.get(position).setCollect(true);
        adapter.notifyDataSetChanged();
        if (adapter2 != null) {
            adapter2.notifyDataSetChanged();
        }
    }

    public void notifyData() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        if (adapter2 != null) {
            adapter2.notifyDataSetChanged();
        }
    }

    @Override
    public void bannersSuccess(final List<BannerEntity> obj) {
        if (obj == null) {
            return;
        }
        if (imageUrls != null && imageUrls.size() > 1) {
            return;
        }
        for (BannerEntity bannerEntity : obj) {
            imageUrls.add(GlobalConstant.getGlobalImagePath(bannerEntity.getUrl()));
        }
        if (imageUrls.size() == 0) {
            banner = banner.setImages(localImageUrls);
        } else {
            if (banner == null) {
                return;
            }
            banner.setImages(imageUrls);
        }
        if (imageUrls.size() > 0) {
            // ??????????????????
            banner.setImages(imageUrls);
        }
        // ????????????
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setIndicatorGravity(BannerConfig.CENTER)
                .setImageLoader(new BannerImageLoader(banner.getWidth(), banner.getHeight()));
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (obj.size() == 0) {
                    return;
                }
                if (!TextUtils.isEmpty(obj.get(position).getLinkUrl())) {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(obj.get(position).getLinkUrl()));
                    intent.setAction(Intent.ACTION_VIEW);
                    getmActivity().startActivity(intent);
                }
            }
        });
        //??????????????????
        banner.setDelayTime(3000);
        banner.setBannerAnimation(Transformer.Default);
        banner.start();
    }

    @Override
    public void bannersFail(Integer code, String toastMessage) {
        //do nothing
    }

    public void tcpNotify() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (mHomeAdapter != null) {
            mHomeAdapter.notifyDataSetChanged();
        }
        if (mHomeAdapterdown != null) {
            mHomeAdapterdown.notifyDataSetChanged();
        }

        if (adapter2 != null) {
            adapter2.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        hideLoadingPopup();
    }

    @Override
    public void onStart() {
        super.onStart();
        hideLoadingPopup();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (marqueeView != null) {
                marqueeView.stopFlipping();
            }
        } else {
            if (marqueeView != null) {
                if (!marqueeView.isFlipping()) {
                    marqueeView.startFlipping();
                }
            }
        }
    }

    private void getMessage() {
        WonderfulOkhttpUtils.post().url(UrlFactory.getMessageUrl())
                .addParams("pageNo", 1 + "").addParams("pageSize", "8")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Message> messages = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<Message>>() {
                        }.getType());
                        messageList.clear();
                        messageList.addAll(messages);
                        setMarqueeView(messageList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                MessageDetailActivity.actionStart(getActivity(), messageList.get(infoss.get(position)).getId());
            }
        });
    }

    private List<Message> messageList = new ArrayList<>();
    private List<String> info = new ArrayList<>();
    private List<Integer> infoss = new ArrayList<>();

    private void setMarqueeView(List<Message> messageList) {
        info.clear();
        int code = SharedPreferenceInstance.getInstance().getLanguageCode();
        if (code == 1) {
            //??????
            for (int i = 0; i < messageList.size(); i++) {
                Message message = messageList.get(i);
                if (isContainChinese(message.getTitle())) {
                    String str = "";
                    if (message.getTitle().length() > 20) {
                        str = message.getTitle();
                        str = str.substring(0, 20);
                        info.add(str + "...");
                    } else {
                        info.add(message.getTitle());
                    }

                    infoss.add(i);
                }
            }

        } else {
            for (int i = 0; i < messageList.size(); i++) {
                Message message = messageList.get(i);
                if (!isContainChinese(message.getTitle())) {
                    info.add(message.getTitle());
                    infoss.add(i);
                }
            }
        }
        marqueeView.startWithList(info);
    }

    static Pattern p = Pattern.compile("[\u4e00-\u9fa5]");

    public static boolean isContainChinese(String str) {
        Matcher m = p.matcher(str);
        return m.find();
    }
}
