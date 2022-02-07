package cn.bizzan.ui.recharge;

import static cn.bizzan.app.GlobalConstant.JSON_ERROR;
import static cn.bizzan.app.GlobalConstant.OKHTTP_ERROR;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.gyf.barlibrary.ImmersionBar;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import cn.bizzan.R;
import cn.bizzan.app.GlobalConstant;
import cn.bizzan.app.MyApplication;
import cn.bizzan.base.BaseActivity;
import cn.bizzan.data.DataSource;
import cn.bizzan.entity.CTCOrderDetail;
import cn.bizzan.entity.Coin;
import cn.bizzan.app.UrlFactory;
import cn.bizzan.entity.CoinAddress;
import cn.bizzan.ui.dialog.HeaderSelectDialogFragment;
import cn.bizzan.ui.myinfo.MyInfoActivity;
import cn.bizzan.ui.set_lock.SetLockActivity;
import cn.bizzan.utils.SharedPreferenceInstance;
import cn.bizzan.utils.WonderfulBitmapUtils;
import cn.bizzan.utils.WonderfulCodeUtils;
import cn.bizzan.utils.WonderfulCommonUtils;
import cn.bizzan.utils.WonderfulFileUtils;
import cn.bizzan.utils.WonderfulLogUtils;
import cn.bizzan.utils.WonderfulPermissionUtils;
import cn.bizzan.utils.WonderfulStringUtils;
import cn.bizzan.utils.WonderfulToastUtils;
import cn.bizzan.utils.WonderfulUriUtils;
import cn.bizzan.utils.okhttp.StringCallback;
import cn.bizzan.utils.okhttp.WonderfulOkhttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import okhttp3.Request;

public class RechargeActivity extends BaseActivity implements HeaderSelectDialogFragment.OperateCallback {

    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvAddressText)
    TextView tvAddressText;
    @BindView(R.id.ivAddress)
    ImageView ivAddress;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.llAlbum)
    LinearLayout llAlbum;
    @BindView(R.id.layout_memo)
    LinearLayout layout_memo;
    @BindView(R.id.llCopy)
    LinearLayout llCopy;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.text_deposit)
    TextView text_deposit;
    @BindView(R.id.text_memo)
    TextView text_memo;
    @BindView(R.id.usdt_erc)
    TextView text_erc;
    @BindView(R.id.usdt_trc)
    TextView text_trc;
    @BindView(R.id.usdt_group)
    LinearLayout ll_usdt;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.ll_recharge_type1)
    LinearLayout llRechargeType1;
    @BindView(R.id.et_amount)
    EditText et_amount;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.upload_img)
    ImageView upload_img;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    private boolean isTRC;
    private String address;
    private String trcAddress;

    private Coin coin;
    private Bitmap saveBitmap;
    @BindView(R.id.view_back)
    View view_back;
    private int biaoshi = 0;

    private File imageFile;
    private String filename = "address.jpg";
    private Uri imageUri;
    private String url;
    private HeaderSelectDialogFragment headerSelectDialogFragment;

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_CAMERA:
                    startCamera();
                    break;
                case GlobalConstant.PERMISSION_STORAGE:
                    chooseFromAlbum();
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_CAMERA:
                    WonderfulToastUtils.showToast(WonderfulToastUtils.getString(RechargeActivity.this, R.string.camera_permission));
                    break;
                case GlobalConstant.PERMISSION_STORAGE:
                    WonderfulToastUtils.showToast(WonderfulToastUtils.getString(RechargeActivity.this, R.string.storage_permission));
                    break;
            }
        }
    };

    public static void actionStart(Context context, Coin coin) {
        Intent intent = new Intent(context, RechargeActivity.class);
        intent.putExtra("coin", coin);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        imageFile = WonderfulFileUtils.getCacheSaveFile(this, filename);

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
        llCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy();
            }
        });
        llAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        this.coin = (Coin) getIntent().getSerializableExtra("coin");
        if (TextUtils.equals(this.coin.getCoin().getUnit(), "USDT")) {
            ll_usdt.setVisibility(View.VISIBLE);
        }

        tvAddress.setText(WonderfulToastUtils.getString(this, R.string.now_link) + this.coin.getCoin().getUnit() + WonderfulToastUtils.getString(this, R.string.node) + "...");
        text_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy2();
            }
        });

        text_erc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTRC = false;
                text_erc.setBackgroundResource(R.color.text_orange);
                text_trc.setBackgroundResource(R.color.transparent);

                if (!TextUtils.isEmpty(address)) {
                    loadAddress(address);
                }
                displayLoadingPopup();
                createAddress(isTRC);
            }
        });

        text_trc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTRC = true;
                text_trc.setBackgroundResource(R.color.text_orange);
                text_erc.setBackgroundResource(R.color.transparent);

                if (!TextUtils.isEmpty(trcAddress)) {
                    loadAddress(trcAddress);
                }
                displayLoadingPopup();
                createAddress(isTRC);
            }
        });

        upload_img.setOnClickListener(view -> {
            showHeaderSelectDialog();
        });


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberDeposit();
            }
        });
    }

    private void showHeaderSelectDialog() {
        if (headerSelectDialogFragment == null)
            headerSelectDialogFragment = HeaderSelectDialogFragment.getInstance(this);
        headerSelectDialogFragment.show(getSupportFragmentManager(), "header_select");
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (biaoshi == 0) {
            biaoshi = 1;
            if (coin.getCoin().getAccountType() == 0) {
                if (coin.getAddr() == null || coin.getAddr().length == 0) {
                    displayLoadingPopup();
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        layout_memo.setVisibility(View.GONE);
                                        if (GlobalConstant.isUdun()) {
                                            createAddress(isTRC);
                                        } else {
                                            huoqu();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    };
                    timer.schedule(task, 5000);
                } else {
                    try {
                        layout_memo.setVisibility(View.GONE);
                        text_deposit.setText(String.format("• " + WonderfulToastUtils.getString(this, R.string.rush1) + "：%s %s，" + WonderfulToastUtils.getString(this, R.string.rush2) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush3) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush4) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush5) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush6) + "", coin.getCoin().getMinRechargeAmount(), coin.getCoin().getUnit()));
                        erciLoad(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (coin.getCoin().getDepositAddress() == null || coin.getCoin().getDepositAddress().equals("")) {
                    WonderfulToastUtils.showToast(WonderfulToastUtils.getString(this, R.string.no_rush));
                } else {
                    layout_memo.setVisibility(View.GONE);
                    text_deposit.setText(String.format("• " + WonderfulToastUtils.getString(this, R.string.rush1) + "：%s %s，" + WonderfulToastUtils.getString(this, R.string.rush2) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush3) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush4) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush5) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush6) + "", coin.getCoin().getMinRechargeAmount(), coin.getCoin().getUnit()));
                    erciLoad(0);
                }
            }
        }

    }

    private void save() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ATC/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (saveBitmap != null) try {
            WonderfulBitmapUtils.saveBitmapToFile(saveBitmap, file, 100);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        WonderfulToastUtils.showToast(WonderfulToastUtils.getString(this, R.string.save_ok));
    }

    private void huoqu() {
        final String formatStr = "• " + WonderfulToastUtils.getString(this, R.string.rush1) + "：%s %s，" + WonderfulToastUtils.getString(this, R.string.rush2) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush3) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush4) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush5) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush6) + "";
        WonderfulOkhttpUtils.post().url(UrlFactory.getWalletUrl()).addHeader("x-auth-token", SharedPreferenceInstance.getInstance().getTOKEN()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取所有钱包出错", "获取所有钱包出错：" + e.getMessage());
                hideLoadingPopup();
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取所有钱包回执：", "获取所有钱包回执：" + response.toString());
                hideLoadingPopup();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Coin> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Coin>>() {
                        }.getType());
                        for (int i = 0; i < objs.size(); i++) {
                            Coin coin1 = objs.get(i);
                            WonderfulLogUtils.logi("miao", coin1.getId() + "-----" + coin.getId());
                            if (coin.getId() == coin1.getId()) {
//                                WonderfulLogUtils.logi("miao", coin1.getAddress() + "-----");

                                String[] addr = coin1.getCoin().getDepositAddress().split(",");
                                setupTabLayout(addr);

                                if (coin1.getCoin().getAccountType() == 1) {
                                    // EOS类型账户地址
//                                    coin.setAddress(coin1.getCoin().getDepositAddress());
//                                    coin.setAddr(addr);
                                    layout_memo.setVisibility(View.VISIBLE);
                                    text_memo.setText(coin.getMemo());
                                    text_deposit.setText(String.format(formatStr, coin1.getCoin().getMinRechargeAmount(), coin1.getCoin().getUnit()));
                                    break;
                                } else {
//                                    coin.setAddress(coin1.getAddress());
//                                    coin.setAddress(coin1.getCoin().getDepositAddress());
//                                    coin.setAddr(addr);
                                    layout_memo.setVisibility(View.GONE);
                                    text_deposit.setText(String.format(formatStr, coin1.getCoin().getMinRechargeAmount(), coin1.getCoin().getUnit()));
                                    break;
                                }
                            }
                        }
                        if (coin.getAddr() == null || coin.getAddr().length == 0) {
                            WonderfulToastUtils.showToast(WonderfulToastUtils.getString(RechargeActivity.this, R.string.create_rush));
                        }
                    } else {
                        WonderfulToastUtils.showToast("" + object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setupTabLayout(String[] addr) {
        if (addr != null && addr.length > 1) {

            tabLayout.setVisibility(View.VISIBLE);
            llRechargeType1.setVisibility(View.GONE);
            String[] address = new String[addr.length];

            for (int i = 0; i < addr.length; i++) {
                String[] at = addr[i].split(":");
                tabLayout.addTab(tabLayout.newTab().setText(at[0]));
                address[i] = at[1];
            }

            coin.setAddr(address);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    erciLoad(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            tabLayout.getTabAt(0).select();
        } else {
            coin.setAddr(addr);
        }
        erciLoad(0);
    }

    private void createAddress(final boolean isTrc) {
        String symbol;
        if (isTrc) {
            symbol = "TRCUSDT";
        } else {
            symbol = coin.getCoin().getUnit();
        }

        WonderfulOkhttpUtils.get().url(UrlFactory.createAddress())
                .addParams("x-auth-token", SharedPreferenceInstance.getInstance().getTOKEN())
                .addParams("symbol", symbol)
                .addParams("memberId", "" + MyApplication.getApp().getCurrentUser().getId())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("create address 出错", "create address出错：" + e.getMessage());
                hideLoadingPopup();
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取create address：", "获取create address：" + response.toString());
                hideLoadingPopup();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 200) {

                        CoinAddress obj = gson.fromJson(object.getJSONObject("data").toString(), CoinAddress.class);

                        if (isTrc) {
                            trcAddress = obj.getAddress();
                        } else {
                            address = obj.getAddress();
                        }

                        loadAddress(obj.getAddress());

                        if (coin.getAddr() == null || coin.getAddr().length == 0) {
                            WonderfulToastUtils.showToast(WonderfulToastUtils.getString(RechargeActivity.this, R.string.create_rush));
                        }

                    } else {
                        WonderfulToastUtils.showToast("" + object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void copy() {
        WonderfulCommonUtils.copyText(this, tvAddress.getText().toString());
        WonderfulToastUtils.showToast(R.string.copy_success);
    }

    private void copy2() {
        WonderfulCommonUtils.copyText(this, text_memo.getText().toString());
        WonderfulToastUtils.showToast(R.string.copy_success);
    }

    private void loadAddress(String address) {
        final String formatStr = "• " + WonderfulToastUtils.getString(this, R.string.rush1) + "：%s %s，" + WonderfulToastUtils.getString(this, R.string.rush2) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush3) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush4) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush5) + "\n• " + WonderfulToastUtils.getString(this, R.string.rush6) + "";
        if (coin.getCoin().getAccountType() == 1) {
            // EOS类型账户地址
//            coin.setAddress(address);
            coin.setAddr(address.split(","));

            layout_memo.setVisibility(View.VISIBLE);
            text_memo.setText(coin.getMemo());
            text_deposit.setText(String.format(formatStr, coin.getCoin().getMinRechargeAmount(), coin.getCoin().getUnit()));
            erciLoad(0);

        } else {
//            coin.setAddress(address);
            coin.setAddr(address.split(","));
            layout_memo.setVisibility(View.GONE);
            text_deposit.setText(String.format(formatStr, coin.getCoin().getMinRechargeAmount(), coin.getCoin().getUnit()));
            erciLoad(0);
        }
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    private void erciLoad(int pos) {
        if (tvTitle == null) {
            return;
        }

        tvTitle.setText(coin.getCoin().getUnit() + WonderfulToastUtils.getString(this, R.string.chargeMoney));
        tvAddressText.setText(coin.getCoin().getUnit() + WonderfulToastUtils.getString(this, R.string.rush_address));
        if (coin.getCoin().getAccountType() == 0) {
            tvAddress.setText(coin.getAddr()[pos]);
            layout_memo.setVisibility(View.GONE);
        } else {
//            tvAddress.setText(coin.getCoin().getDepositAddress());
            tvAddress.setText(coin.getAddr()[pos]);
            layout_memo.setVisibility(View.VISIBLE);
            text_memo.setText(coin.getMemo());
        }

        if (coin == null) {
            tvAddress.setText(WonderfulToastUtils.getString(this, R.string.unChargeMoneyTip1));
            return;
        }
        if (coin.getCoin() == null) {
            tvAddress.setText(WonderfulToastUtils.getString(this, R.string.unChargeMoneyTip1));
            return;
        }

//        if (coin.getCoin().getAccountType() == 0 && coin.getAddr() == null) {
//            tvAddress.setText(WonderfulToastUtils.getString(this, R.string.unChargeMoneyTip1));
//            return;
//        }
//
//        if (coin.getCoin().getAccountType() == 1 && coin.getCoin().getDepositAddress() == null) {
//            tvAddress.setText(WonderfulToastUtils.getString(this, R.string.unChargeMoneyTip1));
//            return;
//        }

        if (coin.getAddr() == null || coin.getAddr().length == 0) {
            tvAddress.setText(WonderfulToastUtils.getString(this, R.string.unChargeMoneyTip1));
            return;
        }

        ivAddress.post(new Runnable() {
            @Override
            public void run() {
//                if (coin.getCoin().getAccountType() == 0) {
//                    if (WonderfulStringUtils.isEmpty(coin.getAddr()[pos])) return;
//                    saveBitmap = createQRCode(coin.getAddr()[pos], Math.min(ivAddress.getWidth(), ivAddress.getHeight()));
//                }
//                if (coin.getCoin().getAccountType() == 1) {
//                    if (WonderfulStringUtils.isEmpty(coin.getAddr()[pos])) return;
//                    saveBitmap = createQRCode(coin.getAddr()[pos], Math.min(ivAddress.getWidth(), ivAddress.getHeight()));
//                }

                if (WonderfulStringUtils.isEmpty(coin.getAddr()[pos])) return;
                saveBitmap = createQRCode(coin.getAddr()[pos], Math.min(ivAddress.getWidth(), ivAddress.getHeight()));
                ivAddress.setImageBitmap(saveBitmap);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    public static Bitmap createQRCode(String text, int size) {
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 2);   //设置白边大小 取值为 0- 4 越大白边越大
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * size + x] = 0xff000000;
                    } else {
                        pixels[y * size + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void memberDeposit() {

        if (TextUtils.isEmpty(url)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.recharge_upload_failed));
            return;
        }

        String amount = et_amount.getText().toString();
        if (new BigDecimal(amount).doubleValue() < coin.getCoin().getMinRechargeAmount()) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.recharge_min_limits));
            return;
        }

        String address2 = et_address.getText().toString();
        String address = tvAddress.getText().toString();
        displayLoadingPopup();
        WonderfulOkhttpUtils.get().url(UrlFactory.getmemberDepositAddUrl())
                .addParams("x-auth-token", getToken())
                .addParams("unit", coin.getCoin().getUnit())
                .addParams("amount", amount)
                .addParams("address", address)
                .addParams("url", url)
                .addParams("address2", address2)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("DepositAdd 出错", "DepositAdd出错：" + e.getMessage());
                hideLoadingPopup();
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取DepositAdd：", "获取DepositAdd：" + response.toString());
                hideLoadingPopup();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {

                        WonderfulToastUtils.showToast(getResources().getString(R.string.recharge_success));
                    } else {
                        WonderfulToastUtils.showToast("" + object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void toTakePhoto() {
        if (!WonderfulPermissionUtils.isCanUseCamera(this))
            checkPermission(GlobalConstant.PERMISSION_CAMERA, Permission.CAMERA);
        else startCamera();
    }

    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

    @Override
    public void toChooseFromAlbum() {
        if (!WonderfulPermissionUtils.isCanUseStorage(this))
            checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
        else chooseFromAlbum();
    }

    private void startCamera() {
        if (imageFile == null) {
            WonderfulToastUtils.showToast(WonderfulToastUtils.getString(this, R.string.unknown_error));
            return;
        }
        imageUri = WonderfulFileUtils.getUriForFile(this, imageFile);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, GlobalConstant.TAKE_PHOTO);
    }

    private void chooseFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, GlobalConstant.CHOOSE_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GlobalConstant.TAKE_PHOTO:
                takePhotoReturn(resultCode, data);
                break;
            case GlobalConstant.CHOOSE_ALBUM:
                choseAlbumReturn(resultCode, data);
                break;
            default:
        }
    }

    private void takePhotoReturn(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        Bitmap bitmap = WonderfulBitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), upload_img.getWidth(), upload_img.getHeight());
        upload_img.setImageBitmap(bitmap);
        uploadBase64Pic(getToken(), filename, filename, imageFile);
    }

    private void choseAlbumReturn(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        imageUri = data.getData();
        if (Build.VERSION.SDK_INT >= 19)
            imageFile = WonderfulUriUtils.getUriFromKitKat(this, imageUri);
        else
            imageFile = WonderfulUriUtils.getUriBeforeKitKat(this, imageUri);
        if (imageFile == null) {
            WonderfulToastUtils.showToast(WonderfulToastUtils.getString(this, R.string.library_file_exception));
            return;
        }
        Bitmap bitmap = WonderfulBitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), upload_img.getWidth(), upload_img.getHeight());
        upload_img.setImageBitmap(bitmap);
        uploadBase64Pic(MyApplication.getApp().getCurrentUser().getToken(), filename, filename, imageFile);
    }

    public void uploadBase64Pic(String token, String name, String filename, File file) {
        url = null;
        displayLoadingPopup();
        WonderfulOkhttpUtils.post().url(UrlFactory.getUploadPicUrl()).addHeader("x-auth-token", token)
                .addFile(name, filename, file)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("上传图片出错", "上传图片出错：" + e.getMessage());
                hideLoadingPopup();
                WonderfulCodeUtils.checkedErrorCode(RechargeActivity.this, OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("上传图片回执：", "上传图片回执：" + response.toString());
                hideLoadingPopup();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        url = object.optString("data");
//                        Glide.with(RechargeActivity.this).load(GlobalConstant.getGlobalImagePath(url)).into(upload_img);
                    } else {
                        WonderfulCodeUtils.checkedErrorCode(RechargeActivity.this, object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
