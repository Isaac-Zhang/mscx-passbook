package com.sxzhongf.mscx.passbook.service;

import com.sxzhongf.mscx.passbook.vo.CommonResponseVO;
import com.sxzhongf.mscx.passbook.vo.CreateMerchantsRequestVO;
import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;

/**
 * IMerchantsService for 商户服务接口
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/5/24
 */
public interface IMerchantsService {

    /**
     * 创建商户
     *
     * @param merchantsRequestVO {@link CreateMerchantsRequestVO}
     * @return {@link CommonResponseVO}
     */
    CommonResponseVO createMerchants(CreateMerchantsRequestVO merchantsRequestVO);

    /**
     * 根据id构造商户信息
     *
     * @param id {@link Integer}
     * @return {@link CommonResponseVO}
     */
    CommonResponseVO buildMerchantsInfoById(Integer id);

    /**
     * 投放优惠券
     *
     * @param passTemplateVO {@link PassTemplateVO} 优惠券对象
     * @return {@link CommonResponseVO}
     */
    CommonResponseVO launchPassTemplate(PassTemplateVO passTemplateVO);
}
