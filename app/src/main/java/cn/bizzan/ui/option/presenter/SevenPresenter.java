package cn.bizzan.ui.option.presenter;

import cn.bizzan.data.DataSource;
import cn.bizzan.ui.home.MainContract;

/**
 * Created by Administrator on 2018/2/24.
 */

public class SevenPresenter implements MainContract.SevenPresenter {
    private MainContract.SevenView view;
    private DataSource dataRepository;

    public SevenPresenter(DataSource dataRepository, MainContract.SevenView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

}
