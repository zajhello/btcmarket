package cn.bizzan.ui.mylab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;

import butterknife.BindArray;
import butterknife.BindView;
import cn.bizzan.R;
import cn.bizzan.app.GlobalConstant;
import cn.bizzan.app.Injection;
import cn.bizzan.app.MyApplication;
import cn.bizzan.base.BaseActivity;
import cn.bizzan.entity.LabDetailEntity;
import cn.bizzan.entity.LabEntity;
import cn.bizzan.entity.User;
import cn.bizzan.ui.home.OneFragment;
import cn.bizzan.ui.myinfo.MyInfoPresenter;
import cn.bizzan.utils.WonderfulCodeUtils;
import cn.bizzan.utils.WonderfulDateUtils;
import cn.bizzan.utils.WonderfulMathUtils;
import cn.bizzan.utils.WonderfulToastUtils;

public class LabDetaiActivity extends BaseActivity implements LabDetailContract.View {

    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.view_back)
    View viewBack;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.iv_average)
    ImageView ivAverage;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subtitle)
    TextView subtitle;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.process)
    ProgressBar mProcess;
    @BindView(R.id.tvPercent)
    TextView tvPercent;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.purchase_type)
    TextView purchaseType;
    @BindView(R.id.instruction)
    TextView instruction;
    @BindView(R.id.tv_amount2)
    TextView tv_amount2;
    @BindView(R.id.tv_cost)
    TextView tv_cost;
    @BindView(R.id.tv_cost_top)
    TextView tv_cost_top;
    @BindView(R.id.tvBuy)
    TextView tvBuy;

    @BindArray(R.array.laboratory_tab)
    String[] tabs;

    @BindView(R.id.tv_activity_currency)
    TextView tv_activity_currency;
    @BindView(R.id.tv_activity_currency_title)
    TextView tv_activity_currency_title;


    @BindView(R.id.tv_accept_currency_title)
    TextView tv_accept_currency_title;
    @BindView(R.id.tv_accept_currency)
    TextView tv_accept_currency;
    @BindView(R.id.tv_limit_num)
    TextView tv_limit_num;
    @BindView(R.id.tv_limit_amount)
    TextView tv_limit_amount;
    @BindView(R.id.tv_start_time)
    TextView tv_start_time;
    @BindView(R.id.tv_end_time)
    TextView tv_end_time;

    @BindView(R.id.cc_status_bottom)
    LinearLayout ll_status_bottom;


    @BindView(R.id.release_type_title)
    TextView release_type_title;
    @BindView(R.id.release_detail_title)
    TextView release_detail_title;
    @BindView(R.id.lock_cost_title)
    TextView lock_cost_title;
    @BindView(R.id.release_times_title)
    TextView release_times_title;

    @BindView(R.id.release_type)
    TextView tv_release_type;
    @BindView(R.id.release_detail)
    TextView tv_release_detail;
    @BindView(R.id.lock_cost)
    TextView tv_lock_cost;
    @BindView(R.id.release_times)
    TextView tv_release_times;


    @BindView(R.id.buy_status)
    CardView buy_status;
    @BindView(R.id.tv_bought_amount)
    TextView tv_bought_amount;
    @BindView(R.id.tv_remaining_amount)
    TextView tv_remaining_amount;
    @BindView(R.id.ll_input)
    LinearLayout ll_input;


    @BindView(R.id.cc_status)
    LinearLayout cc_status;
    @BindView(R.id.tv_cc_amount)
    TextView tv_cc_amount;
    @BindView(R.id.tv_tcc_amount)
    TextView tv_tcc_amount;
    @BindView(R.id.tv_my_amount)
    TextView tv_my_amount;

    @BindView(R.id.input_amount)
    EditText input_amount;
    @BindView(R.id.input_amount_unit)
    TextView input_amount_unit;
    @BindView(R.id.my_available_amount)
    TextView my_available_amount;


    private LabEntity entity;
    private LabDetailEntity detailEntity;


    public static void actionStart(Context context, LabEntity entity) {
        Intent intent = new Intent(context, LabDetaiActivity.class);
        intent.putExtra("entity", entity);
        context.startActivity(intent);
    }

    private LabDetailContract.Presenter presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_lab_detai;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new LabDetailPresenter(Injection.provideTasksRepository(getApplicationContext()), this);

        entity = (LabEntity) getIntent().getSerializableExtra("entity");
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLoadingPopup();
                finish();
            }
        });
        viewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLoadingPopup();
                finish();
            }
        });
        tvBuy.setOnClickListener(view -> {

            double mount = new BigDecimal(input_amount.getText().toString()).doubleValue();
            if (detailEntity.getMinLimitAmout() > mount) {
                WonderfulToastUtils.showToast(getResources().getString(R.string.lab_detail_min_limit_tip));
                return;
            }

            if (detailEntity.getMaxLimitAmout() < mount) {
                WonderfulToastUtils.showToast(getResources().getString(R.string.lab_detail_max_limit_tip));
                return;
            }

            getInstance(this);
        });
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {
        tvTitle.setText(entity.getTitle());
        title.setText(entity.getTitle());
        subtitle.setText(entity.getDetail());
        showJustText(content, entity.getContent());
        Glide.with(this).load(GlobalConstant.getGlobalImagePath(entity.getSmallImageUrl())).into(ivAverage);
        int process = 0;
        if (entity.getType() == 3) {
            if (entity.getStep() == 1) {
                process = 50;
            } else if (entity.getStep() == 2) {
                process = 75;
            } else if (entity.getStep() == 3) {
                process = 100;
            } else {
                process = 0;
            }
        } else {
            process = entity.getProgress();
        }

        mProcess.setProgress(process);
        String format = new DecimalFormat("#0.00").format(process);
        tvPercent.setText(format + "%");
        String format2 = new DecimalFormat("#0.00").format(entity.getTotalSupply());
        amount.setText(format2 + "  " + entity.getUnit());
        tv_amount2.setText(format2 + "  " + entity.getUnit());
        status.setText(tabs[entity.getStep()]);

        String type;
        if (entity.getType() == 1) {
            type = getResources().getString(R.string.lab_type1);
            tv_cost.setVisibility(View.VISIBLE);
            tv_cost_top.setVisibility(View.VISIBLE);
            buy_status.setVisibility(View.GONE);
//            ll_input.setVisibility(View.GONE);

        } else if (entity.getType() == 2) {
            type = getResources().getString(R.string.lab_type2);
            tv_cost.setVisibility(View.VISIBLE);
            tv_cost_top.setVisibility(View.VISIBLE);
//            ll_input.setVisibility(View.GONE);

        } else if (entity.getType() == 3) {
            type = getResources().getString(R.string.lab_type3);
        } else if (entity.getType() == 4) {
            type = getResources().getString(R.string.lab_type4);
            tv_cost.setVisibility(View.VISIBLE);
            tv_cost_top.setVisibility(View.VISIBLE);
            buy_status.setVisibility(View.VISIBLE);
        } else if (entity.getType() == 5) {
            type = getResources().getString(R.string.lab_type5);
            tv_cost.setVisibility(View.VISIBLE);
            tv_cost_top.setVisibility(View.VISIBLE);
            buy_status.setVisibility(View.VISIBLE);
            tv_activity_currency_title.setVisibility(View.GONE);
            tv_activity_currency.setVisibility(View.GONE);

        } else if (entity.getType() == 6) {
            type = getResources().getString(R.string.lab_type6);
            tv_release_type.setVisibility(View.VISIBLE);
            release_type_title.setVisibility(View.VISIBLE);
            tv_release_detail.setVisibility(View.VISIBLE);
            release_detail_title.setVisibility(View.VISIBLE);
            tv_lock_cost.setVisibility(View.VISIBLE);
            lock_cost_title.setVisibility(View.VISIBLE);
            tv_release_times.setVisibility(View.VISIBLE);
            release_times_title.setVisibility(View.VISIBLE);
            tv_activity_currency_title.setVisibility(View.GONE);
            tv_activity_currency.setVisibility(View.GONE);

            if (entity.getReleaseType() == 1) {
                tv_release_type.setText(getResources().getString(R.string.lab_detail_proportional_release));
            } else {
                tv_release_type.setText(getResources().getString(R.string.lab_detail_equal_release));
            }
            tv_release_detail.setText(entity.getLockedDays() + " " + getResources().getString(R.string.lab_detail_period_release));

            String format6 = new DecimalFormat("#0.00").format(entity.getLockedFee());
            tv_lock_cost.setText(format6);
            tv_release_times.setText(String.format(getResources().getString(R.string.lab_detail_release_multiple), "" + entity.getPriceScale()));

        } else {
            type = getResources().getString(R.string.lab_type_unknow);
        }
        purchaseType.setText(type);

        String ff = "#0.";
        for (int i = 0; i < entity.getPriceScale(); i++) {
            ff += "0";
        }
        String format3 = new DecimalFormat(ff).format(entity.getPrice() == null ? 0 : entity.getPrice());

        tv_cost.setText(format3 + "  " + entity.getAcceptUnit());

        tv_activity_currency.setText(entity.getUnit());
        tv_accept_currency.setText(entity.getAcceptUnit());

        tv_limit_num.setText(entity.getLimitTimes() == 0 ? "不限" : entity.getLimitTimes() + "" + getResources().getString(R.string.symbol));


        String limitamoubt;
        if (entity.getMinLimitAmout() == 0 && entity.getMaxLimitAmout() == 0) {
            limitamoubt = "不限";
        } else {
            String format4 = new DecimalFormat("#0.00").format(entity.getMinLimitAmout());
            String format5 = new DecimalFormat("#0.00").format(entity.getMaxLimitAmout());
            limitamoubt = format4 + " ~ " + format5;
        }
        tv_limit_amount.setText(limitamoubt);

        tv_start_time.setText(entity.getStartTime());
        tv_end_time.setText(entity.getEndTime());


        // ===================================================

        BigDecimal trade = new BigDecimal(entity.getTradedAmount());
        BigDecimal total = new BigDecimal(entity.getTotalSupply());

//        if (entity.getStep() == 0) {
        tv_bought_amount.setText(new DecimalFormat("#0.00").format(entity.getTradedAmount()) + " " + entity.getUnit());
        tv_remaining_amount.setText(new DecimalFormat("#0.00").format(total.subtract(trade)) + " " + entity.getUnit());
//        } else {
//            buy_status.setVisibility(View.GONE);
//        }

        // =====================================================


        if (entity.getStep() == 2) {
            tv_cc_amount.setText(new DecimalFormat("#0.00").format(trade) + " " + entity.getUnit());
            tv_tcc_amount.setText(new DecimalFormat("#0.00").format(total.subtract(trade)) + " " + entity.getUnit());
        } else {
            cc_status.setVisibility(View.GONE);
        }

        if (entity.getFreezeAmount() == 0) {
            tv_my_amount.setText("0 " + entity.getUnit());
        } else {
            BigDecimal a = new BigDecimal(entity.getTotalSupply());
            BigDecimal b = new BigDecimal(entity.getFreezeAmount());
            BigDecimal divide = b.divide(a);
            BigDecimal multiply = divide.multiply(a);
            tv_my_amount.setText(WonderfulMathUtils.getRundNumber(multiply.doubleValue(), 2, null) + " " + entity.getUnit());
        }

        input_amount_unit.setText(entity.getUnit());
        my_available_amount.setText(WonderfulMathUtils.getRundNumber(OneFragment.available_amount, 2, null) + " USDT");

        Date endDate = WonderfulDateUtils.getDateTransformString(entity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        long endtime = endDate.getTime();
        long currenttime = System.currentTimeMillis();
        if (entity.getStep() == 1 && endtime > currenttime) {
            tvBuy.setEnabled(true);
        } else {
            tvBuy.setEnabled(false);
        }

        if (entity.getStep() == 0) {
            ll_input.setVisibility(View.GONE);
            cc_status.setVisibility(View.GONE);
        } else if (entity.getStep() == 2) {
            ll_input.setVisibility(View.GONE);
            buy_status.setVisibility(View.GONE);
            tv_accept_currency_title.setText(getResources().getString(R.string.lab_detail_hold_currency));
            ll_status_bottom.setVisibility(View.GONE);

        } else if (entity.getStep() == 1) {

            if (entity.getType() == 2) {
                tv_cost.setVisibility(View.VISIBLE);
                tv_cost_top.setVisibility(View.VISIBLE);
                ll_input.setVisibility(View.GONE);
                cc_status.setVisibility(View.GONE);
                buy_status.setVisibility(View.GONE);
            } else if (entity.getType() == 6) {
                ll_input.setVisibility(View.VISIBLE);
                cc_status.setVisibility(View.GONE);
                buy_status.setVisibility(View.GONE);
            } else if (entity.getType() == 1) {
                ll_input.setVisibility(View.GONE);
                cc_status.setVisibility(View.GONE);
                buy_status.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void loadData() {
        presenter.activityDetail(getToken(), entity.getId());
    }

    @Override
    public void setPresenter(LabDetailContract.Presenter presenter) {
        this.presenter = presenter;
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
    public void activityDetailSucess(LabDetailEntity entity) {

        detailEntity = entity;
        tvTitle.setText(entity.getTitle());
        title.setText(entity.getTitle());
        subtitle.setText(entity.getDetail());
        showJustText(content, entity.getContent());
        Glide.with(this).load(GlobalConstant.getGlobalImagePath(entity.getSmallImageUrl())).into(ivAverage);
        int process = 0;
        if (entity.getType() == 3) {
            if (entity.getStep() == 1) {
                process = 50;
            } else if (entity.getStep() == 2) {
                process = 75;
            } else if (entity.getStep() == 3) {
                process = 100;
            } else {
                process = 0;
            }
        } else {
            process = entity.getProgress();
        }

        mProcess.setProgress(process);
        String format = new DecimalFormat("#0.00").format(process);
        tvPercent.setText(format + "%");
        String format2 = new DecimalFormat("#0.00").format(entity.getTotalSupply());
        amount.setText(format2 + "  " + entity.getUnit());
        tv_amount2.setText(format2 + "  " + entity.getUnit());
        status.setText(tabs[entity.getStep()]);

        String type;
        if (entity.getType() == 1) {
            type = getResources().getString(R.string.lab_type1);
            tv_cost.setVisibility(View.VISIBLE);
            tv_cost_top.setVisibility(View.VISIBLE);
            buy_status.setVisibility(View.GONE);
//            ll_input.setVisibility(View.GONE);

        } else if (entity.getType() == 2) {
            type = getResources().getString(R.string.lab_type2);
            tv_cost.setVisibility(View.VISIBLE);
            tv_cost_top.setVisibility(View.VISIBLE);
//            ll_input.setVisibility(View.GONE);

        } else if (entity.getType() == 3) {
            type = getResources().getString(R.string.lab_type3);
        } else if (entity.getType() == 4) {
            type = getResources().getString(R.string.lab_type4);
            tv_cost.setVisibility(View.VISIBLE);
            tv_cost_top.setVisibility(View.VISIBLE);
            buy_status.setVisibility(View.VISIBLE);
        } else if (entity.getType() == 5) {
            type = getResources().getString(R.string.lab_type5);
            tv_cost.setVisibility(View.VISIBLE);
            tv_cost_top.setVisibility(View.VISIBLE);
            buy_status.setVisibility(View.VISIBLE);
            tv_activity_currency_title.setVisibility(View.GONE);
            tv_activity_currency.setVisibility(View.GONE);

        } else if (entity.getType() == 6) {
            type = getResources().getString(R.string.lab_type6);
            tv_release_type.setVisibility(View.VISIBLE);
            release_type_title.setVisibility(View.VISIBLE);
            tv_release_detail.setVisibility(View.VISIBLE);
            release_detail_title.setVisibility(View.VISIBLE);
            tv_lock_cost.setVisibility(View.VISIBLE);
            lock_cost_title.setVisibility(View.VISIBLE);
            tv_release_times.setVisibility(View.VISIBLE);
            release_times_title.setVisibility(View.VISIBLE);
            tv_activity_currency_title.setVisibility(View.GONE);
            tv_activity_currency.setVisibility(View.GONE);

            if (entity.getReleaseType() == 1) {
                tv_release_type.setText(getResources().getString(R.string.lab_detail_proportional_release));
            } else {
                tv_release_type.setText(getResources().getString(R.string.lab_detail_equal_release));
            }
            tv_release_detail.setText(entity.getLockedDays() + " " + getResources().getString(R.string.lab_detail_period_release));

            String format6 = new DecimalFormat("#0.00").format(entity.getLockedFee());
            tv_lock_cost.setText(format6);
            tv_release_times.setText(String.format(getResources().getString(R.string.lab_detail_release_multiple), "" + entity.getPriceScale()));

        } else {
            type = getResources().getString(R.string.lab_type_unknow);
        }
        purchaseType.setText(type);

        String ff = "#0.";
        for (int i = 0; i < entity.getPriceScale(); i++) {
            ff += "0";
        }
        String format3 = new DecimalFormat(ff).format(entity.getPrice() == null ? 0 : entity.getPrice());

        tv_cost.setText(format3 + "  " + entity.getAcceptUnit());

        tv_activity_currency.setText(entity.getUnit());
        tv_accept_currency.setText(entity.getAcceptUnit());

        tv_limit_num.setText(entity.getLimitTimes() == 0 ? "不限" : entity.getLimitTimes() + "" + getResources().getString(R.string.symbol));


        String limitamoubt;
        if (entity.getMinLimitAmout() == 0 && entity.getMaxLimitAmout() == 0) {
            limitamoubt = "不限";
        } else {
            String format4 = new DecimalFormat("#0.00").format(entity.getMinLimitAmout());
            String format5 = new DecimalFormat("#0.00").format(entity.getMaxLimitAmout());
            limitamoubt = format4 + " ~ " + format5;
        }
        tv_limit_amount.setText(limitamoubt);

        tv_start_time.setText(entity.getStartTime());
        tv_end_time.setText(entity.getEndTime());


        // ===================================================

        BigDecimal trade = new BigDecimal(entity.getTradedAmount());
        BigDecimal total = new BigDecimal(entity.getTotalSupply());

//        if (entity.getStep() == 0) {
        tv_bought_amount.setText(new DecimalFormat("#0.00").format(entity.getTradedAmount()) + " " + entity.getUnit());
        tv_remaining_amount.setText(new DecimalFormat("#0.00").format(total.subtract(trade)) + " " + entity.getUnit());
//        } else {
//            buy_status.setVisibility(View.GONE);
//        }

        // =====================================================


        if (entity.getStep() == 2) {
            tv_cc_amount.setText(new DecimalFormat("#0.00").format(trade) + " " + entity.getUnit());
            tv_tcc_amount.setText(new DecimalFormat("#0.00").format(total.subtract(trade)) + " " + entity.getUnit());
        } else {
            cc_status.setVisibility(View.GONE);
        }

        if (entity.getFreezeAmount() == 0) {
            tv_my_amount.setText("0 " + entity.getUnit());
        } else {
            BigDecimal a = new BigDecimal(entity.getTotalSupply());
            BigDecimal b = new BigDecimal(entity.getFreezeAmount());
            BigDecimal divide = b.divide(a);
            BigDecimal multiply = divide.multiply(a);
            tv_my_amount.setText(WonderfulMathUtils.getRundNumber(multiply.doubleValue(), 2, null) + " " + entity.getUnit());
        }

        input_amount_unit.setText(entity.getUnit());
        my_available_amount.setText(WonderfulMathUtils.getRundNumber(OneFragment.available_amount, 2, null) + " USDT");

        Date endDate = WonderfulDateUtils.getDateTransformString(entity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        long endtime = endDate.getTime();
        long currenttime = System.currentTimeMillis();
        if (entity.getStep() == 1 && endtime > currenttime) {
            tvBuy.setEnabled(true);
        } else {
            tvBuy.setEnabled(false);
        }

        if (entity.getStep() == 0) {
            ll_input.setVisibility(View.GONE);
            cc_status.setVisibility(View.GONE);
        } else if (entity.getStep() == 2) {
            ll_input.setVisibility(View.GONE);
            buy_status.setVisibility(View.GONE);
            tv_accept_currency_title.setText(getResources().getString(R.string.lab_detail_hold_currency));
            ll_status_bottom.setVisibility(View.GONE);

        } else if (entity.getStep() == 1) {

            if (entity.getType() == 2) {
                tv_cost.setVisibility(View.VISIBLE);
                tv_cost_top.setVisibility(View.VISIBLE);
                ll_input.setVisibility(View.GONE);
                cc_status.setVisibility(View.GONE);
                buy_status.setVisibility(View.GONE);
            } else if (entity.getType() == 6) {
                ll_input.setVisibility(View.VISIBLE);
                cc_status.setVisibility(View.GONE);
                buy_status.setVisibility(View.GONE);
            } else if (entity.getType() == 1) {
                ll_input.setVisibility(View.GONE);
                cc_status.setVisibility(View.GONE);
                buy_status.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void activityDetailFailed(int code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void activityAttendSucess() {
        presenter.activityDetail(getToken(), detailEntity.getId());
    }

    @Override
    public void activityAttendlFailed(int code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    private void showJustText(TextView textView, String source) {
        if (textView == null) {
            return;
        }
        // 溢出滚动
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        CharSequence charSequence;
        charSequence = Html.fromHtml(source, imageGetter, null);
        textView.setText(charSequence);
    }

    final Html.ImageGetter imageGetter = new Html.ImageGetter() {

        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            URL url;
            try {
                url = new URL(source);
                drawable = Drawable.createFromStream(url.openStream(), "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;
        }
    };

    public AlertDialog getInstance(Activity activity) {
        View contentView = View.inflate(activity, R.layout.activity_submit_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.custom_dialog).setView(contentView).create();
        dialog.show();

        EditText etphone = contentView.findViewById(R.id.et_phone);
        EditText etcode = contentView.findViewById(R.id.et_code);
        TextView tvclose = contentView.findViewById(R.id.tv_close);
        TextView tvCode = contentView.findViewById(R.id.tv_code);
        TextView tvsubmit = contentView.findViewById(R.id.tvBuy);

        User user = MyApplication.getApp().getCurrentUser();
        etphone.setText(user.getUsername());

        tvclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etcode.setText("3234");
            }
        });

        tvsubmit.setOnClickListener(view -> {
            presenter.activityAttend(getToken(), input_amount.getText().toString(), detailEntity.getId(), user.getUsername(), etcode.getText().toString());

        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.7);
        dialog.getWindow().setAttributes(layoutParams);
        return dialog;
    }
}