package cn.bizzan.ui.myinfo;


import java.io.File;

import cn.bizzan.base.Contract;
import cn.bizzan.data.DataSource;
import cn.bizzan.entity.SafeSetting;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface MyInfoContract {
    interface View extends Contract.BaseView<Presenter> {

        void safeSettingSuccess(SafeSetting obj);

        void safeSettingFail(Integer code, String toastMessage);

        void uploadBase64PicSuccess(String obj);

        void uploadBase64PicFail(Integer code, String toastMessage);

        void avatarSuccess(String obj);

        void avatarFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void safeSetting(String token);

        void uploadBase64Pic( String token, String name, String filename, File file);

        void avatar(String token, String url);
    }
}
