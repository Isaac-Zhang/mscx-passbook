package com.sxzhongf.mscx.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GainPassTemplateRequestVO for 用户领取优惠券请求对象
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GainPassTemplateRequestVO {

    private Long userId;

    /**
     * 用户领取的那一张优惠券
     */
    private PassTemplateVO passTemplateVO;

}
