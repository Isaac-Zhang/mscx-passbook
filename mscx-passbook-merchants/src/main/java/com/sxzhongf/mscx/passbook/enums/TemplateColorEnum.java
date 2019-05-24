package com.sxzhongf.mscx.passbook.enums;

/**
 * TemplateColorEnum for 商户优惠券背景颜色
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/24
 */
public enum TemplateColorEnum {

    RED(1, "红色"),
    GREEN(2, "绿色"),
    BLUE(3, "蓝色");

    /**
     * 颜色代码
     */
    private Integer code;

    /**
     * 颜色描述
     */
    private String color;

    TemplateColorEnum(Integer code, String color) {
        this.code = code;
        this.color = color;
    }

    public Integer getCode() {
        return code;
    }

    public String getColor() {
        return color;
    }
}
