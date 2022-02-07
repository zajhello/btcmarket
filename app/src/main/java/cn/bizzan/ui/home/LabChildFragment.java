package cn.bizzan.ui.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bizzan.R;
import cn.bizzan.adapter.LabAdapter;
import cn.bizzan.app.Injection;
import cn.bizzan.base.BaseNestingTransFragment;
import cn.bizzan.entity.LabEntity;
import cn.bizzan.ui.home.presenter.LabPresenter;
import cn.bizzan.ui.mylab.LabDetaiActivity;
import cn.bizzan.utils.WonderfulLogUtils;

public class LabChildFragment extends BaseNestingTransFragment implements MainContract.LabView {

    public static final String TAG = TwoFragment.class.getSimpleName();

    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private LabAdapter adapter;

    Unbinder unbinder;

    private int page = 1;
    private int pageSize = 10;
    private int type;

    private MainContract.LabPresenter presenter;

    public static LabChildFragment getInstance(int type) {
        LabChildFragment fragment = new LabChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_usdt_market;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        new LabPresenter(Injection.provideTasksRepository(getActivity()), this);

        adapter = new LabAdapter(new ArrayList<>(), getmActivity());
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContent.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, rvContent);



        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.setEnableLoadMore(false);
                adapter.loadMoreEnd(true);

                page = 1;
                WonderfulLogUtils.logi("miao", "2333");
                presenter.getLabList(getmActivity().getToken(), type, page, pageSize, false);

            }
        });
    }

    @Override
    protected void obtainData() {
        displayLoadingPopup();
        adapter.setEnableLoadMore(false);
        adapter.loadMoreEnd(true);
        page = 1;
        type = type - 1;
        presenter.getLabList(getmActivity().getToken(), type, page, pageSize, false);

    }

    private void loadMore() {
        refreshLayout.setEnabled(false);
        page = page + 1;
        presenter.getLabList(getmActivity().getToken(), type, page, pageSize, true);
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void addFragments() {

    }

    @Override
    protected void recoveryFragments() {

    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    @Override
    public void setPresenter(MainContract.LabPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void labListSuccess(List<LabEntity> labs, boolean loadmore) {

        if (labs.size() < pageSize) {
            Log.i("Trust", "PageSize大于listsize");
            adapter.setEnableLoadMore(false);
            adapter.loadMoreEnd(true);
        } else {
            adapter.setEnableLoadMore(true);
        }
        adapter.disableLoadMoreIfNotFullPage();

        if (loadmore) {
            adapter.addData(labs);
            adapter.loadMoreComplete();
            refreshLayout.setEnabled(true);
        } else {
            refreshLayout.setRefreshing(false);
            adapter.setNewData(labs);
        }
    }

    @Override
    public void labListFailed(boolean loadmore) {

        if (loadmore) {
            adapter.loadMoreComplete();
            refreshLayout.setEnabled(true);
        }
        refreshLayout.setRefreshing(false);
        adapter.disableLoadMoreIfNotFullPage();
    }
}
