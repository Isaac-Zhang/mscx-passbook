package com.sxzhongf.mscx.passbook.security;

/**
 * 使用ThreadLocal 单独存储每个线程携带的TOKEN信息
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see ThreadLocal
 * @since 2019/5/24
 */
public class AccessContext {

    private static final ThreadLocal<String> token = new ThreadLocal<>();

    /**
     * 获取当前线程token
     *
     * @return token
     */
    public static String getToken() {
        return token.get();
    }

    /**
     * 设置token
     */
    public static void setToken(String tokenStr) {
        token.set(tokenStr);
    }

    /**
     * 清除当前线程中的token信息
     */
    public static void clearAccessKey() {
        token.remove();
    }
}
