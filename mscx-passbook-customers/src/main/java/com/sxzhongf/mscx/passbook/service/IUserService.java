package com.sxzhongf.mscx.passbook.service;

import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import com.sxzhongf.mscx.passbook.vo.UserVO;

/**
 * IUserService for 用户service
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
public interface IUserService {

    /**
     * 创建用户服务
     * @param userVO {@link UserVO}
     * @return {@link ResponseVO}
     * @throws Exception
     */
    ResponseVO createUser(UserVO userVO) throws Exception;
}
