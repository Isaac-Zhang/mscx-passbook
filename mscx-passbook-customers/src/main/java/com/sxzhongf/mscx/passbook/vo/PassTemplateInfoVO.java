package com.sxzhongf.mscx.passbook.vo;

import com.sxzhongf.mscx.passbook.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * PassTemplateInfoVO for 优惠券模版信息
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassTemplateInfoVO extends PassTemplateVO {

    /**
     * 优惠券模版
     */
    private PassTemplateVO passTemplateVO;

    /**
     * 优惠券所属商户
     */
    private Merchants merchants;
}
