package net.lab1024.sa.admin.module.payment.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * 支付交易流水 查询表单
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentQueryForm extends PageParam {

    @Schema(description = "支付渠道|可选")
    private Integer channelCode;

    @Schema(description = "交易类型|可选")
    private Integer tradeType;

    @Schema(description = "交易状态|可选")
    private Integer status;

    @Schema(description = "商户订单号|可选")
    private String outTradeNo;
}
