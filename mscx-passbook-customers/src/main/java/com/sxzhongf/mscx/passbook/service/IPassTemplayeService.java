package com.sxzhongf.mscx.passbook.service;

import com.sxzhongf.mscx.passbook.vo.GainPassTemplateRequestVO;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;

/**
 * IPassTemplayeService for 用户优惠券功能service
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/31
 */
public interface IPassTemplayeService {

    /**
     * 用户领取优惠券
     *
     * @param requestVO {@link GainPassTemplateRequestVO}
     * @return {@link ResponseVO}
     * @throws Exception 错误
     */
    ResponseVO gainPassTemplate(GainPassTemplateRequestVO requestVO) throws Exception;
}
