package net.lab1024.sa.admin.module.payment.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付交易流水 VO
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class PaymentTransactionVO {

    @Schema(description = "交易流水id")
    private Long transactionId;

    @Schema(description = "支付渠道值")
    private Integer channelCode;

    @Schema(description = "支付渠道名称")
    private String channelDesc;

    @Schema(description = "交易类型值")
    private Integer tradeType;

    @Schema(description = "交易类型名称")
    private String tradeTypeDesc;

    @Schema(description = "商户订单号")
    private String outTradeNo;

    @Schema(description = "渠道交易号")
    private String channelTradeNo;

    @Schema(description = "商户退款单号")
    private String outRefundNo;

    @Schema(description = "渠道退款单号")
    private String channelRefundNo;

    @Schema(description = "订单标题")
    private String subject;

    @Schema(description = "交易总金额")
    private BigDecimal totalAmount;

    @Schema(description = "退款金额")
    private BigDecimal refundAmount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "交易状态值")
    private Integer status;

    @Schema(description = "交易状态名称")
    private String statusDesc;

    @Schema(description = "支付完成时间")
    private LocalDateTime paidTime;

    @Schema(description = "备注")
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
