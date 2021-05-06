package com.gavyselflearn.flashsale.service;

import com.gavyselflearn.flashsale.dao.OrderDao;
import com.gavyselflearn.flashsale.domain.FlashsaleOrder;
import com.gavyselflearn.flashsale.domain.FlashsaleUser;
import com.gavyselflearn.flashsale.domain.OrderInfo;
import com.gavyselflearn.flashsale.redis.OrderKey;
import com.gavyselflearn.flashsale.redis.RedisService;
import com.gavyselflearn.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    public FlashsaleOrder getFlashsaleOrderByUserIdGoodId(long userId, long goodsId) {
        return redisService.get(OrderKey.getFlashsaleOrderByUidGid,""+userId+"_"+goodsId, FlashsaleOrder.class);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    @Transactional
    public OrderInfo createOrder(FlashsaleUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);
        FlashsaleOrder flashsaleOrder = new FlashsaleOrder();
        flashsaleOrder.setGoodsId(goods.getId());
        flashsaleOrder.setOrderId(orderInfo.getId());
        flashsaleOrder.setUserId(user.getId());
        orderDao.insertFlashsaleOrder(flashsaleOrder);

        redisService.set(OrderKey.getFlashsaleOrderByUidGid, ""+user.getId()+"_"+goods.getId(), flashsaleOrder);

        return orderInfo;
    }
}
