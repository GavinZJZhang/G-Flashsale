package com.gavyselflearn.flashsale.rabbitmq;

import com.gavyselflearn.flashsale.domain.FlashsaleUser;

public class FlashsaleMessage {
    private FlashsaleUser user;
    private long goodsId;

    public FlashsaleUser getUser() {
        return user;
    }

    public void setUser(FlashsaleUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
