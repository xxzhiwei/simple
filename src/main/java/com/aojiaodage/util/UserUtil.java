package com.aojiaodage.util;

import com.aojiaodage.entity.User;

public class UserUtil {
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static User get() {
        return threadLocal.get();
    }

    public static void set(User user) {
        threadLocal.set(user);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
