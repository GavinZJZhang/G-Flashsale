package com.gavyselflearn.flashsale.service;

import com.gavyselflearn.flashsale.domain.FlashsaleOrder;
import com.gavyselflearn.flashsale.domain.FlashsaleUser;
import com.gavyselflearn.flashsale.domain.OrderInfo;
import com.gavyselflearn.flashsale.redis.FlashKey;
import com.gavyselflearn.flashsale.redis.RedisService;
import com.gavyselflearn.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlashsaleService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo flashsale(FlashsaleUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        String stockBefore = goodsService.getStock(goods);
        int reduceRet =goodsService.reduceStock(goods);
        String stockAfter = goodsService.getStock(goods);
        System.out.print("userId"+user.getId()+"    该用户秒杀前库存"+stockBefore+"    reduceRet"+reduceRet+"    该用户秒杀后库存"+stockAfter);
        if(reduceRet==1) {
            System.out.print("  该用户秒杀成功");
        }
        if(reduceRet==0) {
            setGoodsOver(goods.getId());
            System.out.print("  该用户秒杀失败");
            //throw new RuntimeException("该用户秒杀失败，实际库存已为0");
        }
        System.out.println();
        //order_info miaosha_order
        return orderService.createOrder(user, goods);
    }

    public long getFlashsaleResult(Long userId, long goodsId) {
        FlashsaleOrder order = orderService.getFlashsaleOrderByUserIdGoodId(userId, goodsId);
        if(order != null) { // 秒杀成功
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(FlashKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(FlashKey.isGoodsOver, ""+goodsId);
    }
}
