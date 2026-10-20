package net.lab1024.sa.admin.module.payment.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 支付交易状态枚举
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@AllArgsConstructor
@Getter
public enum PaymentStatusEnum implements BaseEnum {

    /**
     * 待支付
     */
    WAIT_PAY(0, "待支付"),

    /**
     * 支付中
     */
    PAYING(1, "支付中"),

    /**
     * 成功
     */
    SUCCESS(2, "成功"),

    /**
     * 失败
     */
    FAIL(3, "失败"),

    /**
     * 已关闭
     */
    CLOSED(4, "已关闭"),

    /**
     * 退款中
     */
    REFUNDING(5, "退款中"),

    /**
     * 已退款
     */
    REFUNDED(6, "已退款"),

    ;

    private final Integer value;

    private final String desc;

    /**
     * 是否为终态（不再变化）
     */
    public static boolean isFinalStatus(Integer value) {
        return SUCCESS.getValue().equals(value)
                || FAIL.getValue().equals(value)
                || CLOSED.getValue().equals(value)
                || REFUNDED.getValue().equals(value);
    }
}
