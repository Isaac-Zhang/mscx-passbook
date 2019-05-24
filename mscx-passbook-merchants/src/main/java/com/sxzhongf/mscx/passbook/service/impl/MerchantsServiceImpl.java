package com.sxzhongf.mscx.passbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.sxzhongf.mscx.passbook.constant.Constants;
import com.sxzhongf.mscx.passbook.dao.MerchantsDao;
import com.sxzhongf.mscx.passbook.entity.Merchants;
import com.sxzhongf.mscx.passbook.enums.ErrorCodeEnum;
import com.sxzhongf.mscx.passbook.service.IMerchantsService;
import com.sxzhongf.mscx.passbook.vo.CommonResponseVO;
import com.sxzhongf.mscx.passbook.vo.CreateMerchantsRequestVO;
import com.sxzhongf.mscx.passbook.vo.CreateMerchantsResponseVO;
import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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

    /**
     * Merchants 数据库接口
     */
    private final MerchantsDao merchantsDao;

    /**
     * kafka 客户端
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MerchantsServiceImpl(MerchantsDao merchantsDao, KafkaTemplate<String, String> kafkaTemplate) {
        this.merchantsDao = merchantsDao;
        this.kafkaTemplate = kafkaTemplate;
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
        CommonResponseVO responseVO = new CommonResponseVO();
        Merchants merchants = merchantsDao.getById(id);
        if (null == merchants) {
            responseVO.setErrorCode(ErrorCodeEnum.MERCHANT_NOT_EXIST.getCode());
            responseVO.setErrorMsg(ErrorCodeEnum.MERCHANT_NOT_EXIST.getDesc());
        }
        responseVO.setData(merchants);
        return responseVO;
    }

    @Override
    public CommonResponseVO launchPassTemplate(PassTemplateVO passTemplateVO) {

        CommonResponseVO responseVO = new CommonResponseVO();
        ErrorCodeEnum errorCode = passTemplateVO.validate(merchantsDao);
        if (errorCode != ErrorCodeEnum.SUCCESS) {
            responseVO.setErrorCode(errorCode.getCode());
            responseVO.setErrorMsg(errorCode.getDesc());
        } else {
            String passTemplate = JSON.toJSONString(passTemplateVO);
            kafkaTemplate.send(
                    Constants.TEMPLATE_TOPIC,
                    Constants.TEMPLATE_TOPIC,
                    passTemplate
            );
            log.info("launchPassTemplate :{}", passTemplate);
        }

        return responseVO;
    }
}
