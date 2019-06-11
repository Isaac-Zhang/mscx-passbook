package com.sxzhongf.mscx.passbook.service;

import com.alibaba.fastjson.JSON;
import com.sxzhongf.mscx.passbook.constant.FeedbackTypeEnum;
import com.sxzhongf.mscx.passbook.vo.FeedbackVO;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * FeedbackServiceTest for 用户评论服务测试
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/6/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedbackServiceTest extends AbstractServiceTest {

    @Autowired
    private IFeedbackService feedbackService;

    @Test
    public void testCreateFeedback() {
        FeedbackVO forAPP = new FeedbackVO();
        forAPP.setUserId(user_id);
        forAPP.setTemplateId("-1");
        forAPP.setType(FeedbackTypeEnum.APP.getCode());
        forAPP.setComment("Study how to construct a distribution coupon system.");

        ResponseVO appResponse = feedbackService.createFeedback(forAPP);
        System.out.println("APP feedback context : " + JSON.toJSONString(appResponse));

        FeedbackVO forPassTemplate = new FeedbackVO();
        forPassTemplate.setUserId(user_id);
        forPassTemplate.setTemplateId("d8f14e90ed5a8d2daf86dd3f05e7d17e");
        forPassTemplate.setType(FeedbackTypeEnum.PASS.getCode());
        forPassTemplate.setComment("Good pass template job.");

        ResponseVO passTemplateResponse = feedbackService.createFeedback(forPassTemplate);
        System.out.println("PASS feedback context : " + JSON.toJSONString(passTemplateResponse));

        //需要断言结果是否正确
    }

    @Test
    public void testGetFeedback() {
        ResponseVO responseVO = feedbackService.getFeedbacks(user_id);
        System.out.println(user_id + "'s feedback context : " + JSON.toJSONString(responseVO));
    }
}
