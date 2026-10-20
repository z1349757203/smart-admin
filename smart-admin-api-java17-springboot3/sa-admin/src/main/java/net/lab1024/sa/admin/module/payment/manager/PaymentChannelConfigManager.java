package net.lab1024.sa.admin.module.payment.manager;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.payment.config.PaymentProperties;
import net.lab1024.sa.admin.module.payment.constant.PaymentChannelEnum;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelConfig;
import org.springframework.stereotype.Component;

/**
 * 支付渠道配置管理器
 * <p>
 * 统一对外提供某渠道的接入配置；后续如需支持「多商户/数据库动态配置」，
 * 只需替换本类实现，业务与渠道实现均无需改动。
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Component
public class PaymentChannelConfigManager {

    @Resource
    private PaymentProperties paymentProperties;

    /**
     * 获取渠道配置，不存在时返回一个空配置对象（避免 NPE）
     */
    public ChannelConfig getConfig(PaymentChannelEnum channel) {
        if (channel == null) {
            return new ChannelConfig();
        }
        ChannelConfig config = paymentProperties.getChannel().get(channel.name());
        return config == null ? new ChannelConfig() : config;
    }

    /**
     * 渠道是否已启用
     */
    public boolean isEnabled(PaymentChannelEnum channel) {
        ChannelConfig config = getConfig(channel);
        return Boolean.TRUE.equals(config.getEnabledFlag());
    }
}
