package com.gavyselflearn.flashsale.service;

import com.gavyselflearn.flashsale.dao.FlashsaleDao;
import com.gavyselflearn.flashsale.domain.FlashsaleUser;
import com.gavyselflearn.flashsale.exception.GlobalException;
import com.gavyselflearn.flashsale.redis.FlashsaleUserKey;
import com.gavyselflearn.flashsale.redis.RedisService;
import com.gavyselflearn.flashsale.result.CodeMsg;
import com.gavyselflearn.flashsale.util.MD5Util;
import com.gavyselflearn.flashsale.util.UUIDUtil;
import com.gavyselflearn.flashsale.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class FlashsaleUserService {

    public static final String COOK1_NAME_TOKEN = "token";

    @Autowired
    FlashsaleDao flashsaleDao;

    @Autowired
    RedisService redisService;

    public FlashsaleUser getById(long id) {
        return flashsaleDao.getById(id);
    }

    public boolean login(LoginVo loginVo) {
        if(loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        FlashsaleUser user = getById(Long.parseLong(mobile));
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if(!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        redisService.set(FlashsaleUserKey.token, token, user);
        //Cookie cookie = new Cookie();
        return true;
    }

}
