package com.sxzhongf.mscx.passbook.constant;

/**
 * FeedbackTypeEnum for 用户反馈类型枚举
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/29
 */
public enum FeedbackTypeEnum {
    PASS(1, "针对优惠券的评论"),
    APP(2, "针对卡包APP的评论");

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private Integer code;
    private String desc;

    FeedbackTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
