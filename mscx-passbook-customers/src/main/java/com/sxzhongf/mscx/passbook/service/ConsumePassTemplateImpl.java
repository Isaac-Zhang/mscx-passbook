package com.sxzhongf.mscx.passbook.service;

import com.alibaba.fastjson.JSON;
import com.sxzhongf.mscx.passbook.constant.Constants;
import com.sxzhongf.mscx.passbook.vo.PassTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * ConsumePassTemplateImpl for 消费Kafka中的 PassTemplate
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
@Slf4j
@Component
public class ConsumePassTemplateImpl {

    /**
     * 优惠券相关的 HBase 服务注入
     */
    private final IHBasePassService passService;

    public ConsumePassTemplateImpl(IHBasePassService passService) {
        this.passService = passService;
    }

    /**
     * 获取kafka中的消息
     *
     * @param passTemplate {@link Payload}从kafka中接收到的数据是什么
     * @param key          {@link Header}{@link KafkaHeaders} RECEIVED_MESSAGE_KEY 用于写入分区
     * @param partition    {@link Header}{@link KafkaHeaders} RECEIVED_PARTITION_ID
     * @param topic        {@link Header}{@link KafkaHeaders} RECEIVED_TOPIC
     */
    @KafkaListener(topics = {Constants.TEMPLATE_TOPIC})
    public void receive(@Payload String passTemplate,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumer Receive passtemplate: {}", passTemplate);
        PassTemplateVO passTemplateVO = null;
        try {
            passTemplateVO = JSON.parseObject(passTemplate, PassTemplateVO.class);
        } catch (Exception ex) {
            log.error("Consumer Receive passtemplate: {}", ex.getMessage());
        }
        log.info("launchPassTemplateToHBase: {} ", passService.launchPassTemplateToHBase(passTemplateVO));
    }
}
