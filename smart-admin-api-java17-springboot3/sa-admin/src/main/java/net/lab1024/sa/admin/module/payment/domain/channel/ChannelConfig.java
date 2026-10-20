package net.lab1024.sa.admin.module.payment.domain.channel;

import lombok.Data;

import java.io.Serializable;

/**
 * 支付渠道配置（各聚合支付平台的接入参数）
 * <p>
 * 不同平台所需字段不完全一致，这里抽象出通用字段，
 * 平台特有的参数可放入 {@link #extra} 中。
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class ChannelConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 商户号
     */
    private String merchantId;

    /**
     * 商户私钥（用于请求签名）
     */
    private String privateKey;

    /**
     * 平台公钥/证书（用于验签）
     */
    private String platformPublicKey;

    /**
     * 网关地址
     */
    private String gatewayUrl;

    /**
     * 异步回调地址
     */
    private String notifyUrl;

    /**
     * 同步返回地址
     */
    private String returnUrl;

    /**
     * 是否启用
     */
    private Boolean enabledFlag;

    /**
     * 平台特有扩展参数
     */
    private java.util.Map<String, String> extra;
}
