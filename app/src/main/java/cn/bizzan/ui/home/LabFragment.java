package cn.bizzan.ui.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bizzan.R;
import cn.bizzan.adapter.PagerAdapter;
import cn.bizzan.base.BaseNestingTransFragment;
import cn.bizzan.customview.intercept.WonderfulViewPager;
import cn.bizzan.ui.forgot_pwd.PhoneForgotFragment;

public class LabFragment extends BaseNestingTransFragment {

    public static final String TAG = TwoFragment.class.getSimpleName();
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ibMessage)
    ImageButton ibMessage;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vpPager)
    WonderfulViewPager vpPager;
    @BindArray(R.array.home_laboratory_tab)
    String[] tabs;

    LabChildFragment allF;
    LabChildFragment toStartF;
    LabChildFragment runingF;
    LabChildFragment deliverF;
    LabChildFragment completedF;

    Unbinder unbinder;

    public static LabFragment getInstance() {
        LabFragment phoneForgotFragment = new LabFragment();
        return phoneForgotFragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_two;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (savedInstanceState != null) recoveryFragments();
        vpPager.setOffscreenPageLimit(3);
        List<String> tabs = Arrays.asList(this.tabs);
        vpPager.setAdapter(new PagerAdapter(getChildFragmentManager(), fragments, tabs));
        tab.setupWithViewPager(vpPager, true);
        tab.getTabAt(0).select();
        ibMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tvTitle.setText(getResources().getText(R.string.lab_title));
        llTitle.setVisibility(View.GONE);
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
    protected void addFragments() {
        if (allF == null) fragments.add(allF = LabChildFragment.getInstance(0));
        if (toStartF == null) fragments.add(toStartF = LabChildFragment.getInstance(1));
        if (runingF == null) fragments.add(runingF = LabChildFragment.getInstance(2));
        if (deliverF == null) fragments.add(deliverF = LabChildFragment.getInstance(3));
        if (completedF == null) fragments.add(completedF = LabChildFragment.getInstance(4));
    }

    @Override
    protected void recoveryFragments() {
        allF = (LabChildFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(vpPager.getId(), 0));
        toStartF = (LabChildFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(vpPager.getId(), 1));
        runingF = (LabChildFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(vpPager.getId(), 2));
        deliverF = (LabChildFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(vpPager.getId(), 3));
        completedF = (LabChildFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(vpPager.getId(), 4));

        fragments.add(allF);
        fragments.add(toStartF);
        fragments.add(runingF);
        fragments.add(deliverF);
        fragments.add(completedF);
    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
