package com.sxzhongf.mscx.passbook.service.impl;

import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import com.sxzhongf.mscx.passbook.constant.Constants;
import com.sxzhongf.mscx.passbook.service.IHBasePassService;
import com.sxzhongf.mscx.passbook.utils.RowKeyGenerateUtils;
import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * HBasePassServiceImpl for 优惠券HBase service
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
@Slf4j
@Service
public class HBasePassServiceImpl implements IHBasePassService {

    private final HbaseTemplate hbaseTemplate;

    @Autowired
    public HBasePassServiceImpl(HbaseTemplate hbaseTemplate) {
        this.hbaseTemplate = hbaseTemplate;
    }

    @Override
    public boolean launchPassTemplateToHBase(PassTemplateVO passTemplateVO) {
        if (null == passTemplateVO) {
            log.error("passTemplateVO 不能为空！");
            return false;
        }
        String rowkey = RowKeyGenerateUtils.generatePassTemplateRowKey(passTemplateVO);
        try {
            //判断HBase table中是否已经存在当前Rowkey
            if (hbaseTemplate.getConnection().getTable(TableName.valueOf(Constants.PassTemplateTable.TABLE_NAME))
                             .exists(new Get(Bytes.toBytes(rowkey)))) {
                log.warn("RowKey {} is already exist!", rowkey);
            }
        } catch (IOException e) {
            log.error("LaunchPassTemplateToHBase Error: {}！", e.getMessage());
            return false;
        }
        //写入HBase
        Put putObject = new Put(Bytes.toBytes(rowkey));
        //填充hbase 列内容
        putObject.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(Constants.PassTemplateTable.ID),
                Bytes.toBytes(passTemplateVO.getId())
        );
        putObject.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(Constants.PassTemplateTable.TITLE),
                Bytes.toBytes(passTemplateVO.getTitle())
        );
        putObject.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(Constants.PassTemplateTable.SUMMARY),
                Bytes.toBytes(passTemplateVO.getSummary())
        );
        putObject.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(Constants.PassTemplateTable.DESC),
                Bytes.toBytes(passTemplateVO.getDesc())
        );
        putObject.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(Constants.PassTemplateTable.HAS_TOKEN),
                Bytes.toBytes(passTemplateVO.getHasToken())
        );
        putObject.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B),
                Bytes.toBytes(Constants.PassTemplateTable.BACKGROUND),
                Bytes.toBytes(passTemplateVO.getBackground())
        );

        putObject.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                Bytes.toBytes(passTemplateVO.getLimit())
        );
        putObject.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(Constants.PassTemplateTable.START),
                Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(passTemplateVO.getStart()))
        );
        putObject.addColumn(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(Constants.PassTemplateTable.END),
                Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(passTemplateVO.getEnd()))
        );

        //如果有Rowkey,update, 反之insert
        hbaseTemplate.saveOrUpdate(Constants.PassTemplateTable.TABLE_NAME, putObject);

        return true;
    }
}
