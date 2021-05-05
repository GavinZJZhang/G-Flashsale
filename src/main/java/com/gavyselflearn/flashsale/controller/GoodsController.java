package com.gavyselflearn.flashsale.controller;

import com.gavyselflearn.flashsale.domain.FlashsaleUser;
import com.gavyselflearn.flashsale.redis.RedisService;
import com.gavyselflearn.flashsale.service.FlashsaleUserService;
import com.gavyselflearn.flashsale.service.GoodsService;
import com.gavyselflearn.flashsale.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    FlashsaleUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;


    @RequestMapping("/to_list")
    public String toLogin(Model model, FlashsaleUser user) {
        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
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

}
