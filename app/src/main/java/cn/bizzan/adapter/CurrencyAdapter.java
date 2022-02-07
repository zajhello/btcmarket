package cn.bizzan.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.bizzan.R;
import cn.bizzan.entity.Country;
import cn.bizzan.entity.CurrencyEntity;

public class CurrencyAdapter extends BaseQuickAdapter<CurrencyEntity, BaseViewHolder> {


    public CurrencyAdapter(int layoutResId, @Nullable List<CurrencyEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CurrencyEntity item) {
        helper.setText(R.id.tvEnName, item.getName()).setText(R.id.tvZhName, item.getZhName()).setText(R.id.tvRate, "" + item.getRate());
    }
}
