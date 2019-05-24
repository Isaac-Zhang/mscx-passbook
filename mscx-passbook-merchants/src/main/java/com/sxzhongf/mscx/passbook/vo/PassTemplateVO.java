package com.sxzhongf.mscx.passbook.vo;

import com.sxzhongf.mscx.passbook.dao.MerchantsDao;
import com.sxzhongf.mscx.passbook.enums.ErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * PassTemplate for 优惠券对象
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassTemplate {

    /**
     * 所属商户id
     */
    private Integer id;

    /**
     * 优惠券标题
     */
    private String title;

    /**
     * 优惠券摘要信息
     */
    private String summary;

    /**
     * 优惠券详细
     */
    private String desc;

    /**
     * 最大个数限制
     */
    private long limit;

    /**
     * 优惠券是否有token信息，用户商户核销
     * token 存储于redis Set 中，每次领取从redis中获取
     */
    private Boolean hasToken;

    /**
     * 优惠券背景色
     */
    private Integer background;

    /**
     * 优惠券开始时间
     */
    private Date start;

    /**
     * 优惠券结束时间
     */
    private Date end;

    /**
     * 校验优惠券的有效性
     *
     * @param merchantsDao {@link MerchantsDao}
     * @return {@link ErrorCodeEnum}
     */
    public ErrorCodeEnum validate(MerchantsDao merchantsDao) {
        if (null == merchantsDao.findById(id)) {
            return ErrorCodeEnum.MERCHANT_NOT_EXIST;
        }

        return ErrorCodeEnum.SUCCESS;
    }
}
