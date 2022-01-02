package cn.bizzan.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import cn.bizzan.R;
import cn.bizzan.base.BaseActivity;
import cn.bizzan.ui.home.MainActivity;

public class LanguageActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.llTitle)
    LinearLayout llTitle;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LanguageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        findViewById(R.id.btn_en).setOnClickListener(this);
        findViewById(R.id.btn_zh).setOnClickListener(this);
        findViewById(R.id.btn_de).setOnClickListener(this);
        findViewById(R.id.btn_fa).setOnClickListener(this);
        findViewById(R.id.btn_ja).setOnClickListener(this);
        findViewById(R.id.btn_ko).setOnClickListener(this);
        findViewById(R.id.btn_tr).setOnClickListener(this);
        findViewById(R.id.btn_th).setOnClickListener(this);
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_en:
                switchLanguage("en");
                break;
            case R.id.btn_zh:
                switchLanguage("ch");
                break;
            case R.id.btn_de:
                switchLanguage("de");
                break;
            case R.id.btn_fa:
                switchLanguage("fa");
                break;
            case R.id.btn_ja:
                switchLanguage("ja");
                break;
            case R.id.btn_ko:
                switchLanguage("ko");
                break;
            case R.id.btn_tr:
                switchLanguage("tr");
                break;
            case R.id.btn_th:
                switchLanguage("th");
                break;
        }

        //更新语言后，destroy当前页面，重新绘制
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }
}
