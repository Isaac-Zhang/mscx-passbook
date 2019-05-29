package com.sxzhongf.mscx.passbook.vo;

import com.google.common.base.Enums;
import com.sxzhongf.mscx.passbook.constant.FeedbackTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FeedbackVO for 用户评论表
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackVO {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 评论类型
     */
    private String type;
    /**
     * PassTemplate 在hbase中的RowKey
     * 如果是App评论，则没有
     */
    private String templateId;
    /**
     * 评论内容
     */
    private String comment;

    /**
     * 校验评论类型和评论内容
     *
     * @return true/false
     */
    public boolean validate() {
        FeedbackTypeEnum feedbackType = Enums.getIfPresent(
                FeedbackTypeEnum.class,
                this.type.toUpperCase()).orNull();
        return !(null == feedbackType || null == comment);
    }
}
