package cn.bizzan.ui.home.presenter;

import java.util.List;

import cn.bizzan.data.DataSource;
import cn.bizzan.entity.EntrustHistory;
import cn.bizzan.entity.LabEntity;
import cn.bizzan.ui.home.MainContract;
import cn.bizzan.utils.WonderfulLogUtils;

public class LabPresenter implements MainContract.LabPresenter {
    private MainContract.LabView view;
    private DataSource dataRepository;

    public LabPresenter(DataSource dataRepository, MainContract.LabView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void getLabList(String token, int step, int pageNo, int pageSize, boolean loadmore) {
        dataRepository.labList(token, step, pageNo, pageSize, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.labListSuccess((List<LabEntity>) obj, loadmore);
                WonderfulLogUtils.logi("getEntrustHistory", "obj     " + obj.toString());
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.labListFailed(loadmore);
            }
        });
    }
}
