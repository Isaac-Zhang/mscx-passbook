package com.sxzhongf.mscx.passbook.constant;

/**
 * FeedbackTypeEnum for 用户反馈类型枚举
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/29
 */
public enum FeedbackTypeEnum {
    PASS("pass", "针对优惠券的评论"),
    APP("app", "针对卡包APP的评论");

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private String code;
    private String desc;

    FeedbackTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
