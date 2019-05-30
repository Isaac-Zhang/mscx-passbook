package com.sxzhongf.mscx.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * PassVO for 用户领取的优惠券
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassVO {

    private Long userId;
    /**
     * pass 在hbase中的rowkey
     */
    private String rowKey;
    /**
     * PassTemplate 在hbase中的RowKey
     */
    private String templateId;
    /**
     * 优惠券token，有可能是努力了，则填充-1
     */
    private String token;
    /**
     * 领取日期
     */
    private Date assignedDate;
    /**
     * 消费日期
     */
    private Date consumeDate;
}
