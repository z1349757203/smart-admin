package net.lab1024.sa.admin.module.payment.domain.channel;

import lombok.Data;

import java.io.Serializable;

/**
 * 渠道响应 基类
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class ChannelResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 渠道交互是否成功（网络/验签/受理层面）
     */
    private boolean success;

    /**
     * 渠道返回码
     */
    private String channelCode;

    /**
     * 渠道返回信息
     */
    private String channelMsg;

    /**
     * 渠道原始报文
     */
    private String rawResponse;

    public static ChannelResponse fail(String channelCode, String channelMsg) {
        ChannelResponse response = new ChannelResponse();
        response.setSuccess(false);
        response.setChannelCode(channelCode);
        response.setChannelMsg(channelMsg);
        return response;
    }
}
