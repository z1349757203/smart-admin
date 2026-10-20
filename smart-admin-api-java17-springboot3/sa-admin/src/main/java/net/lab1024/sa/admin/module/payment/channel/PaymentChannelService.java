package net.lab1024.sa.admin.module.payment.channel;

import net.lab1024.sa.admin.module.payment.constant.PaymentChannelEnum;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelNotifyResult;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelPayRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelPayResponse;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelQueryRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelQueryResponse;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelRefundRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelRefundResponse;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelResponse;

/**
 * 支付渠道交易接口（抽象封装）
 * <p>
 * 系统对接不同聚合支付平台（连连国际、PingPong、汇付天下等）时，
 * 每个平台提供一份实现即可；业务编排层只依赖本接口，不感知具体平台差异。
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
public interface PaymentChannelService {

    /**
     * 当前实现对应的支付渠道
     */
    PaymentChannelEnum channel();

    /**
     * 统一下单（发起支付）
     */
    ChannelPayResponse pay(ChannelPayRequest request);

    /**
     * 交易查询
     */
    ChannelQueryResponse query(ChannelQueryRequest request);

    /**
     * 退款
     */
    ChannelRefundResponse refund(ChannelRefundRequest request);

    /**
     * 关闭交易
     */
    ChannelResponse close(ChannelRequest request);

    /**
     * 解析并验签异步回调
     *
     * @param body 回调原始报文
     */
    ChannelNotifyResult parseNotify(String body);
}
