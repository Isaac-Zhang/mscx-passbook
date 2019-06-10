package com.sxzhongf.mscx.passbook.service;

import com.alibaba.fastjson.JSON;
import com.sxzhongf.mscx.passbook.vo.GainPassTemplateRequestVO;
import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * PassTemplateServiceTest for TODO
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/6/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PassTemplateServiceTest extends AbstractServiceTest {

    @Autowired
    private IPassTemplateService passTemplateService;

    @Test
    public void testGainPassTemplate() throws Exception {
        PassTemplateVO passTemplateVO = new PassTemplateVO();
        passTemplateVO.setHasToken(true);
        passTemplateVO.setTitle("title: 众福-2");
        passTemplateVO.setId(18);

        GainPassTemplateRequestVO requestModel = new GainPassTemplateRequestVO();
        requestModel.setPassTemplateVO(passTemplateVO);
        requestModel.setUserId(user_id);

        ResponseVO responseVO = passTemplateService.gainPassTemplate(requestModel);

        assert responseVO.getErrorCode() == 0;
    }
}
