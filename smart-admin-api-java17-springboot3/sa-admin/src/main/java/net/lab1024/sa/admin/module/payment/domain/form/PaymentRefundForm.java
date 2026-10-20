package net.lab1024.sa.admin.module.payment.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
 * 退款表单
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class PaymentRefundForm {

    @Schema(description = "原商户订单号", required = true)
    @NotBlank(message = "原商户订单号不能为空")
    private String outTradeNo;

    @Schema(description = "商户退款单号|可选，不传则系统生成")
    @Length(max = 64, message = "商户退款单号最多64字符")
    private String outRefundNo;

    @Schema(description = "退款金额", required = true)
    @NotNull(message = "退款金额不能为空")
    @DecimalMin(value = "0.01", message = "退款金额必须大于0")
    private BigDecimal refundAmount;

    @Schema(description = "退款原因|可选")
    @Length(max = 256, message = "退款原因最多256字符")
    private String reason;
}
