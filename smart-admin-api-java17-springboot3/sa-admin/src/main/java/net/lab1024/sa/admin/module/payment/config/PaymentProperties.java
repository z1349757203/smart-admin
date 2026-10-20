package net.lab1024.sa.admin.module.payment.config;

import lombok.Data;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付渠道配置（读取 application.yaml 中 payment.channel.* 配置）
 * <p>
 * 示例：
 * <pre>
 * payment:
 *   channel:
 *     LIANLIAN_GLOBAL:
 *       app-id: xxx
 *       merchant-id: xxx
 *       private-key: xxx
 *       platform-public-key: xxx
 *       gateway-url: https://xxx
 *       enabled-flag: true
 * </pre>
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Component
@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {

    /**
     * key = {@link net.lab1024.sa.admin.module.payment.constant.PaymentChannelEnum} 的枚举名
     */
    private Map<String, ChannelConfig> channel = new HashMap<>();
}
