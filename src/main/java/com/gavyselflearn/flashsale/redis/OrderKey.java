package com.gavyselflearn.flashsale.redis;

public class OrderKey extends BasePrefix {

    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getFlashsaleOrderByUidGid = new OrderKey("moug");

}
