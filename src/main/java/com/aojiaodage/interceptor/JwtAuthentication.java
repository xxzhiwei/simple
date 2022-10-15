package com.aojiaodage.interceptor;

import com.alibaba.fastjson.JSON;
import com.aojiaodage.entity.User;
import com.aojiaodage.enums.TokenIssuer;
import com.aojiaodage.exception.CustomException;
import com.aojiaodage.exception.NotLoggedInException;
import com.aojiaodage.exception.WrongTokenException;
import com.aojiaodage.util.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class JwtAuthentication implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public static final String AUTHORIZATION = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();

        // 若为options方法时，直接放行
        if (HttpMethod.OPTIONS.name().equals(method)) {
            return true;
        }
        String authorization = request.getHeader(AUTHORIZATION);

        User user;
        try {
            if (!StringUtils.hasText(authorization)) {
                throw new NotLoggedInException();
            }

            // 格式应该为"Bearer token"
            String[] authorizationArr = authorization.split(" ");
            if (authorizationArr.length != 2) {
                throw new WrongTokenException();
            }
            Claims claims = JwtUtil.parseJWT(authorizationArr[1]);
            String issuer = claims.getIssuer();

            // 防止使用refreshToken当做accessToken使用
            if (!TokenIssuer.ACCESS_TOKEN_ISSUER.name().equals(issuer)) {
                throw new WrongTokenException();
            }
            String userId = claims.getSubject();
            String accessTokenKey = RedisKeyUtil.getAccessTokenKey(userId);
            String userKey = RedisKeyUtil.getUserKey(userId);

            List<String> keys = Arrays.asList(accessTokenKey, userKey);
            List<?> r = redisTemplate.execute(RedisScript.of(LuaUtil.luaScript2, List.class), keys, String.valueOf(keys.size()));
            Object r1;
            Object r2;
            if (r == null || (r1 = r.get(0)) == null || (r2 = r.get(1)) == null) {
                throw new NotLoggedInException();
            }

            String token = r1.toString();
            String userJson = r2.toString();

            // 只有存储于redis的token是有效的
            if (!token.equals(authorizationArr[1])) {
                throw new NotLoggedInException();
            }

            user = JSON.parseObject(userJson, User.class);

            // 记录当前user，以供后续使用【可用threadLocal来记录】
            UserUtil.set(user);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            R<?> r;
            if (exception instanceof CustomException) {
                CustomException ex = (CustomException) exception;
                r = new R<>(ex.getCode(), ex.getMessage());
            }
            else {
                r = R.error();
                r.setCode(NotLoggedInException.CODE);
                r.setMessage("令牌已过期，请重新登录");
            }

            RespUtil.output(response, JSON.toJSONString(r), HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 释放
        UserUtil.remove();
    }
}
