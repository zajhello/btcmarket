package cn.bizzan.ui.currency_list;


import java.util.List;
import java.util.Map;

import cn.bizzan.base.Contract;
import cn.bizzan.entity.Country;
import cn.bizzan.entity.CurrencyEntity;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface CurrencyListContract {
    interface View extends Contract.BaseView<Presenter> {

        void CurrencyListSuccess(Map<String, CurrencyEntity> obj);

        void CurrencyListFail(Integer code, String toastMessage);

        void CurrencyRateSuccess(double obj);

        void CurrencyRatetFail(Integer code, String toastMessage);

    }

    interface Presenter extends Contract.BasePresenter {

        void CurrencyList();

        void CurrentRate(String key);
    }
}
