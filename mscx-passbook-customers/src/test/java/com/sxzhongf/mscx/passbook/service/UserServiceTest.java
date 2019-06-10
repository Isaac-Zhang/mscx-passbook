package com.sxzhongf.mscx.passbook.service;

import com.alibaba.fastjson.JSON;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import com.sxzhongf.mscx.passbook.vo.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * UserServiceTest for 用户服务测试用例
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/6/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private IUserService userService;

    @Test
    public void testCreateUser() throws Exception {
        UserVO user = new UserVO();

        user.setBaseInfo(new UserVO.BaseInfo("Isaac", 18, "m"));
        user.setOtherInfo(new UserVO.OtherInfo("15009299224", "果粒城"));

        ResponseVO responseVO = userService.createUser(user);
        assert responseVO.getErrorCode() == 0;

        System.out.println(JSON.toJSONString(responseVO));
    }
}
