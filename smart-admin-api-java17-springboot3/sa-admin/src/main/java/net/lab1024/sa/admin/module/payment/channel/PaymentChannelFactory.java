package net.lab1024.sa.admin.module.payment.channel;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.payment.constant.PaymentChannelEnum;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 支付渠道工厂（路由）
 * <p>
 * 启动时收集所有 {@link PaymentChannelService} 实现，按渠道建立索引；
 * 业务层通过 {@link #getChannel} 拿到对应平台实现，新增平台无需改动业务代码。
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Component
public class PaymentChannelFactory {

    @Resource
    private List<PaymentChannelService> channelServiceList;

    private final Map<PaymentChannelEnum, PaymentChannelService> channelServiceMap = new EnumMap<>(PaymentChannelEnum.class);

    @PostConstruct
    public void init() {
        for (PaymentChannelService service : channelServiceList) {
            PaymentChannelEnum channel = service.channel();
            if (channel == null) {
                continue;
            }
            PaymentChannelService exists = channelServiceMap.put(channel, service);
            if (exists != null) {
                log.warn("支付渠道[{}]存在重复实现: {} 覆盖了 {}", channel.getDesc(),
                        service.getClass().getSimpleName(), exists.getClass().getSimpleName());
            }
        }
        log.info("支付渠道工厂初始化完成, 已注册渠道: {}", channelServiceMap.keySet());
    }

    /**
     * 根据渠道枚举获取实现
     */
    public PaymentChannelService getChannel(PaymentChannelEnum channel) {
        PaymentChannelService service = channelServiceMap.get(channel);
        if (service == null) {
            throw new IllegalArgumentException("暂不支持的支付渠道: " + (channel == null ? "null" : channel.getDesc()));
        }
        return service;
    }

    /**
     * 根据渠道 value 获取实现
     */
    public PaymentChannelService getChannel(Integer channelValue) {
        return getChannel(PaymentChannelEnum.getByValue(channelValue));
    }

    /**
     * 是否已注册该渠道
     */
    public boolean isSupported(PaymentChannelEnum channel) {
        return channelServiceMap.containsKey(channel);
    }
}
