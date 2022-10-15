package com.aojiaodage.util;

import com.aojiaodage.constant.RedisKey;

public class RedisKeyUtil {
    public static String getAccessTokenKey(Integer userId) {
        return getAccessTokenKey(userId.toString());
    }

    public static String getAccessTokenKey(String userId) {
        return RedisKey.ADMIN + userId + RedisKey.ACCESS_TOKEN;
    }

    public static String getRefreshTokenKey(Integer userId) {
        return getRefreshTokenKey(userId.toString());
    }

    public static String getRefreshTokenKey(String userId) {
        return RedisKey.ADMIN + userId + RedisKey.REFRESH_TOKEN;
    }

    public static String getUserKey(Integer userId) {
        return getUserKey(userId.toString());
    }

    public static String getUserKey(String userId) {
        return RedisKey.ADMIN + userId;
    }
}
