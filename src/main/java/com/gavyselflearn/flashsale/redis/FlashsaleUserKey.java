package com.gavyselflearn.flashsale.redis;

public class FlashsaleUserKey extends BasePrefix {

    private FlashsaleUserKey(String prefix) {
        super(prefix);
    }
    public static FlashsaleUserKey token = new FlashsaleUserKey("tk");


}
