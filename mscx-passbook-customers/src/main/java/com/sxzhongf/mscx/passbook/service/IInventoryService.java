package com.sxzhongf.mscx.passbook.service;

import com.sxzhongf.mscx.passbook.vo.ResponseVO;

/**
 * IInventoryService for 获取库存信息
 * 只返回用户没有领取的库存
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/5/31
 */
public interface IInventoryService {

    /**
     * 获取库存信息
     *
     * @param userId 使用userId可以起到过滤信息作用
     * @return {@link ResponseVO}
     * @throws Exception 异常抛出
     */
    ResponseVO getInventoryInfo(Long userId) throws Exception;
}
