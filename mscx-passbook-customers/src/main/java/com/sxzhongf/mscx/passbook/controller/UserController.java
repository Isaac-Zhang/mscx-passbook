package com.sxzhongf.mscx.passbook.controller;

import com.sxzhongf.mscx.passbook.log.LogConstants;
import com.sxzhongf.mscx.passbook.log.LogGenerator;
import com.sxzhongf.mscx.passbook.service.IUserService;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import com.sxzhongf.mscx.passbook.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * UserController for 用户服务控制器
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/6/5
 */
@Slf4j
@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final IUserService userService;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public UserController(IUserService userService, HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * 创建用户
     *
     * @param userVO {@link UserVO}
     */
    @PostMapping(path = "/register")
    public ResponseVO createUser(@RequestBody UserVO userVO) {
        LogGenerator.writeLog(
                httpServletRequest, -1L, LogConstants.ActionName.CREATE_USER, userVO
        );
        ResponseVO responseVO;
        try {
            responseVO = userService.createUser(userVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.failure("创建用户服务失败！");
        }
        return responseVO;
    }
}
