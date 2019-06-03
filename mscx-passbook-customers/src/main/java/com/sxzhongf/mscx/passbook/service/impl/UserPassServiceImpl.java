package com.sxzhongf.mscx.passbook.service.impl;

import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import com.sxzhongf.mscx.passbook.constant.Constants;
import com.sxzhongf.mscx.passbook.dao.MerchantsDao;
import com.sxzhongf.mscx.passbook.entity.Merchants;
import com.sxzhongf.mscx.passbook.service.IUserPassService;
import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;
import com.sxzhongf.mscx.passbook.vo.PassVO;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * UserPassServiceImpl for 用户优惠券功能实现
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/6/3
 */
@Slf4j
@Service
public class UserPassServiceImpl implements IUserPassService {

    /**
     * HBase 客户端
     */
    private HbaseTemplate hbaseTemplate;

    /**
     * 获取商户操作类
     */
    private MerchantsDao merchantsDao;

    @Autowired
    public UserPassServiceImpl(HbaseTemplate hbaseTemplate, MerchantsDao merchantsDao) {
        this.hbaseTemplate = hbaseTemplate;
        this.merchantsDao = merchantsDao;
    }

    @Override
    public ResponseVO getUserPassInfo(Long userId) throws Exception {
        return null;
    }

    @Override
    public ResponseVO getUserUsedPassInfo(Long userId) throws Exception {
        return null;
    }

    @Override
    public ResponseVO getUserAllPassInfo(Long userId) throws Exception {
        return null;
    }

    @Override
    public ResponseVO userUsePassInfo(PassVO passVO) {
        return null;
    }

    /**
     * 根据获取到的优惠券列表 构造 Map
     *
     * @param passVOS {@link PassVO}
     * @return Map
     * @throws Exception error
     */
    private Map<String, PassTemplateVO> buildPassTemplateMap(List<PassVO> passVOS) throws Exception {
        String[] patterns = new String[]{"yyyy-MM-dd"};

        byte[] FAMILY_B = Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B);
        byte[] ID = Bytes.toBytes(Constants.PassTemplateTable.ID);
        byte[] TITLE = Bytes.toBytes(Constants.PassTemplateTable.TITLE);
        byte[] SUMMARY = Bytes.toBytes(Constants.PassTemplateTable.SUMMARY);
        byte[] DESC = Bytes.toBytes(Constants.PassTemplateTable.DESC);
        byte[] HAS_TOKEN = Bytes.toBytes(Constants.PassTemplateTable.HAS_TOKEN);
        byte[] BACKGROUND = Bytes.toBytes(Constants.PassTemplateTable.BACKGROUND);

        byte[] FAMILY_C = Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C);
        byte[] LIMIT = Bytes.toBytes(Constants.PassTemplateTable.LIMIT);
        byte[] START = Bytes.toBytes(Constants.PassTemplateTable.START);
        byte[] END = Bytes.toBytes(Constants.PassTemplateTable.END);

        //获取模版id
        List<String> templateIds = passVOS.stream().map(
                PassVO::getTemplateId
        ).collect(Collectors.toList());

        //组建Get对象，使用templateId 作为Rowkey，根据HBase Client就可以直接获取对象数据
        List<Get> templateGets = new ArrayList<>(templateIds.size());
        templateIds.forEach(t -> templateGets.add(new Get(Bytes.toBytes(t))));

        /**
         * 获取到的原始数据类型都是 {@link Result[]
         * 1. 获得数据库连接
         * 2. 获得表实例
         * 3. 获得Get对象
         */
        Result[] templateResults = hbaseTemplate.getConnection()
                                                .getTable(TableName.valueOf(
                                                        Constants.PassTemplateTable.TABLE_NAME
                                                )).get(templateGets);

        // 构造PassTemplateId -> PassTemplate Object 的 Map,用于构造PassInfoVO
        Map<String, PassTemplateVO> passTemplateVOMap = new HashMap<>();

        for (Result item : templateResults) {
            PassTemplateVO passTemplateVO = new PassTemplateVO();
            passTemplateVO.setId(Bytes.toInt(item.getValue(FAMILY_B, ID)));
            passTemplateVO.setTitle(Bytes.toString(item.getValue(FAMILY_B, TITLE)));
            passTemplateVO.setSummary(Bytes.toString(item.getValue(FAMILY_B, SUMMARY)));
            passTemplateVO.setDesc(Bytes.toString(item.getValue(FAMILY_B, DESC)));
            passTemplateVO.setHasToken(Bytes.toString(item.getValue(FAMILY_B, HAS_TOKEN)));
            passTemplateVO.setBackground(Bytes.toInt(item.getValue(FAMILY_B, BACKGROUND)));

            passTemplateVO.setLimit(Bytes.toLong(item.getValue(FAMILY_C, LIMIT)));
            passTemplateVO.setStart(DateUtils.parseDate(Bytes.toString(item.getValue(FAMILY_C, START)), patterns));
            passTemplateVO.setEnd(DateUtils.parseDate(Bytes.toString(item.getValue(FAMILY_C, END)), "yyyy-MM-dd"));

            // 获取Rowkey作为Map key
            passTemplateVOMap.put(Bytes.toString(item.getRow()), passTemplateVO);
        }
        return passTemplateVOMap;
    }

    /**
     * 根据PassTemplate 对象构建 Merchants对象 Map
     *
     * @param passTemplateVOS {@link List<PassTemplateVO>}
     * @return Map
     */
    private Map<Integer, Merchants> buildMerchantsMap(List<PassTemplateVO> passTemplateVOS) {
        Map<Integer, Merchants> merchantsMap = new HashMap<>();
        List<Integer> merchantsIds = passTemplateVOS.stream().map(
                PassTemplateVO::getId
        ).collect(Collectors.toList());

        // 获取Mysql中商户信息
        List<Merchants> merchantsList = merchantsDao.findAllById(merchantsIds);

        // 根据获取到的商户信息，填充Map对象
        merchantsList.forEach(m -> merchantsMap.put(m.getId(), m));
        return merchantsMap;
    }
}
