package com.xuchen.demo.common.util;

public class UserContext {

    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    public static Long getUserId() {
        return threadLocal.get();
    }

    public static void setUserId(Long userId) {
        UserContext.threadLocal.set(userId);
    }

    public static void removeUserId() {
        threadLocal.remove();
    }
}
