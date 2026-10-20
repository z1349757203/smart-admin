package net.lab1024.sa.admin.module.payment.domain.channel;

import lombok.Data;
import net.lab1024.sa.admin.module.payment.constant.PaymentStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 渠道异步回调 解析结果（已归一化 + 验签结果）
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class ChannelNotifyResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 验签是否通过
     */
    private boolean signVerified;

    /**
     * 商户系统内部订单号
     */
    private String outTradeNo;

    /**
     * 渠道交易号
     */
    private String channelTradeNo;

    /**
     * 交易状态
     */
    private PaymentStatusEnum status;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 支付完成时间
     */
    private LocalDateTime paidTime;

    /**
     * 回执给渠道的应答内容（各平台要求不同）
     */
    private String responseContent;

    /**
     * 原始报文
     */
    private String rawBody;
}
