package cn.bizzan.ui.mylab;

import cn.bizzan.data.DataSource;
import cn.bizzan.entity.LabDetailEntity;
import cn.bizzan.entity.SafeSetting;

public class LabDetailPresenter implements LabDetailContract.Presenter {

    private final DataSource dataRepository;
    private final LabDetailContract.View view;

    public LabDetailPresenter(DataSource dataRepository, LabDetailContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void phoneCode(String phone, String country) {
        dataRepository.phoneCodeLab(phone, country, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.phoneCodeSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.phoneCodeFail(code, toastMessage);
            }
        });
    }

    @Override
    public void activityDetail(String token, int id) {
        view.displayLoadingPopup();
        dataRepository.activityDetail(token, id, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.activityDetailSucess((LabDetailEntity) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.activityDetailFailed(code, toastMessage);
            }
        });
    }

    @Override
    public void activityAttend(String token, String amount, int activityId, String aims, String code) {
        view.displayLoadingPopup();
        dataRepository.activityAttend(token, amount, activityId, aims, code, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.activityAttendSucess();
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.activityAttendlFailed(code, toastMessage);
            }
        });
    }
}
