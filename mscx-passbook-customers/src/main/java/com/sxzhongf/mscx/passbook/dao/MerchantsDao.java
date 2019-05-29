package com.sxzhongf.mscx.passbook.dao;

import com.sxzhongf.mscx.passbook.entity.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * MerchantsDao for 数据库操作接口实现
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/29
 */
public interface MerchantsDao extends JpaRepository<Merchants,Integer> {

    /**
     * 通过id获取商户对象
     * @param id 商户id
     * @return {@link Merchants}
     */
    @Override
    public Optional<Merchants> findById(Integer id);

    /**
     * 根据商户名称获取商户
     * @param name 商户名称
     * @return {@link Merchants}
     */
    Merchants findByName(String name);

    /**
     * 根据ids集合获取商户列表
     * @param integers 商户ids
     * @return {@link List<Merchants>}
     */
    @Override
    List<Merchants> findAllById(Iterable<Integer> integers);
}
