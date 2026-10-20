package net.lab1024.sa.admin.module.payment.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.payment.domain.form.PaymentCreateForm;
import net.lab1024.sa.admin.module.payment.domain.form.PaymentQueryForm;
import net.lab1024.sa.admin.module.payment.domain.form.PaymentRefundForm;
import net.lab1024.sa.admin.module.payment.domain.vo.PaymentCreateVO;
import net.lab1024.sa.admin.module.payment.domain.vo.PaymentTransactionVO;
import net.lab1024.sa.admin.module.payment.service.PaymentTransactionService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付交易
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Tag(name = AdminSwaggerTagConst.Payment.PAYMENT_TRANSACTION)
public class PaymentTransactionController {

    @Resource
    private PaymentTransactionService paymentTransactionService;

    @Operation(summary = "发起支付（统一下单）")
    @PostMapping("/payment/transaction/create")
    @SaCheckPermission("payment:transaction:create")
    public ResponseDTO<PaymentCreateVO> create(@RequestBody @Valid PaymentCreateForm form) {
        return paymentTransactionService.create(form);
    }

    @Operation(summary = "分页查询交易流水")
    @PostMapping("/payment/transaction/query")
    @SaCheckPermission("payment:transaction:query")
    public ResponseDTO<PageResult<PaymentTransactionVO>> query(@RequestBody @Valid PaymentQueryForm queryForm) {
        return paymentTransactionService.query(queryForm);
    }

    @Operation(summary = "交易详情")
    @GetMapping("/payment/transaction/{transactionId}")
    @SaCheckPermission("payment:transaction:query")
    public ResponseDTO<PaymentTransactionVO> detail(@PathVariable Long transactionId) {
        return paymentTransactionService.detail(transactionId);
    }

    @Operation(summary = "主动同步渠道交易状态")
    @GetMapping("/payment/transaction/sync/{outTradeNo}")
    @SaCheckPermission("payment:transaction:query")
    public ResponseDTO<PaymentTransactionVO> syncStatus(@PathVariable String outTradeNo) {
        return paymentTransactionService.syncStatus(outTradeNo);
    }

    @Operation(summary = "退款")
    @PostMapping("/payment/transaction/refund")
    @SaCheckPermission("payment:transaction:refund")
    public ResponseDTO<PaymentTransactionVO> refund(@RequestBody @Valid PaymentRefundForm form) {
        return paymentTransactionService.refund(form);
    }

    @Operation(summary = "渠道异步回调")
    @PostMapping("/payment/transaction/notify/{channelCode}")
    public ResponseDTO<String> notify(@PathVariable Integer channelCode, @RequestBody String body) {
        return paymentTransactionService.handleNotify(channelCode, body);
    }
}
