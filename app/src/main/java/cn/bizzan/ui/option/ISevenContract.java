package cn.bizzan.ui.option;

import org.json.JSONArray;

import java.util.List;

import cn.bizzan.entity.CurrentBean;
import cn.bizzan.entity.CurrentEntrust;
import cn.bizzan.entity.Detail;
import cn.bizzan.entity.Exchange;
import cn.bizzan.entity.ForecaseBean;
import cn.bizzan.entity.Money;
import cn.bizzan.entity.OptionAddBean;
import cn.bizzan.entity.OptionIconBean;
import cn.bizzan.entity.OptionOrderHistoryBean;
import cn.bizzan.entity.PushKData;
import cn.bizzan.entity.SixInfo;
import cn.bizzan.entity.modifyleverage;
import cn.bizzan.entity.switchpattern;
import cn.bizzan.ui.mychart.KLineBean;

/**
 * author: wuzongjie
 * time  : 2018/4/17 0017 19:12
 * desc  :
 */

public interface ISevenContract {
    interface View {

        void errorMes(int e, String meg);

        void option_History(OptionIconBean.DataBean objs);

        void option_Order_History(OptionOrderHistoryBean.DataBean objs);

        void option_Starting(List<ForecaseBean.DataBean> objs);

        void option_Add(String msg);

        void option_Opening(List<ForecaseBean.DataBean> objs);

        void option_Current(List<CurrentBean.DataBean> objs);
        void option_Current2(List<CurrentBean.DataBean> objs);

        void option_WalletUrl(Money money);
        void showDialog();

        void hideDialog();


        void KDataSuccess(JSONArray jsonArray);

        void KDataSuccess2(JSONArray jsonArray);

        void KDataSuccess(KLineBean data);

        void KDataSuccess2(KLineBean data);
    }

    interface Presenter {
        /**
         * 获取往期结果信息
         */
        void OptionHistory(String symbol, String token);
        //获取历史交割数据
        void OrderHistory(String symbol, String pageNo,String pageSize,String token);
        //正在要开始的预测
        void OptionStarting(String symbol, String token);
        //正在进行中的预测
        void OptionOpening(String symbol, String token);
        //获取我的开仓数据
        void OptionCurrent(String symbol, String optionId, String token,String type);
        //获取我的开仓数据
        void getWallet(String token,String coinName);
        //k线数据
        void KData(String type,String pushmsg);
        //k线数据
        void KData(String symbol, Long from, Long to, String resolution,String type);
        //看涨或看跌
        void Add(String symbol,String direction,String optionId,String amount,String token);
    }
}
