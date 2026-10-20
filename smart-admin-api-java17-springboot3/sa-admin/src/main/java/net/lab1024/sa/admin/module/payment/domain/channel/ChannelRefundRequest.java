package net.lab1024.sa.admin.module.payment.domain.channel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 渠道退款请求
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelRefundRequest extends ChannelRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 商户系统内部退款单号
     */
    private String outRefundNo;

    /**
     * 原渠道交易号
     */
    private String channelTradeNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 原订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 异步回调地址
     */
    private String notifyUrl;
}
