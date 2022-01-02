package cn.bizzan.ui.mychart;

/**
 * Created by loro on 2017/2/8.
 */
public class KLineBean {
    public String date;
    public float open;
    public float close;
    public float high;
    public float low;
    public float vol;
    public String id;
    public float count;
    public float amount;


    public KLineBean(String date, float open, float close, float high, float low, float vol) {
        this.date = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.vol = vol;
    }

    public float getAmount() {
        return amount;
    }

    public float getCount() {
        return count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public float getOpen() {
        return open;
    }

    public float getClose() {
        return close;
    }

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }

    public float getVol() {
        return vol;
    }
}
