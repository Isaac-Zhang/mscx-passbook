package com.sxzhongf.mscx.passbook.dao;

import com.sxzhongf.mscx.passbook.entity.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MerchantsDao for Dao接口
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/24
 */
public interface MerchantsDao extends JpaRepository<Merchants, Integer> {

    /**
     * 根据id 获取商户对象
     *
     * @param id 商户id
     * @return {@link Merchants}
     */
    Merchants getById(Integer id);

    /**
     * 根据name 获取商户对象
     *
     * @param name 商户id
     * @return {@link Merchants}
     */
    Merchants getByName(String name);
}
