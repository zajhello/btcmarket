package cn.bizzan.ui.mylab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import cn.bizzan.R;
import cn.bizzan.base.BaseTransFragmentActivity;
import cn.bizzan.ui.home.LabFragment;

public class MyLibActivity extends BaseTransFragmentActivity {

    @BindView(R.id.llTitle)
    LinearLayout llTitle;

    private LabFragment mLabFragment;

    //重载启动方法 在K线 页面用到
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyLibActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_lib;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        showFragment(mLabFragment);
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
    protected void recoverFragment() {
        mLabFragment = (LabFragment) getSupportFragmentManager().findFragmentByTag(LabFragment.TAG);
        fragments.add(mLabFragment);
    }

    @Override
    protected void initFragments() {
        if (mLabFragment == null) fragments.add(mLabFragment = LabFragment.getInstance());
    }

    @Override
    public int getContainerId() {
        return R.id.content;
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