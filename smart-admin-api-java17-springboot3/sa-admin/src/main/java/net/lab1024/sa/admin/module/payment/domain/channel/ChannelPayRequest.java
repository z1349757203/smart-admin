package net.lab1024.sa.admin.module.payment.domain.channel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 渠道下单（支付）请求
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelPayRequest extends ChannelRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 订单描述
     */
    private String body;

    /**
     * 交易金额
     */
    private BigDecimal totalAmount;

    /**
     * 币种，如 CNY / USD
     */
    private String currency;

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * 异步回调地址（覆盖渠道配置）
     */
    private String notifyUrl;

    /**
     * 同步返回地址（覆盖渠道配置）
     */
    private String returnUrl;
}
