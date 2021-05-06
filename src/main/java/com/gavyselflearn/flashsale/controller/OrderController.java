package com.gavyselflearn.flashsale.controller;

import com.gavyselflearn.flashsale.domain.FlashsaleUser;
import com.gavyselflearn.flashsale.domain.OrderInfo;
import com.gavyselflearn.flashsale.redis.RedisService;
import com.gavyselflearn.flashsale.result.CodeMsg;
import com.gavyselflearn.flashsale.result.Result;
import com.gavyselflearn.flashsale.service.FlashsaleUserService;
import com.gavyselflearn.flashsale.service.GoodsService;
import com.gavyselflearn.flashsale.service.OrderService;
import com.gavyselflearn.flashsale.vo.GoodsVo;
import com.gavyselflearn.flashsale.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    FlashsaleUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    //@NeedLogin //这个可以当作练习自己实现一下这个拦截器注解
    public Result<OrderDetailVo> info(Model model, FlashsaleUser user,
            @RequestParam("orderId")long orderId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if(order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }




}
