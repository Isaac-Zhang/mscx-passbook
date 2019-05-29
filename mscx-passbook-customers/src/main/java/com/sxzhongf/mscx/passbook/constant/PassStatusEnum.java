package com.sxzhongf.mscx.passbook.constant;

/**
 * PassStatusEnum for 优惠券状态
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/29
 */
public enum PassStatusEnum {
    UNUSED(1, "未被使用的"),
    USED(2, "已经使用的"),
    ALL(3, "全部领取的");

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    PassStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
