package cn.bizzan.ui.currency_list;

import java.util.List;
import java.util.Map;

import cn.bizzan.data.DataSource;
import cn.bizzan.entity.Country;
import cn.bizzan.entity.CurrencyEntity;

/**
 * Created by Administrator on 2018/3/1.
 */

public class CurrencyListPresenter implements CurrencyListContract.Presenter {
    private final DataSource dataRepository;
    private final CurrencyListContract.View view;

    public CurrencyListPresenter(DataSource dataRepository, CurrencyListContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void CurrencyList() {
        view.displayLoadingPopup();
        dataRepository.currency(new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.CurrencyListSuccess((Map<String, CurrencyEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.CurrencyListFail(code, toastMessage);

            }
        });
    }

    @Override
    public void CurrentRate(String key) {
        view.displayLoadingPopup();
        dataRepository.currencyRate(key, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.CurrencyRateSuccess((double) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.CurrencyRatetFail(code, toastMessage);

            }
        });
    }
}
