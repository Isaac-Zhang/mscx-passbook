package com.sxzhongf.mscx.passbook.service;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * AbstractServiceTest for service测试基类
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/6/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractServiceTest {
    public Long user_id;

    @Before
    public void init() {
        user_id = 398711L;
    }
}
