package com.sxzhongf.mscx.passbook.vo;

import com.sxzhongf.mscx.passbook.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PassInfoVO for 用户领取的优惠券信息对象
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassInfoVO {

    /**
     * 优惠券信息
     */
    private PassVO passVO;

    /**
     * 优惠券模版
     */
    private PassTemplateVO passTemplateVO;

    /**
     * 优惠券所属对应商户
     */
    private Merchants merchants;
}
