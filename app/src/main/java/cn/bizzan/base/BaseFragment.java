package cn.bizzan.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bizzan.events.CurrencyEvent;

/**
 * Created by Administrator on 2017/9/26.
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;
    Unbinder unbinder;
    protected ImmersionBar immersionBar;
    protected boolean isInit = false;
    protected boolean isNeedLoad = true;
    protected boolean isSetTitle = false;
    protected boolean isSupportEventBus = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        isInit = true;

        if (isSupportEventBus && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        initViews(savedInstanceState);
        obtainData();
        fillWidget();
    }

    protected abstract int getLayoutId();

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void obtainData();

    protected abstract void fillWidget();

    protected abstract void loadData();

    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        immersionBar = ImmersionBar.with(this);
        //immersionBar.keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).statusBarDarkFont(false,0.2f).flymeOSStatusBarFontColor(R.color.help_view).init();
    }

    public static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void displayLoadingPopup() {
        if (getActivity() != null) ((BaseActivity) getActivity()).displayLoadingPopup();
    }

    public void hideLoadingPopup() {
        if (getActivity() != null) ((BaseActivity) getActivity()).hideLoadingPopup();
    }

    @Override
    public View getView() {
        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (isSupportEventBus && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (immersionBar != null) immersionBar.destroy();
        unbinder.unbind();
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCurrencyEvent(CurrencyEvent event) {
        // Do something
        onCurrencyChangedEvent();
    }

    public void onCurrencyChangedEvent() {

    }

    protected void finish() {
        getActivity().finish();
    }

    public BaseActivity getmActivity() {
        return (BaseActivity) super.getActivity();
    }
}
