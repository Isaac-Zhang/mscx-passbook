package com.sxzhongf.mscx.passbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import com.sxzhongf.mscx.passbook.constant.Constants;
import com.sxzhongf.mscx.passbook.constant.PassStatusEnum;
import com.sxzhongf.mscx.passbook.dao.MerchantsDao;
import com.sxzhongf.mscx.passbook.entity.Merchants;
import com.sxzhongf.mscx.passbook.mapper.PassRowMapper;
import com.sxzhongf.mscx.passbook.service.IUserPassService;
import com.sxzhongf.mscx.passbook.vo.PassInfoVO;
import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;
import com.sxzhongf.mscx.passbook.vo.PassVO;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
        return getPassInfoByStatus(userId, PassStatusEnum.UNUSED);
    }

    @Override
    public ResponseVO getUserUsedPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatusEnum.USED);
    }

    @Override
    public ResponseVO getUserAllPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatusEnum.ALL);
    }

    /**
     * 1. 要使用优惠券，就得先找到它
     * 2. 对优惠券进行校验
     * 3. 更新数据库
     */
    @Override
    public ResponseVO userUsePassInfo(PassVO passVO) {
        byte[] FIMILY_I = Constants.PassTable.FAMILY_I.getBytes();
        byte[] CONSUME_DATE = Constants.PassTable.CON_DATE.getBytes();
        byte[] TEMPLATE_ID = Constants.PassTable.TEMPLATE_ID.getBytes();

        //根据userid 构建行键前缀,使用前缀来快速定位HBase中的数据
        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(String.valueOf(passVO.getUserId())).reverse().toString());
        Scan scan = new Scan();
        List<Filter> filters = new ArrayList<>();
        filters.add(new PrefixFilter(rowPrefix));
        filters.add(new SingleColumnValueFilter(FIMILY_I, TEMPLATE_ID,
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes(passVO.getTemplateId())
        ));
        filters.add(new SingleColumnValueFilter(FIMILY_I, CONSUME_DATE,
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes("-1")
        ));
        scan.setFilter(new FilterList(filters));

        List<PassVO> passVOS = hbaseTemplate.find(Constants.PassTable.TABLE_NAME, scan, new PassRowMapper());

        if (null == passVOS || passVOS.size() < 1) {
            log.error("PassVO Null {}", JSON.toJSONString(passVO));
            return ResponseVO.failure("未找到响应的优惠券信息！");
        }

        //更新数据库中的 消费日期
        List<Mutation> datas = new ArrayList<>();
        Put put = new Put(passVOS.get(0).getRowKey().getBytes());
        put.addColumn(FIMILY_I, CONSUME_DATE,
                Bytes.toBytes(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date())));

        datas.add(put);
        hbaseTemplate.saveOrUpdates(Constants.PassTable.TABLE_NAME, datas);
        return ResponseVO.success();
    }

    /**
     * 根据优惠券状态 获取优惠券信息
     *
     * @param userId 用户id
     * @param status 优惠券状态
     * @return {@link ResponseVO}
     */
    private ResponseVO getPassInfoByStatus(Long userId, PassStatusEnum status) throws Exception {
        // 获取userId的反转（作为行键前缀），为了快速在HBase中索引
        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(String.valueOf(userId)).reverse().toString());

        /**
         * 如果状态是未使用的优惠券，则使用 CompareFilter.CompareOp.EQUAL（标示相等比较器）
         */
        CompareFilter.CompareOp compareOp = status == PassStatusEnum.UNUSED
                ? CompareFilter.CompareOp.EQUAL : CompareFilter.CompareOp.NOT_EQUAL;

        Scan scan = new Scan();

        //多Filter设置
        List<Filter> filters = new ArrayList<>();

        // 1. 行键前缀过滤器，找到特定用户的优惠券
        filters.add(new PrefixFilter(rowPrefix));
        // 2. 基于列单元值的过滤器，找到未使用的优惠券，如果是ALL，直接返回上述结果
        if (status != PassStatusEnum.ALL) {
            //使用列族FAMILY_I 中的字段CON_DATE 和-1进行比较，如果未-1,则标示未消费
            filters.add(new SingleColumnValueFilter(
                    Constants.PassTable.FAMILY_I.getBytes(),
                    Constants.PassTable.CON_DATE.getBytes(),
                    compareOp,
                    Bytes.toBytes("-1")
            ));
        }
        scan.setFilter(new FilterList(filters));

        List<PassVO> passVOList = hbaseTemplate.find(Constants.PassTable.TABLE_NAME,
                scan, new PassRowMapper());
        //获取passTemplate信息
        Map<String, PassTemplateVO> passTemplateVOMap = buildPassTemplateMap(passVOList);
        //获取商户信息


        Map<Integer, Merchants> merchantsMap = buildMerchantsMap(new ArrayList<>(
                passTemplateVOMap.values()));

        List<PassInfoVO> result = new ArrayList<>();
        for (PassVO passVO : passVOList) {
            PassTemplateVO passTemplateVO = passTemplateVOMap.getOrDefault(passVO.getTemplateId(), null);
            if (null == passTemplateVO) {
                log.error("PassTemplate Null : {}", passVO.getTemplateId());
                continue;
            }

            Merchants merchant = merchantsMap.getOrDefault(passTemplateVO.getId(), null);
            if (null == merchant) {
                log.error("Merchants Null : {}", passTemplateVO.getId());
                continue;
            }

            result.add(new PassInfoVO(passVO, passTemplateVO, merchant));
        }

        return new ResponseVO(result);
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
            passTemplateVO.setHasToken(Bytes.toBoolean(item.getValue(FAMILY_B, HAS_TOKEN)));
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
