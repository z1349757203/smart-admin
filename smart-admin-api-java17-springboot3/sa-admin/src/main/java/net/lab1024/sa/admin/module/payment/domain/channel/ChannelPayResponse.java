package net.lab1024.sa.admin.module.payment.domain.channel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 渠道下单（支付）响应
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelPayResponse extends ChannelResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 渠道交易号
     */
    private String channelTradeNo;

    /**
     * 收银台跳转地址（网关支付/收银台模式）
     */
    private String payUrl;

    /**
     * 前端调起支付所需参数（如 app/h5/jsapi 支付参数）
     */
    private Map<String, String> payParams;
}
