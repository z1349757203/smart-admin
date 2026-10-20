package net.lab1024.sa.admin.module.payment.domain.channel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 渠道交易查询请求
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelQueryRequest extends ChannelRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 渠道交易号（与 outTradeNo 二选一，优先使用渠道交易号）
     */
    private String channelTradeNo;
}
