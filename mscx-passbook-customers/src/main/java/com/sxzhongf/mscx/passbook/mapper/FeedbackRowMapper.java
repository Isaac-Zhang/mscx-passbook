package com.sxzhongf.mscx.passbook.mapper;

import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import com.sxzhongf.mscx.passbook.constant.Constants;
import com.sxzhongf.mscx.passbook.vo.FeedbackVO;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * FeedbackRowMapper for HBase Feedback ORM映射
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
public class FeedbackRowMapper implements RowMapper<FeedbackVO> {

    // 定义HBase FeedbackVO 表的列族I中的字节定义
    private static byte[] FAMILY_I = Constants.FeedbackTable.FAMILY_I.getBytes();
    private static byte[] USER_ID = Constants.FeedbackTable.USER_ID.getBytes();
    private static byte[] TEMPLATE_ID = Constants.FeedbackTable.TEMPLATE_ID.getBytes();
    private static byte[] TYPE = Constants.FeedbackTable.TYPE.getBytes();
    private static byte[] COMMENT = Constants.FeedbackTable.COMMENT.getBytes();

    @Override
    public FeedbackVO mapRow(Result result, int rowNum) throws Exception {
        FeedbackVO feedback = new FeedbackVO();
        feedback.setUserId(Bytes.toLong(result.getValue(FAMILY_I, USER_ID)));
        feedback.setComment(Bytes.toString(result.getValue(FAMILY_I, COMMENT)));
        feedback.setTemplateId(Bytes.toString(result.getValue(FAMILY_I, TEMPLATE_ID)));
        feedback.setType(Bytes.toString(result.getValue(FAMILY_I, TYPE)));
        return feedback;
    }
}
