package cn.bizzan.ui.kline;


import cn.bizzan.base.Contract;
import cn.bizzan.entity.Currency;
import cn.bizzan.ui.mychart.KLineBean;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public interface KlineContract {

    interface View extends Contract.BaseView<Presenter> {

        void KDataFail(Integer code, String toastMessage);

        void KDataSuccess(JSONArray obj);
        void KDataSuccess2(JSONArray obj);

        void KDataSuccess(KLineBean data);
        void KDataSuccess2(KLineBean data);

        void allCurrencySuccess(List<Currency> obj);

        void allCurrencyFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void KData(String symbol, Long from, Long to, String resolution,String type);

        void KData_Constract(String symbol, Long from, Long to, String resolution,String type);

        //k线数据
        void KData_Constract(String pushmsg,String type);

        void allCurrency();

    }

    interface MinuteView extends Contract.BaseView<MinutePresenter> {

        void allCurrencySuccess(List<Currency> obj);

        void allCurrencyFail(Integer code, String toastMessage);

        void KDataSuccess(JSONArray obj);

        void KDataFail(Integer code, String toastMessage);
    }

    interface MinutePresenter extends Contract.BasePresenter {

        void allCurrency();

        void KData(String symbol, long from, Long to, String resolution);
    }
}
