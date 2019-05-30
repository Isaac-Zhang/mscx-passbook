package com.sxzhongf.mscx.passbook.mapper;

import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import com.sxzhongf.mscx.passbook.constant.Constants;
import com.sxzhongf.mscx.passbook.vo.PassVO;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * PassRowMapper for HBase 数据表pass的 ORM映射
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
public class PassRowMapper implements RowMapper<PassVO> {

    // 定义HBase PassVO 表的列族I中的字节定义
    private static byte[] FAMILY_I = Constants.PassTable.FAMILY_I.getBytes();
    private static byte[] USER_ID = Constants.PassTable.USER_ID.getBytes();
    private static byte[] TEMPLATE_ID = Constants.PassTable.TEMPLATE_ID.getBytes();
    private static byte[] TOKEN = Constants.PassTable.TOKEN.getBytes();
    private static byte[] ASSIGNED_DATE = Constants.PassTable.ASSIGNED_DATE.getBytes();
    private static byte[] CONSUME_DATE = Constants.PassTable.CON_DATE.getBytes();

    @Override
    public PassVO mapRow(Result result, int rowNum) throws Exception {
        PassVO pass = new PassVO();
        pass.setUserId(Bytes.toLong(result.getValue(FAMILY_I, USER_ID)));
        pass.setTemplateId(Bytes.toString(result.getValue(FAMILY_I, TEMPLATE_ID)));
        pass.setToken(Bytes.toString(result.getValue(FAMILY_I, TOKEN)));

        String[] patterns = new String[]{"yyyy-MM-dd dd:MM:ss"};
        pass.setAssignedDate(DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_I, ASSIGNED_DATE)), patterns));

        String consume_date = Bytes.toString(result.getValue(FAMILY_I, CONSUME_DATE));
        //判断优惠券是否被消费
        if ("-1".equals(consume_date)) {
            pass.setConsumeDate(null);
        } else {
            pass.setConsumeDate(DateUtils.parseDate(consume_date, patterns));
        }

        return pass;
    }
}
