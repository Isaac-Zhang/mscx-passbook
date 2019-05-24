package com.sxzhongf.mscx.passbook.service;

import com.alibaba.fastjson.JSON;
import com.sxzhongf.mscx.passbook.vo.CreateMerchantsRequestVO;
import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Date;

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

    /**
     * 添加上 {@link Transactional},系统会探测到是测试方法
     * ，添加数据成功后，会自动回滚事务
     * {"data":{"id":19},"errorCode":0,"errorMsg":""} 单数数据库没有该19的数据
     */
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

    /**
     * {"data":{"id":-1},"errorCode":1,"errorMsg":"商户名称重复"}
     */
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

    /**
     * 查找 名称为《众福》的商户
     * {"data":
     * {
     * "address":"陕西渭南蒲城县",
     * "businessLicenseUrl":"www.sxzhongf.com",
     * "id":18,
     * "isAudit":false,
     * "logoUrl":"www.sxzhongf.com",
     * "name":"众福kU",
     * "phone":"0913-7196961"
     * },
     * "errorCode":0,
     * "errorMsg":""
     * }
     */
    @Test
    public void testBuildMerchantsInfoByIdSuccess() {
        System.out.println(JSON.toJSONString(merchantsService.buildMerchantsInfoById(18)));
    }

    /**
     * 查找一个不存在的商户
     * {"errorCode":6,"errorMsg":"商户不存在"}
     */
    @Test
    public void testBuildMerchantsInfoByIdFail() {
        System.out.println(JSON.toJSONString(merchantsService.buildMerchantsInfoById(1)));
    }

    /**
     * 消息投放到kafka测试
     * launchPassTemplate :{"background":2,"desc":"详情: 众福优惠券"
     * ,"end":1559565153495,"hasToken":false,"id":18
     * ,"limit":10000,"start":1558701153495,"summary":"简介: 众福优惠券"
     * ,"title":"title: 众福"}
     */
    @Test
    public void testLaunchPassTemplate() {
        PassTemplateVO passTemplateVO = new PassTemplateVO();
        //设置商户id为18
        passTemplateVO.setId(18);
        passTemplateVO.setTitle("title: 众福");
        passTemplateVO.setSummary("简介: 众福优惠券");
        passTemplateVO.setDesc("详情: 众福优惠券");
        passTemplateVO.setLimit(10000L);
        passTemplateVO.setHasToken(false);
        passTemplateVO.setBackground(2);
        passTemplateVO.setStart(new Date());
        passTemplateVO.setEnd(DateUtils.addDays(new Date(), 10));

        System.out.println(JSON.toJSONString(merchantsService.launchPassTemplate(passTemplateVO)));

    }

}
