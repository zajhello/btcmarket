package cn.bizzan.ui.kline;


import static cn.bizzan.app.GlobalConstant.JSON_ERROR;

import cn.bizzan.data.DataSource;
import cn.bizzan.entity.Currency;
import cn.bizzan.ui.mychart.KLineBean;
import cn.bizzan.utils.WonderfulLogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public class KlinePresenter implements KlineContract.Presenter {
    private DataSource dataRepository;
    private KlineContract.View view;

    public KlinePresenter(DataSource dataRepository, KlineContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    //type  1 表示第一次请求  2表示加载数据
    @Override
    public void KData(String symbol, Long from, Long to, String resolution, final String type) {
        dataRepository.KData(symbol, from, to, resolution, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                if (type.equals("1")){
                    view.KDataSuccess((JSONArray) obj);
                }else if (type.equals("2")){
                    view.KDataSuccess2((JSONArray) obj);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.KDataFail(code, toastMessage);
            }
        });
    }


    @Override
    public void KData_Constract(String symbol, Long from, Long to, String resolution, final String type) {
        dataRepository.KData_Constract(symbol, from, to, resolution, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                if (type.equals("1")){
                    view.KDataSuccess((JSONArray) obj);
                }else if (type.equals("2")){
                    view.KDataSuccess2((JSONArray) obj);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.KDataFail(code, toastMessage);
            }
        });
    }

    @Override
    public void KData_Constract(String pushmsg,String type) {
        WonderfulLogUtils.logi("期权历史K线数据回执：", "期权历史K线数据回执：" + pushmsg);
        if (view == null) return;
        try {
            JSONObject jsonArray = new JSONObject(pushmsg);
            KLineBean kline = new KLineBean(jsonArray.getString("time"),
                    Float.valueOf(jsonArray.getString("openPrice")),
                    Float.valueOf(jsonArray.getString("closePrice")),
                    Float.valueOf(jsonArray.getString("highestPrice")),
                    Float.valueOf(jsonArray.getString("lowestPrice")),
                    Float.valueOf(jsonArray.getString("volume")));

            if (type.equals("1")) {
                view.KDataSuccess(kline);
            } else if (type.equals("2")) {
                view.KDataSuccess2(kline);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void allCurrency() {
        dataRepository.allCurrency(new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.allCurrencySuccess((List<Currency>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.allCurrencyFail(code, toastMessage);

            }
        });
    }
}
