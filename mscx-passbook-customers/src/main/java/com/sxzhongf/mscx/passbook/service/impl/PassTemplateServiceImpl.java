package com.sxzhongf.mscx.passbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import com.sxzhongf.mscx.passbook.constant.Constants;
import com.sxzhongf.mscx.passbook.mapper.PassTemplateRowMapper;
import com.sxzhongf.mscx.passbook.service.IPassTemplateService;
import com.sxzhongf.mscx.passbook.utils.RowKeyGenerateUtils;
import com.sxzhongf.mscx.passbook.vo.GainPassTemplateRequestVO;
import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PassTemplateServiceImpl for 优惠券模版服务实现
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/6/4
 */
@Slf4j
@Service
public class PassTemplateServiceImpl implements IPassTemplateService {

    private final HbaseTemplate hbaseTemplate;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public PassTemplateServiceImpl(HbaseTemplate hbaseTemplate, StringRedisTemplate redisTemplate) {
        this.hbaseTemplate = hbaseTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ResponseVO gainPassTemplate(GainPassTemplateRequestVO requestVO) throws IOException {
        //判断优惠券是否可用
        PassTemplateVO passTemplateVO = null;
        String passTemplateId = RowKeyGenerateUtils.generatePassTemplateRowKey(requestVO.getPassTemplateVO());
        try {
            passTemplateVO = hbaseTemplate.get(Constants.PassTemplateTable.TABLE_NAME,
                    passTemplateId, new PassTemplateRowMapper());
        } catch (Exception ex) {
            log.error("Get PassTemplate Error: {}", JSON.toJSONString(requestVO.getPassTemplateVO()));
            return ResponseVO.failure("当前优惠券商户不存在！");
        }

        if (null != passTemplateVO) {
            if (passTemplateVO.getLimit() < 1 && passTemplateVO.getLimit() != -1) {
                log.error("PassTemplate to MAX Error: {}", JSON.toJSONString(requestVO.getPassTemplateVO()));
                return ResponseVO.failure("当前优惠券已经达到领取上限！");
            }
        }

        Date currentDate = new Date();
        if (!(currentDate.getTime() >= passTemplateVO.getStart().getTime()
                && currentDate.getTime() < passTemplateVO.getEnd().getTime())) {
            log.error("PassTemplate Date Error: {}", JSON.toJSONString(requestVO.getPassTemplateVO()));
            return ResponseVO.failure("当前优惠券不在使用有效期！");
        }
        //减掉优惠券的limit
        if (passTemplateVO.getLimit() != -1) {
            List<Mutation> datas = new ArrayList<>();
            Put put = new Put(Bytes.toBytes(RowKeyGenerateUtils.generatePassTemplateRowKey(passTemplateVO)));
            put.addColumn(Constants.PassTemplateTable.FAMILY_C.getBytes(),
                    Constants.PassTemplateTable.LIMIT.getBytes(),
                    Bytes.toBytes(passTemplateVO.getLimit() - 1));

            datas.add(put);
            hbaseTemplate.saveOrUpdates(Constants.PassTemplateTable.TABLE_NAME, datas);
        }

        // 将优惠券保存到用户优惠券表
        boolean success = addPassForUser(new GainPassTemplateRequestVO(requestVO.getUserId(), passTemplateVO),
                passTemplateVO.getId(), passTemplateId);

        return success ? ResponseVO.success() : ResponseVO.failure("领取优惠券失败！");
    }

    /**
     * 为用户添加优惠券
     *
     * @param requestVO      {@link GainPassTemplateRequestVO}
     * @param merchantsId    商户id
     * @param passTemplateId 优惠券id
     * @return true/false
     * @throws IOException 写入错误
     */
    private boolean addPassForUser(GainPassTemplateRequestVO requestVO, Integer merchantsId, String passTemplateId)
            throws IOException {
        byte[] FIMILY_I = Constants.PassTable.FAMILY_I.getBytes();
        byte[] CONSUME_DATE = Constants.PassTable.CON_DATE.getBytes();
        byte[] TEMPLATE_ID = Constants.PassTable.TEMPLATE_ID.getBytes();
        byte[] USER_ID = Constants.PassTable.USER_ID.getBytes();
        byte[] TOKEN = Constants.PassTable.TOKEN.getBytes();
        byte[] ASSIGNED_DATE = Constants.PassTable.ASSIGNED_DATE.getBytes();

        List<Mutation> datas = new ArrayList<>();
        Put put = new Put(Bytes.toBytes(RowKeyGenerateUtils.generatePassRowKey(requestVO)));
        put.addColumn(FIMILY_I, USER_ID, Bytes.toBytes(requestVO.getUserId()));
        put.addColumn(FIMILY_I, TEMPLATE_ID, Bytes.toBytes(passTemplateId));
        if (requestVO.getPassTemplateVO().isHasToken()) {
            //根据redis key获取token信息,使用一个减少一个
            String token = redisTemplate.opsForSet().pop(passTemplateId);
            if (null == token) {
                log.error("token 不存在: {}", passTemplateId);
            }
            recordTokenToFile(merchantsId, passTemplateId, token);
            put.addColumn(FIMILY_I, TOKEN, Bytes.toBytes(token));
        } else {
            put.addColumn(FIMILY_I, TOKEN, Bytes.toBytes(-1));
        }
        put.addColumn(FIMILY_I, ASSIGNED_DATE,
                Bytes.toBytes(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date())));
        put.addColumn(FIMILY_I, CONSUME_DATE, Bytes.toBytes(-1));

        datas.add(put);

        hbaseTemplate.saveOrUpdates(Constants.PassTable.TABLE_NAME, datas);
        return true;
    }

    /**
     * 将已使用的token写入记录到文件中
     *
     * @param merchantId     商户id
     * @param passTemplateId 优惠券id
     * @param token          分配的优惠券token
     * @throws IOException 写入异常
     */
    private void recordTokenToFile(Integer merchantId, String passTemplateId, String token) throws IOException {
        Files.write(
                Paths.get(Constants.TOKEN_DIR, String.valueOf(merchantId),
                        passTemplateId + Constants.USED_TOKEN_SUFFIX),
                (token + "\n").getBytes(),
                StandardOpenOption.APPEND
        );
    }
}
