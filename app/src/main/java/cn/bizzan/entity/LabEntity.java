package cn.bizzan.entity;

import java.io.Serializable;

public class LabEntity  implements Serializable {
   /**
    {
        "id": 6,
            "status": 1,
            "step": 1,
            "progress": 0,
            "startTime": "2022-01-26 00:00:00",
            "endTime": "2022-01-31 00:00:00",
            "type": 2,
            "leveloneCount": 0,
            "totalSupply": 1000000,
            "tradedAmount": 0,
            "freezeAmount": 0,
            "price": 456,
            "priceScale": 2,
            "unit": "CEO",
            "acceptUnit": "USDT",
            "amountScale": 2,
            "maxLimitAmout": 8888,
            "minLimitAmout": 150,
            "holdLimit": 10000,
            "holdUnit": "USDT",
            "limitTimes": 1,
            "miningPeriod": 0,
            "miningDays": 0,
            "miningUnit": "",
            "lockedUnit": "",
            "lockedPeriod": 0,
            "lockedDays": 0,
            "releaseType": 0,
            "releasePercent": 0,
            "lockedFee": 0,
            "releaseAmount": 0,
            "releaseTimes": 0,
            "miningInvite": 0,
            "miningInvitelimit": 0,
            "settings": "",
            "title": "首次上线（平分）",
            "detail": "首次上线（平分）",
            "content": "<p>首次上线（平分）首次上线（平分）首次上线（平分）首次上线（平分）首次上线（平分）首次上线（平分）首次上线（平分）首次上线（平分）首次上线（平分）首次上线（平分）首次上线（平分）首次上线（平分）<br><\/p><br><div class=\"image-desc\" style=\"text-align: center; color: #333;\">\n                <img class=\"uploaded-img\" src=\"https://api.bizzan.app/uc/readImage?basePath=/sys/202201/387bd742574340cdbb1a957bdd40b4abfile.jpg\" max-width=\"100%\" width=\"auto\" height=\"auto\"><\/div>",
            "contentEN": "",
            "smallImageUrl": "/sys/202201/d29838c50e7a4f508a03a0264b6f79dafile.jpg",
            "bannerImageUrl": "/sys/202201/8cdd0430094440feac419fefecb6b8a2file.jpg",
            "noticeLink": "/lab",
            "activityLink": "/lab",
            "createTime": "2022-01-26 20:47:43"
    }
    */

    private Integer id;
    private Integer status;
    private Integer step;
    private Integer progress;
    private String startTime;
    private String endTime;
    private Integer type;
    private Integer leveloneCount;
    private Integer totalSupply;
    private Integer tradedAmount;
    private Integer freezeAmount;
    private Integer price;
    private Integer priceScale;
    private String unit;
    private String acceptUnit;
    private Integer amountScale;
    private Integer maxLimitAmout;
    private Integer minLimitAmout;
    private Integer holdLimit;
    private String holdUnit;
    private Integer limitTimes;
    private Integer miningPeriod;
    private Integer miningDays;
    private String miningUnit;
    private String lockedUnit;
    private Integer lockedPeriod;
    private Integer lockedDays;
    private Integer releaseType;
    private Integer releasePercent;
    private Integer lockedFee;
    private Integer releaseAmount;
    private Integer releaseTimes;
    private Integer miningInvite;
    private Integer miningInvitelimit;
    private String settings;
    private String title;
    private String detail;
    private String content;
    private String contentEN;
    private String smallImageUrl;
    private String bannerImageUrl;
    private String noticeLink;
    private String activityLink;
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLeveloneCount() {
        return leveloneCount;
    }

    public void setLeveloneCount(Integer leveloneCount) {
        this.leveloneCount = leveloneCount;
    }

    public Integer getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(Integer totalSupply) {
        this.totalSupply = totalSupply;
    }

    public Integer getTradedAmount() {
        return tradedAmount;
    }

    public void setTradedAmount(Integer tradedAmount) {
        this.tradedAmount = tradedAmount;
    }

    public Integer getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(Integer freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPriceScale() {
        return priceScale;
    }

    public void setPriceScale(Integer priceScale) {
        this.priceScale = priceScale;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAcceptUnit() {
        return acceptUnit;
    }

    public void setAcceptUnit(String acceptUnit) {
        this.acceptUnit = acceptUnit;
    }

    public Integer getAmountScale() {
        return amountScale;
    }

    public void setAmountScale(Integer amountScale) {
        this.amountScale = amountScale;
    }

    public Integer getMaxLimitAmout() {
        return maxLimitAmout;
    }

    public void setMaxLimitAmout(Integer maxLimitAmout) {
        this.maxLimitAmout = maxLimitAmout;
    }

    public Integer getMinLimitAmout() {
        return minLimitAmout;
    }

    public void setMinLimitAmout(Integer minLimitAmout) {
        this.minLimitAmout = minLimitAmout;
    }

    public Integer getHoldLimit() {
        return holdLimit;
    }

    public void setHoldLimit(Integer holdLimit) {
        this.holdLimit = holdLimit;
    }

    public String getHoldUnit() {
        return holdUnit;
    }

    public void setHoldUnit(String holdUnit) {
        this.holdUnit = holdUnit;
    }

    public Integer getLimitTimes() {
        return limitTimes;
    }

    public void setLimitTimes(Integer limitTimes) {
        this.limitTimes = limitTimes;
    }

    public Integer getMiningPeriod() {
        return miningPeriod;
    }

    public void setMiningPeriod(Integer miningPeriod) {
        this.miningPeriod = miningPeriod;
    }

    public Integer getMiningDays() {
        return miningDays;
    }

    public void setMiningDays(Integer miningDays) {
        this.miningDays = miningDays;
    }

    public String getMiningUnit() {
        return miningUnit;
    }

    public void setMiningUnit(String miningUnit) {
        this.miningUnit = miningUnit;
    }

    public String getLockedUnit() {
        return lockedUnit;
    }

    public void setLockedUnit(String lockedUnit) {
        this.lockedUnit = lockedUnit;
    }

    public Integer getLockedPeriod() {
        return lockedPeriod;
    }

    public void setLockedPeriod(Integer lockedPeriod) {
        this.lockedPeriod = lockedPeriod;
    }

    public Integer getLockedDays() {
        return lockedDays;
    }

    public void setLockedDays(Integer lockedDays) {
        this.lockedDays = lockedDays;
    }

    public Integer getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(Integer releaseType) {
        this.releaseType = releaseType;
    }

    public Integer getReleasePercent() {
        return releasePercent;
    }

    public void setReleasePercent(Integer releasePercent) {
        this.releasePercent = releasePercent;
    }

    public Integer getLockedFee() {
        return lockedFee;
    }

    public void setLockedFee(Integer lockedFee) {
        this.lockedFee = lockedFee;
    }

    public Integer getReleaseAmount() {
        return releaseAmount;
    }

    public void setReleaseAmount(Integer releaseAmount) {
        this.releaseAmount = releaseAmount;
    }

    public Integer getReleaseTimes() {
        return releaseTimes;
    }

    public void setReleaseTimes(Integer releaseTimes) {
        this.releaseTimes = releaseTimes;
    }

    public Integer getMiningInvite() {
        return miningInvite;
    }

    public void setMiningInvite(Integer miningInvite) {
        this.miningInvite = miningInvite;
    }

    public Integer getMiningInvitelimit() {
        return miningInvitelimit;
    }

    public void setMiningInvitelimit(Integer miningInvitelimit) {
        this.miningInvitelimit = miningInvitelimit;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentEN() {
        return contentEN;
    }

    public void setContentEN(String contentEN) {
        this.contentEN = contentEN;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getNoticeLink() {
        return noticeLink;
    }

    public void setNoticeLink(String noticeLink) {
        this.noticeLink = noticeLink;
    }

    public String getActivityLink() {
        return activityLink;
    }

    public void setActivityLink(String activityLink) {
        this.activityLink = activityLink;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
