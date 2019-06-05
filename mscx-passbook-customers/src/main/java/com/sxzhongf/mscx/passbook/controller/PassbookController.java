package com.sxzhongf.mscx.passbook.controller;

import com.sxzhongf.mscx.passbook.log.LogConstants;
import com.sxzhongf.mscx.passbook.log.LogGenerator;
import com.sxzhongf.mscx.passbook.service.IFeedbackService;
import com.sxzhongf.mscx.passbook.service.IInventoryService;
import com.sxzhongf.mscx.passbook.service.IPassTemplateService;
import com.sxzhongf.mscx.passbook.service.IUserPassService;
import com.sxzhongf.mscx.passbook.vo.FeedbackVO;
import com.sxzhongf.mscx.passbook.vo.GainPassTemplateRequestVO;
import com.sxzhongf.mscx.passbook.vo.PassVO;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * PassbookController for 优惠券控制器
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/6/4
 */
@Slf4j
@RestController
@RequestMapping(path = "/v1/passbook")
public class PassbookController {

    /**
     * 用户优惠券服务
     */
    private final IUserPassService userPassService;

    /**
     * 库存服务
     */
    private final IInventoryService inventoryService;

    /**
     * 领取优惠券服务
     */
    private final IPassTemplateService passTemplateService;

    /**
     * 用户反馈服务
     */
    private final IFeedbackService feedbackService;

    /**
     * httpServletRequest
     */
    private final HttpServletRequest httpServletRequest;

    /**
     * 构造函数注入
     */
    @Autowired
    public PassbookController(IUserPassService userPassService, IInventoryService inventoryService,
                              IPassTemplateService passTemplateService, IFeedbackService feedbackService,
                              HttpServletRequest httpServletRequest) {
        this.userPassService = userPassService;
        this.inventoryService = inventoryService;
        this.passTemplateService = passTemplateService;
        this.feedbackService = feedbackService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * 获取用户可领取优惠券信息
     *
     * @param user_id user_id
     */
    @GetMapping(path = "/user/{user_id}/un_used")
    public ResponseVO getUserPassbookByUserId(@PathVariable(name = "user_id") Long user_id) {
        ResponseVO responseVO;
        LogGenerator.writeLog(httpServletRequest, user_id, LogConstants.ActionName.USER_PASS_INFO, null);

        try {
            responseVO = userPassService.getUserPassInfo(user_id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.failure("获取用户优惠券失败！");
        }
        return responseVO;
    }

    /**
     * 获取用户已使用优惠券信息
     *
     * @param user_id user_id
     */
    @GetMapping(path = "/user/{user_id}/used")
    public ResponseVO getUsedUserPassbookByUserId(@PathVariable(name = "user_id") Long user_id) {
        ResponseVO responseVO;
        LogGenerator.writeLog(httpServletRequest, user_id, LogConstants.ActionName.USER_PASS_INFO, null);

        try {
            responseVO = userPassService.getUserUsedPassInfo(user_id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.failure("获取用户已使用优惠券失败！");
        }
        return responseVO;
    }

    /**
     * 获取用户所有优惠券信息
     *
     * @param user_id user_id
     */
    @GetMapping(path = "/user/{user_id}")
    public ResponseVO getAllUserPassbookByUserId(@PathVariable(name = "user_id") Long user_id) {
        ResponseVO responseVO;
        LogGenerator.writeLog(httpServletRequest, user_id, "getAllUserPassbookByUserId", null);

        try {
            responseVO = userPassService.getUserUsedPassInfo(user_id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.failure("获取用户所有优惠券失败！");
        }
        return responseVO;
    }

    /**
     * 使用使用优惠券
     *
     * @param passVO {@link PassVO}
     * @return {@link ResponseVO}
     */
    @PostMapping(name = "/user/spent")
    public ResponseVO userSpentPassbook(@RequestBody PassVO passVO) {
        LogGenerator.writeLog(
                httpServletRequest, passVO.getUserId(), LogConstants.ActionName.USER_USE_PASS, passVO
        );
        return userPassService.userUsePassInfo(passVO);
    }

    /**
     * 获取用户优惠券库存
     *
     * @param user_id 用户id
     * @return {@link ResponseVO}
     */
    @PostMapping(name = "/user/inventory")
    public ResponseVO getUserInventory(@RequestParam("user_id") Long user_id) {
        LogGenerator.writeLog(
                httpServletRequest, user_id, LogConstants.ActionName.INVENTORY_INFO, null
        );
        ResponseVO responseVO;
        try {
            responseVO = inventoryService.getInventoryInfo(user_id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.failure("获取用户优惠券库存失败！");
        }
        return responseVO;
    }

    /**
     * 用户领取优惠券
     */
    @PostMapping("/user/receive")
    public ResponseVO receivePassTemplate(@RequestBody GainPassTemplateRequestVO gainPassTemplateRequestVO) {
        LogGenerator.writeLog(
                httpServletRequest, gainPassTemplateRequestVO.getUserId(),
                LogConstants.ActionName.RECEIVE_PASS_TEMPLATE,
                gainPassTemplateRequestVO
        );
        ResponseVO responseVO;
        try {
            responseVO = passTemplateService.gainPassTemplate(gainPassTemplateRequestVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.failure("用户领取优惠券失败！");
        }
        return responseVO;
    }

    /**
     * 创建用户评论
     *
     * @param feedbackVO {@link FeedbackVO}
     */
    @PostMapping(path = "/user/feedback/create")
    public ResponseVO createFeedback(@RequestBody FeedbackVO feedbackVO) {
        LogGenerator.writeLog(
                httpServletRequest, feedbackVO.getUserId(), LogConstants.ActionName.CREATE_FEEDBAK, feedbackVO
        );

        return feedbackService.createFeedback(feedbackVO);
    }

    /**
     * 创建用户评论
     *
     * @param user_id 用户id
     */
    @GetMapping(path = "/user/feedback")
    public ResponseVO createFeedback(@RequestParam(name = "user_id") Long user_id) {
        LogGenerator.writeLog(
                httpServletRequest, user_id, LogConstants.ActionName.GET_FEEDBAK, null
        );

        return feedbackService.getFeedbacks(user_id);
    }

    /**
     * 全局异常演示
     */
    @GetMapping("/exception")
    ResponseVO exceptionExam() throws Exception {
        throw new Exception("Nice to meet you, isaac.");
    }
}
