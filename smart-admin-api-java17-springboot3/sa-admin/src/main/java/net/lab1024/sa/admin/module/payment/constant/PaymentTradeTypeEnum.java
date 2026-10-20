package net.lab1024.sa.admin.module.payment.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 支付交易类型枚举
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@AllArgsConstructor
@Getter
public enum PaymentTradeTypeEnum implements BaseEnum {

    /**
     * 支付
     */
    PAY(1, "支付"),

    /**
     * 退款
     */
    REFUND(2, "退款"),

    ;

    private final Integer value;

    private final String desc;
}
