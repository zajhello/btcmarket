package cn.bizzan.ui.wallet;

import java.util.List;

import cn.bizzan.base.Contract;
import cn.bizzan.entity.Coin;
import cn.bizzan.entity.SupportCoin;

public interface WalletListContract {

    interface View extends Contract.BaseView<WalletListContract.Presenter> {

        void mySupportCoinsSuccess(List<SupportCoin> obj);

        void mySupportCoinsFailed();
    }

    interface Presenter extends Contract.BasePresenter {

        void mySupportCoins(String token);
    }
}
