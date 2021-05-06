package com.gavyselflearn.flashsale.redis;

public class FlashKey extends BasePrefix {

    private FlashKey(String prefix) {
        super(prefix);
    }

    public static FlashKey isGoodsOver = new FlashKey("go");

}
