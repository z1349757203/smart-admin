package net.lab1024.sa.admin.module.payment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.payment.channel.PaymentChannelFactory;
import net.lab1024.sa.admin.module.payment.channel.PaymentChannelService;
import net.lab1024.sa.admin.module.payment.constant.PaymentChannelEnum;
import net.lab1024.sa.admin.module.payment.constant.PaymentStatusEnum;
import net.lab1024.sa.admin.module.payment.constant.PaymentTradeTypeEnum;
import net.lab1024.sa.admin.module.payment.dao.PaymentTransactionDao;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelConfig;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelNotifyResult;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelPayRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelPayResponse;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelQueryRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelQueryResponse;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelRefundRequest;
import net.lab1024.sa.admin.module.payment.domain.channel.ChannelRefundResponse;
import net.lab1024.sa.admin.module.payment.domain.entity.PaymentTransactionEntity;
import net.lab1024.sa.admin.module.payment.domain.form.PaymentCreateForm;
import net.lab1024.sa.admin.module.payment.domain.form.PaymentQueryForm;
import net.lab1024.sa.admin.module.payment.domain.form.PaymentRefundForm;
import net.lab1024.sa.admin.module.payment.domain.vo.PaymentCreateVO;
import net.lab1024.sa.admin.module.payment.domain.vo.PaymentTransactionVO;
import net.lab1024.sa.admin.module.payment.manager.PaymentChannelConfigManager;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartEnumUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 支付交易 业务编排
 * <p>
 * 负责交易流水的落库与状态流转，通过 {@link PaymentChannelFactory} 路由到具体渠道实现，
 * 不感知任何平台差异。新增聚合支付平台时，本类无需改动。
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class PaymentTransactionService {

    private static final DateTimeFormatter TRADE_NO_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Resource
    private PaymentTransactionDao paymentTransactionDao;

    @Resource
    private PaymentChannelFactory paymentChannelFactory;

    @Resource
    private PaymentChannelConfigManager paymentChannelConfigManager;

    /**
     * 发起支付（统一下单）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<PaymentCreateVO> create(PaymentCreateForm form) {
        PaymentChannelEnum channel = PaymentChannelEnum.getByValue(form.getChannelCode());
        if (channel == null || !paymentChannelFactory.isSupported(channel)) {
            return ResponseDTO.userErrorParam("暂不支持的支付渠道");
        }

        // 1. 落库交易流水（待支付）
        PaymentTransactionEntity entity = new PaymentTransactionEntity();
        entity.setChannelCode(channel.getValue());
        entity.setTradeType(PaymentTradeTypeEnum.PAY.getValue());
        entity.setOutTradeNo(StringUtils.isNotBlank(form.getOutTradeNo()) ? form.getOutTradeNo() : generateNo("PAY"));
        entity.setSubject(form.getSubject());
        entity.setTotalAmount(form.getTotalAmount());
        entity.setCurrency(form.getCurrency());
        entity.setClientIp(form.getClientIp());
        entity.setNotifyUrl(form.getNotifyUrl());
        entity.setReturnUrl(form.getReturnUrl());
        entity.setRemark(form.getBody());
        entity.setStatus(PaymentStatusEnum.WAIT_PAY.getValue());
        entity.setDeletedFlag(false);
        entity.setCreateUserId(SmartRequestUtil.getRequestUserId());
        paymentTransactionDao.insert(entity);

        // 2. 调用渠道下单
        ChannelPayRequest payRequest = new ChannelPayRequest();
        payRequest.setOutTradeNo(entity.getOutTradeNo());
        payRequest.setSubject(form.getSubject());
        payRequest.setBody(form.getBody());
        payRequest.setTotalAmount(form.getTotalAmount());
        payRequest.setCurrency(form.getCurrency());
        payRequest.setClientIp(form.getClientIp());
        payRequest.setNotifyUrl(form.getNotifyUrl());
        payRequest.setReturnUrl(form.getReturnUrl());
        payRequest.setConfig(paymentChannelConfigManager.getConfig(channel));

        ChannelPayResponse payResponse = paymentChannelFactory.getChannel(channel).pay(payRequest);

        // 3. 回写渠道结果
        entity.setChannelTradeNo(payResponse.getChannelTradeNo());
        entity.setChannelResponse(payResponse.getRawResponse());
        entity.setStatus(payResponse.isSuccess() ? PaymentStatusEnum.PAYING.getValue() : PaymentStatusEnum.FAIL.getValue());
        paymentTransactionDao.updateById(entity);

        PaymentCreateVO vo = new PaymentCreateVO();
        vo.setTransactionId(entity.getTransactionId());
        vo.setOutTradeNo(entity.getOutTradeNo());
        vo.setChannelTradeNo(payResponse.getChannelTradeNo());
        vo.setStatus(entity.getStatus());
        vo.setPayUrl(payResponse.getPayUrl());
        vo.setPayParams(payResponse.getPayParams());
        if (!payResponse.isSuccess()) {
            log.warn("渠道[{}]下单未成功, outTradeNo={}, msg={}", channel.getDesc(), entity.getOutTradeNo(), payResponse.getChannelMsg());
        }
        return ResponseDTO.ok(vo);
    }

    /**
     * 分页查询交易流水
     */
    public ResponseDTO<PageResult<PaymentTransactionVO>> query(PaymentQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<PaymentTransactionVO> list = paymentTransactionDao.query(page, queryForm);
        list.forEach(this::fillDesc);
        PageResult<PaymentTransactionVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 交易详情
     */
    public ResponseDTO<PaymentTransactionVO> detail(Long transactionId) {
        PaymentTransactionEntity entity = paymentTransactionDao.selectById(transactionId);
        if (entity == null || Boolean.TRUE.equals(entity.getDeletedFlag())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        PaymentTransactionVO vo = SmartBeanUtil.copy(entity, PaymentTransactionVO.class);
        fillDesc(vo);
        return ResponseDTO.ok(vo);
    }

    /**
     * 主动向渠道查询并同步交易状态
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<PaymentTransactionVO> syncStatus(String outTradeNo) {
        PaymentTransactionEntity entity = paymentTransactionDao.selectByOutTradeNo(outTradeNo, PaymentTradeTypeEnum.PAY.getValue());
        if (entity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        PaymentChannelService channelService = paymentChannelFactory.getChannel(entity.getChannelCode());

        ChannelQueryRequest queryRequest = new ChannelQueryRequest();
        queryRequest.setOutTradeNo(outTradeNo);
        queryRequest.setChannelTradeNo(entity.getChannelTradeNo());
        queryRequest.setConfig(paymentChannelConfigManager.getConfig(PaymentChannelEnum.getByValue(entity.getChannelCode())));
        ChannelQueryResponse queryResponse = channelService.query(queryRequest);

        if (queryResponse.isSuccess() && queryResponse.getStatus() != null) {
            entity.setStatus(queryResponse.getStatus().getValue());
            entity.setChannelTradeNo(StringUtils.defaultIfBlank(queryResponse.getChannelTradeNo(), entity.getChannelTradeNo()));
            entity.setPaidTime(queryResponse.getPaidTime());
            entity.setChannelResponse(queryResponse.getRawResponse());
            paymentTransactionDao.updateById(entity);
        }

        PaymentTransactionVO vo = SmartBeanUtil.copy(entity, PaymentTransactionVO.class);
        fillDesc(vo);
        return ResponseDTO.ok(vo);
    }

    /**
     * 退款
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<PaymentTransactionVO> refund(PaymentRefundForm form) {
        PaymentTransactionEntity origin = paymentTransactionDao.selectByOutTradeNo(form.getOutTradeNo(), PaymentTradeTypeEnum.PAY.getValue());
        if (origin == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "原支付交易不存在");
        }
        if (!PaymentStatusEnum.SUCCESS.getValue().equals(origin.getStatus())) {
            return ResponseDTO.userErrorParam("原交易未支付成功，不能退款");
        }
        if (form.getRefundAmount().compareTo(origin.getTotalAmount()) > 0) {
            return ResponseDTO.userErrorParam("退款金额不能大于原交易金额");
        }

        PaymentChannelEnum channel = PaymentChannelEnum.getByValue(origin.getChannelCode());
        ChannelConfig config = paymentChannelConfigManager.getConfig(channel);

        // 落库退款流水
        PaymentTransactionEntity refundEntity = new PaymentTransactionEntity();
        refundEntity.setChannelCode(origin.getChannelCode());
        refundEntity.setTradeType(PaymentTradeTypeEnum.REFUND.getValue());
        refundEntity.setOutTradeNo(origin.getOutTradeNo());
        refundEntity.setChannelTradeNo(origin.getChannelTradeNo());
        refundEntity.setOutRefundNo(StringUtils.isNotBlank(form.getOutRefundNo()) ? form.getOutRefundNo() : generateNo("REFUND"));
        refundEntity.setSubject(origin.getSubject());
        refundEntity.setTotalAmount(origin.getTotalAmount());
        refundEntity.setRefundAmount(form.getRefundAmount());
        refundEntity.setCurrency(origin.getCurrency());
        refundEntity.setRemark(form.getReason());
        refundEntity.setStatus(PaymentStatusEnum.REFUNDING.getValue());
        refundEntity.setDeletedFlag(false);
        refundEntity.setCreateUserId(SmartRequestUtil.getRequestUserId());
        paymentTransactionDao.insert(refundEntity);

        ChannelRefundRequest refundRequest = new ChannelRefundRequest();
        refundRequest.setOutTradeNo(origin.getOutTradeNo());
        refundRequest.setChannelTradeNo(origin.getChannelTradeNo());
        refundRequest.setOutRefundNo(refundEntity.getOutRefundNo());
        refundRequest.setRefundAmount(form.getRefundAmount());
        refundRequest.setTotalAmount(origin.getTotalAmount());
        refundRequest.setReason(form.getReason());
        refundRequest.setConfig(config);

        ChannelRefundResponse refundResponse = paymentChannelFactory.getChannel(channel).refund(refundRequest);
        refundEntity.setChannelRefundNo(refundResponse.getChannelRefundNo());
        refundEntity.setChannelResponse(refundResponse.getRawResponse());
        if (refundResponse.isSuccess() && refundResponse.getStatus() != null) {
            refundEntity.setStatus(refundResponse.getStatus().getValue());
        } else if (!refundResponse.isSuccess()) {
            refundEntity.setStatus(PaymentStatusEnum.FAIL.getValue());
        }
        paymentTransactionDao.updateById(refundEntity);

        PaymentTransactionVO vo = SmartBeanUtil.copy(refundEntity, PaymentTransactionVO.class);
        fillDesc(vo);
        return ResponseDTO.ok(vo);
    }

    /**
     * 处理渠道异步回调
     *
     * @return 需要回执给渠道的应答内容
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> handleNotify(Integer channelCode, String body) {
        PaymentChannelEnum channel = PaymentChannelEnum.getByValue(channelCode);
        if (channel == null || !paymentChannelFactory.isSupported(channel)) {
            return ResponseDTO.userErrorParam("暂不支持的支付渠道");
        }
        ChannelNotifyResult notifyResult = paymentChannelFactory.getChannel(channel).parseNotify(body);
        if (notifyResult == null || !notifyResult.isSignVerified()) {
            log.warn("渠道[{}]回调验签失败", channel.getDesc());
            return ResponseDTO.userErrorParam("回调验签失败");
        }

        PaymentTransactionEntity entity = paymentTransactionDao.selectByOutTradeNo(notifyResult.getOutTradeNo(), PaymentTradeTypeEnum.PAY.getValue());
        if (entity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "交易不存在");
        }
        // 幂等：已是终态则不再更新
        if (!PaymentStatusEnum.isFinalStatus(entity.getStatus()) && notifyResult.getStatus() != null) {
            entity.setStatus(notifyResult.getStatus().getValue());
            entity.setChannelTradeNo(StringUtils.defaultIfBlank(notifyResult.getChannelTradeNo(), entity.getChannelTradeNo()));
            entity.setPaidTime(notifyResult.getPaidTime() != null ? notifyResult.getPaidTime() : LocalDateTime.now());
            entity.setChannelResponse(body);
            paymentTransactionDao.updateById(entity);
        }
        return ResponseDTO.ok(notifyResult.getResponseContent());
    }

    private void fillDesc(PaymentTransactionVO vo) {
        vo.setChannelDesc(SmartEnumUtil.getEnumDescByValue(vo.getChannelCode(), PaymentChannelEnum.class));
        vo.setTradeTypeDesc(SmartEnumUtil.getEnumDescByValue(vo.getTradeType(), PaymentTradeTypeEnum.class));
        vo.setStatusDesc(SmartEnumUtil.getEnumDescByValue(vo.getStatus(), PaymentStatusEnum.class));
    }

    private String generateNo(String prefix) {
        return prefix + LocalDateTime.now().format(TRADE_NO_FORMATTER) + ThreadLocalRandom.current().nextInt(100000, 999999);
    }
}
