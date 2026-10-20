package net.lab1024.sa.admin.module.payment.channel.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.payment.channel.AbstractPaymentChannelService;
import net.lab1024.sa.admin.module.payment.constant.PaymentChannelEnum;
import net.lab1024.sa.admin.module.payment.constant.PaymentStatusEnum;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelConfig;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelNotifyResult;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelPayRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelPayResponse;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelQueryRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelQueryResponse;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelRefundRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelRefundResponse;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelResponse;
import net.lab1024.sa.admin.module.payment.manager.PaymentChannelConfigManager;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 汇付天下（Huifu）支付渠道实现
 * <p>
 * 已完成「参数组装 + 归一化结果」骨架，标注 TODO 处接入汇付天下斗拱/官方 SDK 接口即可。
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class HuifuChannelService extends AbstractPaymentChannelService {

    @Resource
    private PaymentChannelConfigManager configManager;

    @Override
    public PaymentChannelEnum channel() {
        return PaymentChannelEnum.HUIFU;
    }

    @Override
    protected ChannelPayResponse doPay(ChannelPayRequest request) {
        ChannelConfig config = resolveConfig(request.getConfig());
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("sys_id", config.getAppId());
        params.put("huifu_id", config.getMerchantId());
        params.put("req_seq_id", request.getOutTradeNo());
        params.put("trans_amt", request.getTotalAmount());
        params.put("currency", request.getCurrency());
        params.put("goods_desc", request.getSubject());
        params.put("notify_url", request.getNotifyUrl() != null ? request.getNotifyUrl() : config.getNotifyUrl());
        // TODO 调用汇付天下 SDK：签名 -> 请求下单 -> 解析返回
        log.info("[汇付天下] 待接入下单接口, params={}", params);

        ChannelPayResponse response = new ChannelPayResponse();
        response.setSuccess(false);
        response.setChannelCode("NOT_IMPLEMENTED");
        response.setChannelMsg("汇付天下下单接口待接入官方SDK/HTTP");
        return response;
    }

    @Override
    protected ChannelQueryResponse doQuery(ChannelQueryRequest request) {
        // TODO 调用汇付天下交易查询接口，并将平台状态映射为 PaymentStatusEnum
        ChannelQueryResponse response = new ChannelQueryResponse();
        response.setSuccess(false);
        response.setStatus(PaymentStatusEnum.PAYING);
        response.setChannelCode("NOT_IMPLEMENTED");
        response.setChannelMsg("汇付天下查询接口待接入官方SDK/HTTP");
        return response;
    }

    @Override
    protected ChannelRefundResponse doRefund(ChannelRefundRequest request) {
        // TODO 调用汇付天下退款接口
        ChannelRefundResponse response = new ChannelRefundResponse();
        response.setSuccess(false);
        response.setStatus(PaymentStatusEnum.REFUNDING);
        response.setChannelCode("NOT_IMPLEMENTED");
        response.setChannelMsg("汇付天下退款接口待接入官方SDK/HTTP");
        return response;
    }

    @Override
    protected ChannelResponse doClose(ChannelRequest request) {
        // TODO 调用汇付天下关单接口
        return ChannelResponse.fail("NOT_IMPLEMENTED", "汇付天下关单接口待接入官方SDK/HTTP");
    }

    @Override
    public ChannelNotifyResult parseNotify(String body) {
        // TODO 汇付天下回调验签 + 报文解析
        ChannelNotifyResult result = new ChannelNotifyResult();
        result.setSignVerified(false);
        result.setRawBody(body);
        result.setResponseContent("fail");
        return result;
    }

    private ChannelConfig resolveConfig(ChannelConfig requestConfig) {
        return requestConfig != null ? requestConfig : configManager.getConfig(channel());
    }
}
