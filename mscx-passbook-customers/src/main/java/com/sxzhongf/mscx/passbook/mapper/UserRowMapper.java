package com.sxzhongf.mscx.passbook.mapper;

import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import com.sxzhongf.mscx.passbook.constant.Constants;
import com.sxzhongf.mscx.passbook.vo.UserVO;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;


/**
 * UserRowMapper for 定义HBase User Row To User Object
 * ORM映射实现
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/29
 */
public class UserRowMapper implements RowMapper<UserVO> {

    // 定义HBase User 表的列族B中的字节定义
    private static byte[] FAMILY_B = Constants.UserTable.FAMILY_B.getBytes();
    private static byte[] NAME = Constants.UserTable.NAME.getBytes();
    private static byte[] AGE = Constants.UserTable.AGE.getBytes();
    private static byte[] SEX = Constants.UserTable.SEX.getBytes();

    // 定义HBase User 表的列族O中的字节定义
    private static byte[] FAMILY_O = Constants.UserTable.FAMILY_O.getBytes();
    private static byte[] PHONE = Constants.UserTable.PHONE.getBytes();
    private static byte[] ADDRESS = Constants.UserTable.ADDRESS.getBytes();

    /**
     * HBase user表的单行数据映射
     *
     * @param result 结果数据
     * @param rowNum 行数
     * @return 用户数据信息
     * @throws {@link Exception}
     */
    @Override
    public UserVO mapRow(Result result, int rowNum) throws Exception {
        UserVO.BaseInfo baseInfo = new UserVO.BaseInfo(
                Bytes.toString(result.getValue(FAMILY_B, NAME)),
                Bytes.toInt(result.getValue(FAMILY_B, AGE)),
                Bytes.toString(result.getValue(FAMILY_B, SEX))

        );
        UserVO.OtherInfo otherInfo = new UserVO.OtherInfo(
                Bytes.toString(result.getValue(FAMILY_O, PHONE)),
                Bytes.toString(result.getValue(FAMILY_O, ADDRESS))

        );

        return new UserVO(Bytes.toLong(result.getRow()), baseInfo, otherInfo);
    }
}
