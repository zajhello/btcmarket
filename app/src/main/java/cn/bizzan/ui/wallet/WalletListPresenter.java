package cn.bizzan.ui.wallet;


import java.util.List;

import cn.bizzan.data.DataSource;
import cn.bizzan.entity.Coin;
import cn.bizzan.entity.SupportCoin;

public class WalletListPresenter implements WalletListContract.Presenter {

    private final DataSource dataRepository;
    private final WalletListContract.View view;

    public WalletListPresenter(DataSource dataRepository, WalletListContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void mySupportCoins(String token) {
        view.displayLoadingPopup();
        dataRepository.getSupportCoins(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.mySupportCoinsSuccess((List<SupportCoin>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.mySupportCoinsFailed();
            }
        });
    }
}
