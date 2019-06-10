package com.sxzhongf.mscx.passbook.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * InventoryServiceTest for TODO
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/6/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InventoryServiceTest extends AbstractServiceTest {

    @Autowired
    private IInventoryService inventoryService;

    @Test
    public void testGetInventoryInfo() throws Exception {
        ResponseVO responseVO = inventoryService.getInventoryInfo(user_id);
        System.out.printf("获取库存信息Start：%s", JSON.toJSONString(responseVO,
                SerializerFeature.DisableCircularReferenceDetect));

        assert JSON.toJSONString(responseVO).contains("众福-2") == true;

        System.out.printf("获取库存信息End：%s", JSON.toJSONString(responseVO,
                SerializerFeature.DisableCircularReferenceDetect));
    }
}
