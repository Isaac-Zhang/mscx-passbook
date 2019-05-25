package com.sxzhongf.mscx.passbook.controller;

import com.alibaba.fastjson.JSON;
import com.sxzhongf.mscx.passbook.service.IMerchantsService;
import com.sxzhongf.mscx.passbook.vo.CommonResponseVO;
import com.sxzhongf.mscx.passbook.vo.CreateMerchantsRequestVO;
import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * MerchantsController for 商户服务web提供
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/24
 */
@Slf4j
@RestController
@RequestMapping(path = "/merchants")
public class MerchantsController {

    /**
     * 商户服务接口注入
     *
     */
    private final IMerchantsService merchantsService;

    @Autowired
    public MerchantsController(IMerchantsService merchantsService) {
        this.merchantsService = merchantsService;
    }

    @PostMapping("/create")
    public CommonResponseVO createMerchants(@RequestBody CreateMerchantsRequestVO requestVO) {
        log.info("创建商户信息: {}", JSON.toJSONString(requestVO));
        return merchantsService.createMerchants(requestVO);
    }

    @GetMapping("/{id}")
    public CommonResponseVO getMerchants(@PathVariable Integer id) {
        CommonResponseVO responseVO = merchantsService.buildMerchantsInfoById(id);
        log.info("获取商户{} 信息: {}", id, JSON.toJSONString(responseVO));
        return responseVO;
    }

    @PostMapping("/launch")
    public CommonResponseVO createMerchants(@RequestBody PassTemplateVO passTemplateVO) {
        log.info("商户发送优惠券信息: {}", JSON.toJSONString(passTemplateVO));
        return merchantsService.launchPassTemplate(passTemplateVO);
    }
}
