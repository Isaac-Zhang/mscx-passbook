package com.sxzhongf.mscx.passbook.utils;

import com.sxzhongf.mscx.passbook.vo.FeedbackVO;
import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * RowKeyGenerateUtils for HBase Rowkey 生成器工具类
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
@Slf4j
public class RowKeyGenerateUtils {

    /**
     * 根据PassTemplate 生成Rowkey
     *
     * @param passTemplate {@link PassTemplateVO}
     * @return {@link String} RowKey
     */
    public static String generatePassTemplateRowKey(PassTemplateVO passTemplate) {

        //因为商户ID和优惠券名称都是唯一的，因此passInfo也是唯一不重复的
        String passInfo = String.valueOf(passTemplate.getId()) + "_" + passTemplate.getTitle();
        String rowKey = DigestUtils.md5Hex(passInfo);
        log.info("generatePassTemplateRowKey passInfo: {}, Rowkey: {}", passInfo, rowKey);
        return rowKey;
    }

    /**
     * 根据{@link FeedbackVO} 生成Rowkey
     *
     * @param feedback {@link FeedbackVO}
     * @return rowkey
     */
    public static String generateFeedbackRowKey(FeedbackVO feedback) {

        String userIdReverseStr = new StringBuilder(
                String.valueOf(feedback.getUserId())
        ).reverse().toString();
        String rowKey = userIdReverseStr + (Long.MAX_VALUE - System.currentTimeMillis());
        return rowKey;
    }
}
