package com.sxzhongf.mscx.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * PassTemplateVO for 投放的优惠券对象
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassTemplateVO {

    /**
     * 所属商户id
     */
    private Integer id;

    private String title;
    /**
     * 摘要
     */
    private String summary;
    /**
     * 详情
     */
    private String desc;
    private Long limit;
    /**
     * 优惠券是否有token，用于商户核销
     */
    private String hasToken;
    private Integer background;
    private Date start;
    private Date end;

}
