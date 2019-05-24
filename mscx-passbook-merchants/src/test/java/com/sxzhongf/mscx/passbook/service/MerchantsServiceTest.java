package com.sxzhongf.mscx.passbook.service;

import com.alibaba.fastjson.JSON;
import com.sxzhongf.mscx.passbook.vo.CreateMerchantsRequestVO;
import net.bytebuddy.utility.RandomString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

/**
 * MerchantsServiceTest for 商户服务测试类
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MerchantsServiceTest {

    @Autowired
    private IMerchantsService merchantsService;

    /** 添加上 {@link Transactional},系统会探测到是测试方法
     * ，添加数据成功后，会自动回滚事务*/
    @Test
    @Transactional
    public void testCreateMerchantsSuccess() {
        CreateMerchantsRequestVO requestVO = new CreateMerchantsRequestVO();

        requestVO.setName("众福" + RandomString.make(2));
        requestVO.setLogoUrl("www.sxzhongf.com");
        requestVO.setBusinessLicenseUrl("www.sxzhongf.com");
        requestVO.setPhone("0913-7196961");
        requestVO.setAddress("陕西渭南蒲城县");

        System.out.println(JSON.toJSONString(merchantsService.createMerchants(requestVO)));
    }

    @Test
    public void testCreateMerchantsFail() {
        CreateMerchantsRequestVO requestVO = new CreateMerchantsRequestVO();

        requestVO.setName("众福");
        requestVO.setLogoUrl("www.sxzhongf.com");
        requestVO.setBusinessLicenseUrl("www.sxzhongf.com");
        requestVO.setPhone("0913-7196961");
        requestVO.setAddress("陕西渭南蒲城县");

        System.out.println(JSON.toJSONString(merchantsService.createMerchants(requestVO)));
    }
}
