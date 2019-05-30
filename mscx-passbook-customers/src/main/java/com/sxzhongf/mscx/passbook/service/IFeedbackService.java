package com.sxzhongf.mscx.passbook.service;

import com.sxzhongf.mscx.passbook.vo.FeedbackVO;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;

/**
 * IFeedbackService for 评论service
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
public interface IFeedbackService {

    /**
     * 创建评论
     *
     * @param feedbackVO {@link FeedbackVO}
     * @return {@link ResponseVO}
     */
    ResponseVO createFeedback(FeedbackVO feedbackVO);

    /**
     * 获取用户评论
     *
     * @param userId 用户id
     * @return {@link ResponseVO}
     */
    ResponseVO getFeedbacks(Long userId);
}
