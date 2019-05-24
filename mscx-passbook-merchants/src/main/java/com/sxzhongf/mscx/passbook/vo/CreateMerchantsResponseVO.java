package com.sxzhongf.mscx.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreateMerchantsResponseVO for 创建商户响应VO
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMerchantsResponseVO {

    /** 商户 id: 创建失败则为 -1 */
    private Integer id;

}
