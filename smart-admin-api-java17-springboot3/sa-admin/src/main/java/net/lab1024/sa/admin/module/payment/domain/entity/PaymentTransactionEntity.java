package net.lab1024.sa.admin.module.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.admin.module.payment.constant.PaymentChannelEnum;
import net.lab1024.sa.admin.module.payment.constant.PaymentStatusEnum;
import net.lab1024.sa.admin.module.payment.constant.PaymentTradeTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付交易流水 实体类
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("t_payment_transaction")
public class PaymentTransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long transactionId;

    /**
     * 支付渠道
     *
     * @see PaymentChannelEnum
     */
    private Integer channelCode;

    /**
     * 交易类型
     *
     * @see PaymentTradeTypeEnum
     */
    private Integer tradeType;

    /**
     * 商户系统内部订单号
     */
    private String outTradeNo;

    /**
     * 渠道交易号
     */
    private String channelTradeNo;

    /**
     * 商户系统内部退款单号
     */
    private String outRefundNo;

    /**
     * 渠道退款单号
     */
    private String channelRefundNo;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 交易总金额
     */
    private BigDecimal totalAmount;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 交易状态
     *
     * @see PaymentStatusEnum
     */
    private Integer status;

    /**
     * 支付完成时间
     */
    private LocalDateTime paidTime;

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * 异步回调地址
     */
    private String notifyUrl;

    /**
     * 同步返回地址
     */
    private String returnUrl;

    /**
     * 渠道原始响应报文
     */
    private String channelResponse;

    /**
     * 扩展数据(json)
     */
    private String extraData;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标识
     */
    private Boolean deletedFlag;

    /**
     * 创建人id
     */
    private Long createUserId;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
