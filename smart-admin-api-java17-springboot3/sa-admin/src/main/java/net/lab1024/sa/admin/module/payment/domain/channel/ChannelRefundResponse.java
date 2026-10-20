package net.lab1024.sa.admin.module.payment.domain.channel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.admin.module.payment.constant.PaymentStatusEnum;

import java.math.BigDecimal;

/**
 * 渠道退款响应
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelRefundResponse extends ChannelResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 渠道退款单号
     */
    private String channelRefundNo;

    /**
     * 退款状态
     */
    private PaymentStatusEnum status;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
}
