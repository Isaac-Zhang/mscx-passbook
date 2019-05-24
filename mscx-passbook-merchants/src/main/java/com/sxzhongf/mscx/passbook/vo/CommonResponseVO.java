package com.sxzhongf.mscx.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CommonResponse for 通用Response返回对象
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/5/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {

    /**
     * 错误编码，正确返回0
     */
    private Integer errorCode = 0;

    /**
     * 错误信息，正确返回空字符串
     */
    private String errorMsg = "";

    /**
     *  返回值对象
     */
    private Object data;

    public CommonResponse(Object data) {
        this.data = data;
    }
}
