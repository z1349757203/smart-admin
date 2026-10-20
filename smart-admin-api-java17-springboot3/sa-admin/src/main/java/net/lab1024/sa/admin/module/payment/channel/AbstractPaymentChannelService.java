package net.lab1024.sa.admin.module.payment.channel;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelPayRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelPayResponse;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelQueryRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelQueryResponse;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelRefundRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelRefundResponse;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelResponse;

/**
 * 支付渠道 抽象模板
 * <p>
 * 统一处理日志、异常兜底、参数校验等公共逻辑，各平台只需实现 doXxx 钩子方法，
 * 聚焦于「组装平台报文 -> 调用平台接口 -> 解析并归一化结果」。
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
public abstract class AbstractPaymentChannelService implements PaymentChannelService {

    @Override
    public ChannelPayResponse pay(ChannelPayRequest request) {
        checkRequest(request);
        log.info("[{}] 统一下单, outTradeNo={}", channel().getDesc(), request.getOutTradeNo());
        try {
            return doPay(request);
        } catch (Exception e) {
            log.error("[{}] 统一下单异常, outTradeNo={}", channel().getDesc(), request.getOutTradeNo(), e);
            return failPay(e);
        }
    }

    @Override
    public ChannelQueryResponse query(ChannelQueryRequest request) {
        checkRequest(request);
        log.info("[{}] 交易查询, outTradeNo={}", channel().getDesc(), request.getOutTradeNo());
        try {
            return doQuery(request);
        } catch (Exception e) {
            log.error("[{}] 交易查询异常, outTradeNo={}", channel().getDesc(), request.getOutTradeNo(), e);
            ChannelQueryResponse response = new ChannelQueryResponse();
            response.setSuccess(false);
            response.setChannelMsg(e.getMessage());
            return response;
        }
    }

    @Override
    public ChannelRefundResponse refund(ChannelRefundRequest request) {
        checkRequest(request);
        log.info("[{}] 退款, outTradeNo={}, outRefundNo={}", channel().getDesc(), request.getOutTradeNo(), request.getOutRefundNo());
        try {
            return doRefund(request);
        } catch (Exception e) {
            log.error("[{}] 退款异常, outRefundNo={}", channel().getDesc(), request.getOutRefundNo(), e);
            ChannelRefundResponse response = new ChannelRefundResponse();
            response.setSuccess(false);
            response.setChannelMsg(e.getMessage());
            return response;
        }
    }

    @Override
    public ChannelResponse close(ChannelRequest request) {
        checkRequest(request);
        log.info("[{}] 关闭交易, outTradeNo={}", channel().getDesc(), request.getOutTradeNo());
        try {
            return doClose(request);
        } catch (Exception e) {
            log.error("[{}] 关闭交易异常, outTradeNo={}", channel().getDesc(), request.getOutTradeNo(), e);
            return ChannelResponse.fail("SYSTEM_ERROR", e.getMessage());
        }
    }

    protected void checkRequest(ChannelRequest request) {
        if (request == null || request.getOutTradeNo() == null || request.getOutTradeNo().isEmpty()) {
            throw new IllegalArgumentException("商户订单号(outTradeNo)不能为空");
        }
    }

    private ChannelPayResponse failPay(Exception e) {
        ChannelPayResponse response = new ChannelPayResponse();
        response.setSuccess(false);
        response.setChannelCode("SYSTEM_ERROR");
        response.setChannelMsg(e.getMessage());
        return response;
    }

    /**
     * 组装平台报文并调用平台下单接口
     */
    protected abstract ChannelPayResponse doPay(ChannelPayRequest request);

    /**
     * 调用平台交易查询接口，并将平台状态归一化为系统状态
     */
    protected abstract ChannelQueryResponse doQuery(ChannelQueryRequest request);

    /**
     * 调用平台退款接口
     */
    protected abstract ChannelRefundResponse doRefund(ChannelRefundRequest request);

    /**
     * 调用平台关闭交易接口
     */
    protected abstract ChannelResponse doClose(ChannelRequest request);
}
