package com.sxzhongf.mscx.passbook.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LogObject for 日志对象
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogObject {

    /**
     * 日志动作类型
     */
    private String action;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 当前时间
     */
    private Long timestamp;

    /**
     * 远程请求ip，作用于反作弊
     */
    private String remoteIp;

    /**
     * 日志信息
     */
    private Object info = null;
}
