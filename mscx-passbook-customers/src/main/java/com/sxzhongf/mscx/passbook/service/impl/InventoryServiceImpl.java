package com.sxzhongf.mscx.passbook.service.impl;

import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import com.sxzhongf.mscx.passbook.constant.Constants;
import com.sxzhongf.mscx.passbook.dao.MerchantsDao;
import com.sxzhongf.mscx.passbook.entity.Merchants;
import com.sxzhongf.mscx.passbook.mapper.PassTemplateRowMapper;
import com.sxzhongf.mscx.passbook.service.IInventoryService;
import com.sxzhongf.mscx.passbook.service.IUserPassService;
import com.sxzhongf.mscx.passbook.utils.RowKeyGenerateUtils;
import com.sxzhongf.mscx.passbook.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.LongComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * InventoryServiceImpl for 库存服务
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/6/3
 */
@Slf4j
@Service
public class InventoryServiceImpl implements IInventoryService {

    private final HbaseTemplate hbaseTemplate;
    private final MerchantsDao merchantsDao;
    private final IUserPassService userPassService;

    @Autowired
    public InventoryServiceImpl(HbaseTemplate hbaseTemplate, MerchantsDao merchantsDao, IUserPassService userPassService) {
        this.hbaseTemplate = hbaseTemplate;
        this.merchantsDao = merchantsDao;
        this.userPassService = userPassService;
    }

    /**
     * 获取库存信息，只返回用户未领取的
     *
     * @param userId 使用userId可以起到过滤信息作用
     * @return {@link ResponseVO}
     * @throws Exception error
     */
    @Override
    public ResponseVO getInventoryInfo(Long userId) throws Exception {

        ResponseVO responseVO = userPassService.getUserAllPassInfo(userId);
        List<PassInfoVO> allPassInfo = (List<PassInfoVO>) responseVO.getData();

        List<PassTemplateVO> needToBeExcludedObjectList = allPassInfo.stream().map(
                PassInfoVO::getPassTemplateVO
        ).collect(Collectors.toList());

        List<String> needToBeExcludedIds = new ArrayList<>();
        needToBeExcludedObjectList.forEach(
                e -> needToBeExcludedIds.add(
                        RowKeyGenerateUtils.generatePassTemplateRowKey(e)
                )
        );

        return new ResponseVO(new InventoryResponseVO(userId,
                buildPassTemplateInfo(
                        getAvailablePassTemplate(needToBeExcludedIds))
        ));
    }

    /**
     * 获取可用的优惠券，后续扩展如果优惠券过多，那么就需要考虑进行分页/缓存处理
     *
     * @param excludeIds 需要排除的优惠券ids
     * @return {@link List<PassTemplateVO>}
     */
    private List<PassTemplateVO> getAvailablePassTemplate(List<String> excludeIds) {

        //该条件集合被设置为or的关系，标示多条件只需满足其中任意一个条件即可
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);

        // 过滤条件1，最少可使用数 > 0
        filterList.addFilter(
                new SingleColumnValueFilter(
                        Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                        Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                        CompareFilter.CompareOp.GREATER,
                        new LongComparator(0L)
                )
        );
        // 过滤条件2，最少可使用数 == -1
        filterList.addFilter(
                new SingleColumnValueFilter(
                        Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                        Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                        CompareFilter.CompareOp.EQUAL,
                        Bytes.toBytes(-1)
                )
        );

        Scan scan = new Scan();
        scan.setFilter(filterList);

        // 使用Hbase Client 查询列表数据
        List<PassTemplateVO> validList = hbaseTemplate.find(
                Constants.PassTemplateTable.TABLE_NAME,
                scan, new PassTemplateRowMapper()
        );

        List<PassTemplateVO> availableList = new ArrayList<>();
        //定义当前时间，检查优惠券是否过期
        Date current = new Date();
        for (PassTemplateVO item : validList) {
            // 如果当前元素的rowkey包含在需要排除的列表中，则删掉
            if (excludeIds.contains(RowKeyGenerateUtils.generatePassTemplateRowKey(item))) {
                continue;
            }
            //校验当前用户群是否在可用日期范围内
            if (current.getTime() >= item.getStart().getTime() && current.getTime() <= item.getEnd().getTime()) {
                availableList.add(item);
            }
        }
        return availableList;
    }

    /**
     * 构造优惠券的展示信息
     *
     * @param list {@link List<PassTemplateVO>}
     * @return {@link List<PassTemplateInfoVO>}
     */
    private List<PassTemplateInfoVO> buildPassTemplateInfo(List<PassTemplateVO> list) {
        Map<Integer, Merchants> merchantsMap = new HashMap<>();
        //获取商户ids
        List<Integer> merchantIds = list.stream().map(
                PassTemplateVO::getId
        ).collect(Collectors.toList());

        List<Merchants> merchantsList = merchantsDao.findAllById(merchantIds);
        merchantsList.forEach(m -> merchantsMap.put(m.getId(), m));

        List<PassTemplateInfoVO> result = new ArrayList<>(list.size());
        for (PassTemplateVO p : list) {
            Merchants mc = merchantsMap.getOrDefault(p.getId(), null);
            if (null == mc) {
                log.error("Merchants Error : {}", p.getId());
                continue;
            }
            result.add(new PassTemplateInfoVO(p, mc));
        }

        return result;
    }
}
