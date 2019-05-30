package com.sxzhongf.mscx.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ErrorInfoVO for 统一错误信息
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfoVO<T> {

    /**
     * 统一错误码
     */
    public static final Integer ERROR = -1;

    /**
     * 特定错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 错误来源
     */
    private String url;

    /**
     * 请求返回的数据
     */
    private T data;
}
