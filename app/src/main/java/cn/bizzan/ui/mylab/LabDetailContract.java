package cn.bizzan.ui.mylab;

import cn.bizzan.base.Contract;
import cn.bizzan.entity.LabDetailEntity;
import cn.bizzan.entity.SafeSetting;
import cn.bizzan.ui.myinfo.MyInfoContract;

public interface LabDetailContract {

    interface View extends Contract.BaseView<LabDetailContract.Presenter> {

        void activityDetailSucess(LabDetailEntity entity);

        void activityDetailFailed(int code, String toastMessage);

        void activityAttendSucess();

        void activityAttendlFailed(int code, String toastMessage);

        void phoneCodeSuccess(String obj);

        void phoneCodeFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void phoneCode(String phone, String country);

        void activityDetail(String token, int id);

        void activityAttend(String token, String amount, int activityId, String aims, String code);
    }
}
