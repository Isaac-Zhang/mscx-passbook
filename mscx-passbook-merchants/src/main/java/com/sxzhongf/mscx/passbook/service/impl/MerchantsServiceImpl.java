package com.sxzhongf.mscx.passbook.service.impl;

import com.sxzhongf.mscx.passbook.dao.MerchantsDao;
import com.sxzhongf.mscx.passbook.enums.ErrorCodeEnum;
import com.sxzhongf.mscx.passbook.service.IMerchantsService;
import com.sxzhongf.mscx.passbook.vo.CommonResponseVO;
import com.sxzhongf.mscx.passbook.vo.CreateMerchantsRequestVO;
import com.sxzhongf.mscx.passbook.vo.CreateMerchantsResponseVO;
import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * MerchantsServiceImpl for TODO
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/24
 */
@Slf4j
@Service
public class MerchantsServiceImpl implements IMerchantsService {

    private final MerchantsDao merchantsDao;

    @Autowired
    public MerchantsServiceImpl(MerchantsDao merchantsDao) {
        this.merchantsDao = merchantsDao;
    }

    @Override
    @Transactional
    public CommonResponseVO createMerchants(CreateMerchantsRequestVO requestVO) {
        CommonResponseVO responseVO = new CommonResponseVO();
        CreateMerchantsResponseVO merchantsResponseVO = new CreateMerchantsResponseVO();

        ErrorCodeEnum errorCode = requestVO.validate(merchantsDao);

        if (errorCode != ErrorCodeEnum.SUCCESS) {
            merchantsResponseVO.setId(-1);
            responseVO.setErrorCode(errorCode.getCode());
            responseVO.setErrorMsg(errorCode.getDesc());
        } else {
            merchantsResponseVO.setId(merchantsDao.save(requestVO.toMerchants()).getId());
        }
        responseVO.setData(merchantsResponseVO);
        return responseVO;
    }

    @Override
    public CommonResponseVO buildMerchantsInfoById(Integer id) {
        return null;
    }

    @Override
    public CommonResponseVO launchPassTemplate(PassTemplateVO passTemplateVO) {
        return null;
    }
}
