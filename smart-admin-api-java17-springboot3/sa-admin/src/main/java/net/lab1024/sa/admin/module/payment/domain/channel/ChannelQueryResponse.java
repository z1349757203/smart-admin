package net.lab1024.sa.admin.module.payment.domain.channel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.admin.module.payment.constant.PaymentStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 渠道交易查询响应
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelQueryResponse extends ChannelResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 交易状态（已归一化为系统内部状态）
     */
    private PaymentStatusEnum status;

    /**
     * 渠道交易号
     */
    private String channelTradeNo;

    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 支付完成时间
     */
    private LocalDateTime paidTime;
}
