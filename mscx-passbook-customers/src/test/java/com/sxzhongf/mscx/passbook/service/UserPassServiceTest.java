package com.sxzhongf.mscx.passbook.service;

import com.alibaba.fastjson.JSON;
import com.sxzhongf.mscx.passbook.vo.PassVO;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * UserPassServiceTest for 用户优惠券service测试
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/6/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserPassServiceTest extends AbstractServiceTest {

    @Autowired
    private IUserPassService userPassService;

    @Test
    public void testGetUserPassInfo() throws Exception {
        System.out.println(JSON.toJSONString(
                "获取用户优惠券信息：" + userPassService.getUserPassInfo(user_id)
        ));
    }

    @Test
    public void testGetUsedUserPassInfo() throws Exception {
        System.out.println(JSON.toJSONString(
                "获取用户已使用优惠券信息：" + userPassService.getUserUsedPassInfo(user_id)
        ));
    }

    @Test
    public void testGetAllUserPassInfo() throws Exception {
        System.out.println(JSON.toJSONString(
                "获取用户全部优惠券信息：" + userPassService.getUserAllPassInfo(user_id)
        ));
    }

    /**
     * [PassInfoVO(passVO=PassVO(userId=398711, rowKey=null, templateId=d8f14e90ed5a8d2daf86dd3f05e7d17e,
     * token=token-3, assignedDate=Mon Jun 10 00:00:00 CST 2019, consumeDate=null),
     * passTemplateVO=PassTemplateVO(id=18, title=title: 众福-2, summary=简介: 众福优惠券,
     * desc=详情: 众福优惠券, limit=9992, hasToken=true, background=2, start=Fri May 31 00:00:00 CST 2019,
     * end=Thu Jun 20 00:00:00 CST 2019), merchants=Merchants(id=18, name=众福kU, logoUrl=www.sxzhongf.com,
     * businessLicenseUrl=www.sxzhongf.com, phone=0913-7196961, address=陕西渭南蒲城县, isAudit=true))]
     */

    @Test
    public void testUseUserPass() {
        PassVO passVO = new PassVO();
        passVO.setUserId(user_id);
        passVO.setTemplateId("d8f14e90ed5a8d2daf86dd3f05e7d17e");
        ResponseVO responseVO = userPassService.userUsePassInfo(passVO);
        System.out.println("用户使用优惠券结果：" + JSON.toJSONString(responseVO));
        assert responseVO.getErrorCode() == 0;
    }
}

