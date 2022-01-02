package cn.bizzan.ui.home.presenter;

import cn.bizzan.ui.home.MainContract;
import cn.bizzan.data.DataSource;
import cn.bizzan.entity.Coin;
import cn.bizzan.entity.SafeSetting;

import java.util.List;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018/2/24.
 */

public class FivePresenter implements MainContract.FivePresenter {
    private MainContract.FiveView view;
    private DataSource dataRepository;

    public FivePresenter(DataSource dataRepository, MainContract.FiveView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void myWallet(String token) {
        dataRepository.myWallet(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.myWalletSuccess((List<Coin>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.myWalletFail(code, toastMessage);
            }
        });
    }

    @Override
    public void safeSetting(String token) {
        dataRepository.safeSetting(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.safeSettingSuccess((SafeSetting) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.safeSettingFail(code, toastMessage);

            }
        });
    }

    @Override
    public void myPromotion(String token) {
        dataRepository.myPromotion(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.myPromotionSuccess((JSONObject) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.myPromotionFail(code, toastMessage);
            }
        });
    }

    @Override
    public void getUdunConf(String token) {
        dataRepository.getUdunConf(token,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.myUdunConfSuccess();
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.myUdunConfFail();
            }
        });
    }
}
