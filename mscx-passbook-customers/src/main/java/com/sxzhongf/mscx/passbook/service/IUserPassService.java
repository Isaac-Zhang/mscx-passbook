package com.sxzhongf.mscx.passbook.service;

import com.sxzhongf.mscx.passbook.vo.PassVO;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;

/**
 * IUserPassService for 获取用户个人优惠券信息
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/5/31
 */
public interface IUserPassService {

    /**
     * 获取用户个人优惠券信息
     *
     * @param userId 用户id
     * @return {@link ResponseVO}
     */
    ResponseVO getUserPassInfo(Long userId) throws Exception;

    /**
     * 获取用户已经使用的优惠券
     *
     * @param userId 用户id
     * @return {@link ResponseVO}
     */
    ResponseVO getUserUsedPassInfo(Long userId) throws Exception;

    /**
     * 获取用户所有优惠券
     *
     * @param userId 用户id
     * @return {@link ResponseVO}
     */
    ResponseVO getUserAllPassInfo(Long userId) throws Exception;

    /**
     * 用户使用优惠券
     *
     * @param passVO {@link PassVO}
     * @return {@link ResponseVO}
     */
    ResponseVO userUsePassInfo(PassVO passVO);
}
