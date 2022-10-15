package com.aojiaodage.util;

public class LuaUtil {
    // 查询并删除
    public static final String luaScript1 = "for i,k in pairs(KEYS) do local v=redis.call('get', k); if v then redis.call('del', k); end; end; return 1;";
    // 查询多个key
    public static final String luaScript2 = "local r={}; for i,k in pairs(KEYS) do r[i]=redis.call('get', k) end; return r;";
}
