package com.sxzhongf.mscx.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ResponseVO for 创建通用响应对象
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
@Data
//@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO {

    /**
     * 错误吗：正确返回 0
     */
    private Integer errorCode = 0;

    /**
     * 错误信息，正确返回 空字符串
     */
    private String errorMsg = "";

    /**
     * 返回值对象
     */
    private Object data;

    /**
     * 正确的响应构造函数
     */
    public ResponseVO(Object data) {
        this.data = data;
    }

    /**
     * 空响应
     */
    public static ResponseVO success() {
        return new ResponseVO();
    }

    public ResponseVO (){}

    /**
     * 错误响应
     * @param errorMsg
     * @return
     */
    public static ResponseVO failure(String errorMsg) {
        return new ResponseVO(-1, errorMsg, null);
    }
}
