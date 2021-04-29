package com.gavyselflearn.flashsale.controller;

import com.gavyselflearn.flashsale.domain.User;
import com.gavyselflearn.flashsale.redis.RedisService;
import com.gavyselflearn.flashsale.redis.UserKey;
import com.gavyselflearn.flashsale.result.CodeMsg;
import com.gavyselflearn.flashsale.result.Result;
import com.gavyselflearn.flashsale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.soap.SOAPBinding;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name","yes I also learn with Joshua——a good teacher");
        return "hello";
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "HelloWorld!";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
        return Result.success("hello,");
        //return new Result<String>(0,"success","hello,");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<CodeMsg> helloError() {
        return Result.error(CodeMsg.SERVER_ERROR);
        //return new Result<String>(500100,"session失效");
    }


    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {

        User user = userService.getById(1);
        return Result.success(user);

    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {

        userService.tx();
        return Result.success(true);

    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {

        User user = redisService.get(UserKey.getById,""+1, User.class);
        return Result.success(user);

    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("1111");
        redisService.set(UserKey.getById, ""+1, user);
        //String str = redisService.get(UserKey.getById, ""+1, String.class);
        return Result.success(true);

    }


}
