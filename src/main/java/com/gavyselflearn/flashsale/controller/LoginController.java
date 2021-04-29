package com.gavyselflearn.flashsale.controller;

import com.gavyselflearn.flashsale.domain.FlashsaleUser;
import com.gavyselflearn.flashsale.redis.RedisService;
import com.gavyselflearn.flashsale.result.CodeMsg;
import com.gavyselflearn.flashsale.result.Result;
import com.gavyselflearn.flashsale.service.FlashsaleUserService;
import com.gavyselflearn.flashsale.util.ValidatorUtil;
import com.gavyselflearn.flashsale.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    FlashsaleUserService userService;

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

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
        //return new Result<String>(0,"success","hello,");
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(@Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //登录
        boolean ret = userService.login(loginVo);
        return Result.success(true);
        //return new Result<String>(500100,"session失效");
    }

}


//参数校验
//        String passInput = loginVo.getPassword();
//        String mobile = loginVo.getMobile();
//        if(StringUtils.isEmpty(passInput)) {
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        if(StringUtils.isEmpty(mobile)) {
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        if(!ValidatorUtil.isMobile(mobile)) {
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }