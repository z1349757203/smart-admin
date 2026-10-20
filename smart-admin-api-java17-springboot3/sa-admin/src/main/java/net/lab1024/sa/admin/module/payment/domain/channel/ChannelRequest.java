package net.lab1024.sa.admin.module.payment.domain.channel;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 渠道请求 基类
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class ChannelRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户系统内部订单号
     */
    private String outTradeNo;

    /**
     * 渠道配置
     */
    private ChannelConfig config;

    /**
     * 扩展参数
     */
    private Map<String, Object> extra;
}
