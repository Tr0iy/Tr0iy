package com.wxc.reggie.common;

/**
 * 基于ThreadLocal封装的工具类，用于保存、获取当前用户登入的ID
 */
public class BaseContext {

    // 作用域是每一个线程之内，每一个请求都是一个新的线程！！！
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }
}
