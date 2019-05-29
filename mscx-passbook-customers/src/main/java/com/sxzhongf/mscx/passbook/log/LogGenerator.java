package com.sxzhongf.mscx.passbook.log;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * LogGenerator for 生成日志
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/29
 */
@Slf4j
public class LogGenerator {

    /**
     * 生成log
     * @param request {@link HttpServletRequest}
     * @param userId 用户id
     * @param action 日志类型
     * @param info 日志信息，可以为null
     */
    public static void writeLog(HttpServletRequest request, Long userId, String action, Object info) {
        log.info(
                JSON.toJSONString(
                        new LogObject(action, userId, System.currentTimeMillis(), request.getRemoteAddr(), info)
                )
        );
    }
}
