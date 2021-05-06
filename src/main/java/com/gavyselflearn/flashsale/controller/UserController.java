package com.gavyselflearn.flashsale.controller;

import com.gavyselflearn.flashsale.domain.FlashsaleUser;
import com.gavyselflearn.flashsale.redis.RedisService;
import com.gavyselflearn.flashsale.result.Result;
import com.gavyselflearn.flashsale.service.FlashsaleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    FlashsaleUserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<FlashsaleUser> info(Model model, FlashsaleUser user) {
        return Result.success(user);
    }

}
