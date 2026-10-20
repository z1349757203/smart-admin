package net.lab1024.sa.admin.module.payment.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 发起支付 结果 VO
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class PaymentCreateVO {

    @Schema(description = "交易流水id")
    private Long transactionId;

    @Schema(description = "商户订单号")
    private String outTradeNo;

    @Schema(description = "渠道交易号")
    private String channelTradeNo;

    @Schema(description = "交易状态值")
    private Integer status;

    @Schema(description = "收银台跳转地址")
    private String payUrl;

    @Schema(description = "前端调起支付所需参数")
    private Map<String, String> payParams;
}
