package com.sxzhongf.mscx.passbook.service.impl;

import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import com.sxzhongf.mscx.passbook.constant.Constants;
import com.sxzhongf.mscx.passbook.service.IUserService;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import com.sxzhongf.mscx.passbook.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * UserServiceImpl for 用户服务实现
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    /**
     * HBase client
     */
    private HbaseTemplate hbaseTemplate;

    /**
     * Redis Client
     */
    private StringRedisTemplate redisTemplate;

    @Autowired
    public UserServiceImpl(HbaseTemplate hbaseTemplate, StringRedisTemplate redisTemplate) {
        this.hbaseTemplate = hbaseTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ResponseVO createUser(UserVO userVO) throws Exception {

        // hbase 中user表的B列族
        byte[] FAMILY_B = Constants.UserTable.FAMILY_B.getBytes();
        byte[] NAME = Constants.UserTable.NAME.getBytes();
        byte[] AGE = Constants.UserTable.AGE.getBytes();
        byte[] SEX = Constants.UserTable.SEX.getBytes();
        // hbase 中user表的O列族
        byte[] FAMILY_O = Constants.UserTable.FAMILY_O.getBytes();
        byte[] PHONE = Constants.UserTable.PHONE.getBytes();
        byte[] ADDRESS = Constants.UserTable.ADDRESS.getBytes();

        // 获取Redis中当前用户数量
        Long currentUserCount = redisTemplate.opsForValue().increment(Constants.USER_COUNT_REDIS_KEY, 1);
        Long userId = generateUserId(currentUserCount);

        List<Mutation> datas = new ArrayList<>();
        Put putObject = new Put(Bytes.toBytes(userId));
        putObject.addColumn(FAMILY_B, NAME, Bytes.toBytes(userVO.getBaseInfo().getName()));
        putObject.addColumn(FAMILY_B, AGE, Bytes.toBytes(userVO.getBaseInfo().getAge()));
        putObject.addColumn(FAMILY_B, SEX, Bytes.toBytes(userVO.getBaseInfo().getSex()));
        putObject.addColumn(FAMILY_O, PHONE, Bytes.toBytes(userVO.getOtherInfo().getPhone()));
        putObject.addColumn(FAMILY_O, ADDRESS, Bytes.toBytes(userVO.getOtherInfo().getAddress()));
        datas.add(putObject);

        log.info("CreateUser: {} start...", datas);
        // 保存数据到HBase
        hbaseTemplate.saveOrUpdates(Constants.UserTable.TABLE_NAME, datas);
        userVO.setId(userId);
        log.info("CreateUser: {} end.", datas);

        return new ResponseVO(userVO);
    }

    /**
     * 用户id生成器
     *
     * @param prefix 当前用户数量
     * @return 用户id
     */
    private Long generateUserId(Long prefix) {
        String suffix = RandomStringUtils.randomNumeric(5);
        return Long.valueOf(prefix + suffix);
    }
}
