package net.lab1024.sa.admin.module.payment.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 支付渠道（聚合支付平台）枚举
 * <p>
 * 新增一个聚合支付平台时，在此登记一个渠道值，并提供对应的
 * {@link net.lab1024.sa.admin.module.payment.channel.PaymentChannelService} 实现即可，
 * 业务编排层无需改动。
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@AllArgsConstructor
@Getter
public enum PaymentChannelEnum implements BaseEnum {

    /**
     * 连连国际
     */
    LIANLIAN_GLOBAL(1, "连连国际"),

    /**
     * PingPong
     */
    PINGPONG(2, "PingPong"),

    /**
     * 汇付天下
     */
    HUIFU(3, "汇付天下"),

    ;

    private final Integer value;

    private final String desc;

    /**
     * 根据 value 获取枚举，未匹配返回 null
     */
    public static PaymentChannelEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PaymentChannelEnum item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return null;
    }
}
