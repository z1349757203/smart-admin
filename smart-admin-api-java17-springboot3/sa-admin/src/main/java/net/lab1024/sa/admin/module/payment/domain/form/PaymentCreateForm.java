package net.lab1024.sa.admin.module.payment.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.admin.module.payment.constant.PaymentChannelEnum;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
 * 发起支付（统一下单）表单
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class PaymentCreateForm {

    @SchemaEnum(desc = "支付渠道", value = PaymentChannelEnum.class)
    @CheckEnum(value = PaymentChannelEnum.class, required = true, message = "支付渠道错误")
    private Integer channelCode;

    @Schema(description = "商户订单号|可选，不传则系统生成")
    @Length(max = 64, message = "商户订单号最多64字符")
    private String outTradeNo;

    @Schema(description = "订单标题", required = true)
    @NotBlank(message = "订单标题不能为空")
    @Length(max = 128, message = "订单标题最多128字符")
    private String subject;

    @Schema(description = "订单描述|可选")
    @Length(max = 256, message = "订单描述最多256字符")
    private String body;

    @Schema(description = "交易金额", required = true)
    @NotNull(message = "交易金额不能为空")
    @DecimalMin(value = "0.01", message = "交易金额必须大于0")
    private BigDecimal totalAmount;

    @Schema(description = "币种，如 CNY / USD", required = true)
    @NotBlank(message = "币种不能为空")
    @Length(max = 8, message = "币种最多8字符")
    private String currency;

    @Schema(description = "客户端ip|可选")
    @Length(max = 64, message = "客户端ip最多64字符")
    private String clientIp;

    @Schema(description = "异步回调地址|可选")
    @Length(max = 256, message = "回调地址最多256字符")
    private String notifyUrl;

    @Schema(description = "同步返回地址|可选")
    @Length(max = 256, message = "返回地址最多256字符")
    private String returnUrl;
}
