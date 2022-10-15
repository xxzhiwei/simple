package com.aojiaodage.service.impl;

import com.alibaba.fastjson.JSON;
import com.aojiaodage.dao.UserDao;
import com.aojiaodage.dto.LoginForm;
import com.aojiaodage.dto.Query;
import com.aojiaodage.dto.RefreshForm;
import com.aojiaodage.dto.UserForm;
import com.aojiaodage.entity.User;
import com.aojiaodage.entity.UserRole;
import com.aojiaodage.enums.TokenIssuer;
import com.aojiaodage.exception.CustomException;
import com.aojiaodage.exception.TokenExpired;
import com.aojiaodage.exception.WrongTokenException;
import com.aojiaodage.exception.WrongUsernameOrPwdException;
import com.aojiaodage.service.PermissionService;
import com.aojiaodage.service.UserRoleService;
import com.aojiaodage.service.UserService;
import com.aojiaodage.util.*;
import com.aojiaodage.vo.LoginInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    PermissionService permissionService;

    @Autowired
    UserRoleService userRoleService;

    @Override
    public LoginInfo<User> login(LoginForm loginForm) {
        // 1、根据用户名查询数据库（确保用户名在数据库是唯一的
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginForm.getUsername());
        wrapper.eq(User::getPassword, Md5Util.encode(loginForm.getPassword()));

        User user = getOne(wrapper);
        // 2、找不到用户时，返回与密码错误时相同的提示信息
        if (user == null) {
            throw new WrongUsernameOrPwdException();
        }
        // 3、查询用户角色
        if (user.getState().equals(2)) {
            throw new CustomException("账户已被禁用");
        }
        String subject = user.getId().toString();
        String accessToken = makeAccessToken(subject);

        // 刷新token（7天）
        String refreshToken = makeRefreshToken(subject);

        LoginInfo<User> loginInfo = new LoginInfo<>(accessToken, refreshToken);

        loginInfo.setBaseInfo(user);
        cacheUser(user);
        cacheToken(loginInfo, user.getId());
        return loginInfo;
    }

    private void cacheToken(LoginInfo<User> loginInfo, Integer userId) {
        String accessTokenKey = RedisKeyUtil.getAccessTokenKey(userId);
        redisTemplate.opsForValue().set(accessTokenKey, loginInfo.getAccessToken(), JwtUtil.TOKEN_TTL, TimeUnit.MILLISECONDS);
        if (StringUtils.hasText(loginInfo.getRefreshToken())) {
            String refreshTokenKey = RedisKeyUtil.getRefreshTokenKey(userId);
            redisTemplate.opsForValue().set(refreshTokenKey, loginInfo.getRefreshToken(), JwtUtil.REFRESH_TOKEN_TTL, TimeUnit.MILLISECONDS);
        }
    }

    private String makeToken(String userId, Long ttl, String uuid, String issuer) {
        JwtBuilder jwtBuilder = JwtUtil.getJwtBuilder(userId, ttl, uuid, issuer);
        return jwtBuilder.compact();
    }

    private String makeAccessToken(String userId) {
        return makeToken(userId, JwtUtil.TOKEN_TTL, JwtUtil.getUUID(), TokenIssuer.ACCESS_TOKEN_ISSUER.name());
    }

    private String makeRefreshToken(String userId) {
        return makeToken(userId, JwtUtil.REFRESH_TOKEN_TTL, JwtUtil.getUUID(), TokenIssuer.REFRESH_TOKEN_ISSUER.name());
    }

    // accessToken的有效期和其保存在redis中的有效期一致
    private void cacheUser(User user) {
        String key = RedisKeyUtil.getUserKey(user.getId());
        redisTemplate.opsForValue().set(key, JSON.toJSONString(user), JwtUtil.TOKEN_TTL, TimeUnit.MILLISECONDS);
    }

    @Override
    public void logout() {
        Integer id = UserUtil.get().getId();
        String accessTokenKey = RedisKeyUtil.getAccessTokenKey(id);
        String userKey = RedisKeyUtil.getUserKey(id);
        String refreshTokenKey = RedisKeyUtil.getRefreshTokenKey(id);
        List<String> keys = Arrays.asList(accessTokenKey, userKey, refreshTokenKey);
        Long r = redisTemplate.execute(
                RedisScript.of(LuaUtil.luaScript1, Long.class),
                keys,
                String.valueOf(keys.size()));
        if (r == null || r != 1) {
            throw new CustomException("登出失败");
        }
    }

    @Override
    public LoginInfo<User> refresh(RefreshForm refreshForm) {
        try {
            // 校验refreshToken是否过期
            Claims claims = JwtUtil.parseJWT(refreshForm.getRefreshToken());

            // 校验是否为refreshToken
            if (!TokenIssuer.REFRESH_TOKEN_ISSUER.name().equals(claims.getIssuer())) {
                throw new WrongTokenException();
            }
            String userId = claims.getSubject();

            // 检查该refreshToken是否存在redis中
            String refreshTokenKey = RedisKeyUtil.getRefreshTokenKey(userId);
            String refreshTokenInRedis = redisTemplate.opsForValue().get(refreshTokenKey);

            // 若token不存在或者传入的token与存在于redis中的不同，则认为是过期的
            if (!StringUtils.hasText(refreshTokenInRedis) || !refreshTokenInRedis.equals(refreshForm.getRefreshToken())) {
                throw new TokenExpired();
            }
            LoginInfo<User> loginInfo = new LoginInfo<>();

            // 重新缓存用户信息
            User user = getById(userId);

            if (user == null) {
                throw new CustomException("用户不存在，id：" + userId);
            }

            // 生成新的token
            String accessToken = makeAccessToken(userId);
            loginInfo.setAccessToken(accessToken);

            Date expiration = claims.getExpiration();
            long currentTimeMillis = System.currentTimeMillis();

            // refreshToken即将过期时，重新颁发一个
            boolean refreshTokenExpired = (expiration.getTime() - currentTimeMillis) < JwtUtil.TOKEN_TTL * 6;
            if (refreshTokenExpired) {
                String refreshToken = makeRefreshToken(userId);
                loginInfo.setRefreshToken(refreshToken);
            }

            cacheUser(user);
            cacheToken(loginInfo, user.getId());
            return loginInfo;
        }
        catch (TokenExpired exception) {
            throw exception;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new TokenExpired();
        }
    }

    @Override
    public Page<User> getPagination(Query query) {
        Page<User> page = PaginationUtil.getPage(query);
        page(page);
        return page;
    }

    @Transactional
    @Override
    public void update(UserForm form) {
        Integer id = form.getId();

        User user = getById(id);

        if (user == null) {
            throw new CustomException("数据不存在");
        }
        user.setAvatar(form.getAvatar());
        user.setEmail(form.getEmail());
        user.setNickname(form.getNickname());
        updateById(user);

        List<Integer> assigning = form.getAssigning();
        List<Integer> removing = form.getRemoving();

        // 新增角色
        if (assigning != null && assigning.size() > 0) {
            List<UserRole> userRoles = assigning
                    .stream()
                    .map(roleId -> UserRole.builder().roleId(roleId).userId(id).build())
                    .collect(Collectors.toList());
            userRoleService.saveBatch(userRoles);
        }

        // 移除角色
        if (removing != null && removing.size() > 0) {
            userRoleService.removeByIds(removing);
        }
    }

    @Transactional
    @Override
    public void save(UserForm form) {
        User user = new User();
        BeanUtils.copyProperties(form, user);

        user.setPassword(Md5Util.encode(user.getPassword()));

        // 保存用户信息
        save(user);
        List<Integer> assigning = form.getAssigning();

        // 保存角色信息
        if (assigning != null && assigning.size() > 0) {
            List<UserRole> userRoles = assigning
                    .stream()
                    .map(roleId -> UserRole.builder().roleId(roleId).userId(user.getId()).build())
                    .collect(Collectors.toList());
            userRoleService.saveBatch(userRoles);
        }
    }

    @Transactional
    @Override
    public void del(UserForm form) {
        Integer id = form.getId();

        User old = getById(id);

        if (old == null) {
            throw new CustomException("数据不存在");
        }

        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId, id);
        userRoleService.remove(userRoleLambdaQueryWrapper);

        removeById(id);
    }
}
