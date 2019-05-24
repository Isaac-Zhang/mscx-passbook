package com.sxzhongf.mscx.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CommonResponseVO for 通用CommonResponseVO返回对象
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/5/24
 */
@Data
@AllArgsConstructor
public class CommonResponseVO {

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

    public CommonResponseVO() {
    }

    public CommonResponseVO(Object data) {
        this.data = data;
    }
}
