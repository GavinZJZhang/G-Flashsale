package com.gavyselflearn.flashsale.vo;


import com.gavyselflearn.flashsale.domain.FlashsaleUser;

public class GoodsDetailVo {
    private int flashsaleStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private FlashsaleUser user;

    public int getFlashsaleStatus() {
        return flashsaleStatus;
    }

    public void setFlashsaleStatus(int flashsaleStatus) {
        this.flashsaleStatus = flashsaleStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public FlashsaleUser getUser() {
        return user;
    }

    public void setUser(FlashsaleUser user) {
        this.user = user;
    }

}
