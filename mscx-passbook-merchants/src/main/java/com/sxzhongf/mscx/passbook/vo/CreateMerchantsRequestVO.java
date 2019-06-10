package com.sxzhongf.mscx.passbook.vo;

import com.sxzhongf.mscx.passbook.dao.MerchantsDao;
import com.sxzhongf.mscx.passbook.entity.Merchants;
import com.sxzhongf.mscx.passbook.enums.ErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreateMerchantsRequestVO for 创建商户VO
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMerchantsRequestVO {

    private String name;
    private String logoUrl;
    private String businessLicenseUrl;
    private String phone;
    private String address;

    /**
     * 验证请求的有效性
     *
     * @param merchantsDao {@link MerchantsDao}
     * @return {@link ErrorCodeEnum}
     */
    public ErrorCodeEnum validate(MerchantsDao merchantsDao) {

        if (merchantsDao.findByName(name) != null) {
            return ErrorCodeEnum.DUPLICATE_NAME;
        }

        if (null == logoUrl) {
            return ErrorCodeEnum.EMPTY_LOGO;
        }

        if (null == businessLicenseUrl) {
            return ErrorCodeEnum.EMPTY_BUSINESS_LICENSE;
        }

        if (null == phone) {
            return ErrorCodeEnum.ERROR_PHONE;
        }

        if (null == address) {
            return ErrorCodeEnum.EMPTY_ADDRESS;
        }

        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 将请求对象转为商户对象
     *
     * @return {@link Merchants}
     */
    public Merchants toMerchants() {

        Merchants merchants = new Merchants();
        merchants.setName(name);
        merchants.setLogoUrl(logoUrl);
        merchants.setBusinessLicenseUrl(businessLicenseUrl);
        merchants.setPhone(phone);
        merchants.setAddress(address);
        return merchants;
    }
}
