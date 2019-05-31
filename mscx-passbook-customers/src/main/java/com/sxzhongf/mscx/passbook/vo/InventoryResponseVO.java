package com.sxzhongf.mscx.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * InventoryResponseVO for 库存请求响应结果
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseVO {

    /**
     * 用户id,使用用户id标示不同用户可以看到不同优惠券信息
     */
    private Long userId;

    /**
     * 优惠券模版信息
     */
    private List<PassTemplateInfoVO> passTemplateInfoVOS;
}
