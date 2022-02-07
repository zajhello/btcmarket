package cn.bizzan.entity;

import java.io.Serializable;

public class CurrencyEntity implements Serializable {

//    {
//        "name":"CNY",
//            "zhName":"人民币",
//            "symbol":"￥",
//            "rate":6.47
//    }

    private String name;
    private String zhName;
    private String symbol;
    private Double rate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
