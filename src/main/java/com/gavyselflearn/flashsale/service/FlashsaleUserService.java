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
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

@Service
public class FlashsaleUserService {

    public static final String COOK1_NAME_TOKEN = "token";

    @Autowired
    FlashsaleDao flashsaleDao;

    @Autowired
    RedisService redisService;

    public FlashsaleUser getById(long id) {
        //取缓存
        FlashsaleUser user = redisService.get(FlashsaleUserKey.getById, ""+id, FlashsaleUser.class);
        if(user != null) {
            return user;
        }
        //取数据库
        user = flashsaleDao.getById(id);
        if(user != null) {
            redisService.set(FlashsaleUserKey.getById,""+id, user);
        }
        return user;
    }

    public boolean updatePassword(String token, long id, String passwordNew) {
        //取user
        FlashsaleUser user = getById(id);
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        FlashsaleUser toBeUpdate = new FlashsaleUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(passwordNew, user.getSalt()));
        flashsaleDao.update(toBeUpdate);
        //处理缓存
        redisService.delete(FlashsaleUserKey.getById,""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(FlashsaleUserKey.token, token, user);
        return true;
    }

    public FlashsaleUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        FlashsaleUser user = redisService.get(FlashsaleUserKey.token, token, FlashsaleUser.class);
        //延长有效期
        if(user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
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
        addCookie(response, token, user);
        return true;

    }

    private void addCookie(HttpServletResponse response, String token, FlashsaleUser user) {
        //String token = UUIDUtil.uuid();
        redisService.set(FlashsaleUserKey.token, token, user);
        Cookie cookie = new Cookie(COOK1_NAME_TOKEN, token);
        cookie.setMaxAge(FlashsaleUserKey.token.expireSeconds());
        //System.out.println("token过期时间: "+FlashsaleUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
