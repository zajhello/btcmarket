package cn.bizzan.ui.mychart;

import java.util.List;

public class KHuoBiLineBean {

    String  ch ;
    String status ;
    long ts;
    List<KLineBean> data;

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public List<KLineBean> getData() {
        return data;
    }

    public void setData(List<KLineBean> data) {
        this.data = data;
    }
}
