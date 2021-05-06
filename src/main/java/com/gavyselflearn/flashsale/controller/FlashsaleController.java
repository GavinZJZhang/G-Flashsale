package com.gavyselflearn.flashsale.controller;

import com.gavyselflearn.flashsale.domain.FlashsaleOrder;
import com.gavyselflearn.flashsale.domain.FlashsaleUser;
import com.gavyselflearn.flashsale.rabbitmq.FlashsaleMessage;
import com.gavyselflearn.flashsale.rabbitmq.MQSender;
import com.gavyselflearn.flashsale.redis.GoodsKey;
import com.gavyselflearn.flashsale.redis.RedisService;
import com.gavyselflearn.flashsale.result.CodeMsg;
import com.gavyselflearn.flashsale.result.Result;
import com.gavyselflearn.flashsale.service.FlashsaleService;
import com.gavyselflearn.flashsale.service.FlashsaleUserService;
import com.gavyselflearn.flashsale.service.GoodsService;
import com.gavyselflearn.flashsale.service.OrderService;
import com.gavyselflearn.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/miaosha")
public class FlashsaleController implements InitializingBean {

    @Autowired
    FlashsaleUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    FlashsaleService flashsaleService;

    @Autowired
    MQSender sender;

    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 系统初始化
     * @throws Exception
     * @Author Gavin
     * @Created on May 4th, 2021
     * @Modified on May 5th, 2021 对Redis内存标记，减少对Redis的访问
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList= goodsService.listGoodsVo();
        if(goodsList == null) {
            return;
        }
        for(GoodsVo goods:goodsList) {
            redisService.set(GoodsKey.getFlashsaleGoodsStock, ""+goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    /**
     *
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     * @Author Gavin
     * @Version G-FlashSale6.2
     * @Modified on May 4th, 2021 对/do_miaosha做了如下接口优化：
     */
    @RequestMapping(value = "/do_miaosha", method=RequestMethod.POST)
    @ResponseBody
    public Result<Integer> toLogin(Model model, FlashsaleUser user,
            @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
           return Result.error(CodeMsg.SESSION_ERROR);
        }

        boolean over = localOverMap.get(goodsId);
        if(over) {
            return Result.error(CodeMsg.FLASH_SALE_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getFlashsaleGoodsStock, ""+goodsId);
        if(stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.FLASH_SALE_OVER);
        }
        //判断是否已经秒杀成功过
        FlashsaleOrder order = orderService.getFlashsaleOrderByUserIdGoodId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_FLASHSALE);
        }
        //入队
        FlashsaleMessage fm = new FlashsaleMessage();
        fm.setUser(user);
        fm.setGoodsId(goodsId);
        sender.sendFlashsaleMessage(fm);
        return Result.success(0); // 排队中

    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, FlashsaleUser user,
                         @PathVariable("goodsId")long goodsId) {
        //snowflake
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        //
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int flashsaleStatus = 0;
        int remainSeconds = 0;

        if(now < startAt) {//秒杀还没开始，倒计时
            flashsaleStatus = 0;
            remainSeconds = ((int)(startAt-now)/1000);
        } else if(now > endAt) {//秒杀已经结束
            flashsaleStatus = 2;
            remainSeconds = -1;
        } else {//秒杀正在进行中
            flashsaleStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("flashsaleStatus", flashsaleStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }

    /**
     *
     * @param model
     * @param user
     * @param goodsId
     * @return 成功:orderId   秒杀失败:-1    排队中:0
     */
    @RequestMapping(value="/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> flashsaleResult(Model model, FlashsaleUser user,
                         @RequestParam("goodsId")long goodsId) {
       model.addAttribute("user", user);
       if(user == null) {
           return Result.error(CodeMsg.SESSION_ERROR);
       }
       long result =  flashsaleService.getFlashsaleResult(user.getId(), goodsId);
       return Result.success(result);
    }



}
