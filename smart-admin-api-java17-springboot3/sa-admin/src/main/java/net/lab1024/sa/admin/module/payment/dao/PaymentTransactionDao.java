package net.lab1024.sa.admin.module.payment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.payment.domain.entity.PaymentTransactionEntity;
import net.lab1024.sa.admin.module.payment.domain.form.PaymentQueryForm;
import net.lab1024.sa.admin.module.payment.domain.vo.PaymentTransactionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 支付交易流水 dao
 *
 * @Author 1024创新实验室
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface PaymentTransactionDao extends BaseMapper<PaymentTransactionEntity> {

    /**
     * 分页查询交易流水
     */
    List<PaymentTransactionVO> query(@Param("page") Page<?> page, @Param("query") PaymentQueryForm queryForm);

    /**
     * 根据商户订单号 + 交易类型 查询
     */
    PaymentTransactionEntity selectByOutTradeNo(@Param("outTradeNo") String outTradeNo, @Param("tradeType") Integer tradeType);
}
