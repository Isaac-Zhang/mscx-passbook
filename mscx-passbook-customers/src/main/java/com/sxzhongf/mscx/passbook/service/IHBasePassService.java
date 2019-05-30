package com.sxzhongf.mscx.passbook.service;

import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;

/**
 * IHBasePassService for HBase 优惠券 service
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
public interface IHBasePassService {

    /**
     * 将 {@link PassTemplateVO} 写入HBase
     *
     * @param passTemplateVO {@link PassTemplateVO}
     * @return true/false
     */
    boolean launchPassTemplateToHBase(PassTemplateVO passTemplateVO);
}
