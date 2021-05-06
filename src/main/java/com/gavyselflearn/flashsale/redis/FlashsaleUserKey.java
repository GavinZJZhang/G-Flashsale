package com.gavyselflearn.flashsale.redis;

public class FlashsaleUserKey extends BasePrefix {


    public static final int TOKEN_EXPIRE = 3600*24*2;
    private FlashsaleUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static FlashsaleUserKey token = new FlashsaleUserKey(TOKEN_EXPIRE,"tk");
    public static FlashsaleUserKey getById = new FlashsaleUserKey(0,"id");


}
