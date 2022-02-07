package cn.bizzan.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.List;

import cn.bizzan.R;
import cn.bizzan.app.GlobalConstant;
import cn.bizzan.entity.LabEntity;
import cn.bizzan.ui.mylab.LabDetaiActivity;

public class LabAdapter extends BaseQuickAdapter<LabEntity, BaseViewHolder> {

    private Context context;

    public LabAdapter(@Nullable List<LabEntity> data, Context context) {
        super(R.layout.adapter_lab, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LabEntity item) {
        Glide.with(context).load(GlobalConstant.getGlobalImagePath(item.getBannerImageUrl())).into((ImageView) helper.getView(R.id.ivImage));
        helper.setText(R.id.tvTitle, item.getTitle());
        helper.getView(R.id.tvBuy).setOnClickListener(view -> {
            LabDetaiActivity.actionStart(context, item);
        });

        int process = 0;
        if (item.getType() == 3) {

            if (item.getStep() == 1) {
                process = 50;
            } else if (item.getStep() == 2) {
                process = 75;
            } else if (item.getStep() == 3) {
                process = 100;
            } else {
                process = 0;
            }

        } else {
            process = item.getProgress();
        }

        ((ProgressBar) helper.getView(R.id.process)).setProgress(process);

        String format = new DecimalFormat("#0.00").format(process);
        helper.setText(R.id.tvPercent, format + "%");

        String format2 = new DecimalFormat("#0.00").format(item.getTotalSupply());
        helper.setText(R.id.amount, format2 + "  " + item.getUnit());

        String type;
        if (item.getType() == 1) {
            type = context.getResources().getString(R.string.lab_type1);
        } else if (item.getType() == 2) {
            type = context.getResources().getString(R.string.lab_type2);
        } else if (item.getType() == 3) {
            type = context.getResources().getString(R.string.lab_type3);
        } else if (item.getType() == 4) {
            type = context.getResources().getString(R.string.lab_type4);
        } else if (item.getType() == 5) {
            type = context.getResources().getString(R.string.lab_type5);
        } else if (item.getType() == 6) {
            type = context.getResources().getString(R.string.lab_type6);
        } else {
            type = context.getResources().getString(R.string.lab_type_unknow);
        }

        helper.setText(R.id.tvTitle2, item.getDetail());
        helper.setText(R.id.tvStartTime, item.getStartTime());
        helper.setText(R.id.tvEndTime, item.getEndTime());
    }
}
