package com.sxzhongf.mscx.passbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import com.sxzhongf.mscx.passbook.constant.Constants;
import com.sxzhongf.mscx.passbook.mapper.FeedbackRowMapper;
import com.sxzhongf.mscx.passbook.service.IFeedbackService;
import com.sxzhongf.mscx.passbook.utils.RowKeyGenerateUtils;
import com.sxzhongf.mscx.passbook.vo.FeedbackVO;
import com.sxzhongf.mscx.passbook.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * FeedbackServiceImpl for 评论功能实现
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
@Slf4j
@Service
public class FeedbackServiceImpl implements IFeedbackService {

    private HbaseTemplate hbaseTemplate;

    @Autowired
    public FeedbackServiceImpl(HbaseTemplate hbaseTemplate) {
        this.hbaseTemplate = hbaseTemplate;
    }

    @Override
    public ResponseVO createFeedback(FeedbackVO feedbackVO) {
        if (!feedbackVO.validate()) {
            log.error("Feedback Error: {}", JSON.toJSONString(feedbackVO));
            return ResponseVO.failure("Feedback error");
        }
        Put putObject = new Put(Bytes.toBytes(RowKeyGenerateUtils.generateFeedbackRowKey(feedbackVO)));
        putObject.addColumn(
                Bytes.toBytes(Constants.FeedbackTable.FAMILY_I),
                Bytes.toBytes(Constants.FeedbackTable.USER_ID),
                Bytes.toBytes(feedbackVO.getUserId())
        );
        putObject.addColumn(
                Bytes.toBytes(Constants.FeedbackTable.FAMILY_I),
                Bytes.toBytes(Constants.FeedbackTable.TEMPLATE_ID),
                Bytes.toBytes(feedbackVO.getTemplateId())
        );
        putObject.addColumn(
                Bytes.toBytes(Constants.FeedbackTable.FAMILY_I),
                Bytes.toBytes(Constants.FeedbackTable.TYPE),
                Bytes.toBytes(feedbackVO.getType())
        );
        putObject.addColumn(
                Bytes.toBytes(Constants.FeedbackTable.FAMILY_I),
                Bytes.toBytes(Constants.FeedbackTable.COMMENT),
                Bytes.toBytes(feedbackVO.getComment())
        );

        hbaseTemplate.saveOrUpdate(Constants.FeedbackTable.TABLE_NAME, putObject);
        return ResponseVO.success();
    }

    @Override
    public ResponseVO getFeedbacks(Long userId) {

        byte[] reverseUserId = new StringBuilder(String.valueOf(userId)).reverse().toString().getBytes();
        //定义扫描器
        Scan scan = new Scan();
        //为扫描器定义过滤器
        //使用前缀过滤器，是因为我们的HBase userId的存储是用的反转之后的userID作为前缀
        //所以前缀过滤器在扫描的时候，会将前缀相同的数据全部抓取
        scan.setFilter(new PrefixFilter(reverseUserId));

        List<FeedbackVO> feedbackVOS = hbaseTemplate.find(Constants.FeedbackTable.TABLE_NAME, scan, new FeedbackRowMapper());
        return new ResponseVO(feedbackVOS);
    }
}
