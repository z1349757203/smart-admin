package net.lab1024.sa.admin.module.business.ad.tk.account.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ad.tk.account.domain.entities.AdvAccountEntity;
import net.lab1024.sa.admin.module.business.ad.tk.account.domain.req.AdvAccountQueryForm;
import net.lab1024.sa.admin.module.business.ad.tk.account.domain.resp.AccountResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * OA办公-OA银行信息
 *
 * @Author 1024创新实验室:善逸
 * @Date 2022/6/23 21:59:22
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface AdvAccountDao extends BaseMapper<AdvAccountEntity> {

    /**
     * 根据账号查询
     */
    AdvAccountEntity queryByAccountNumber(@Param("enterpriseId") Long enterpriseId, @Param("accountNumber") String accountNumber, @Param("excludeBankId") Long excludeBankId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 删除银行信息
     *
     */
    void deleteBank(@Param("bankId") Long bankId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 银行信息分页查询
     *
     */
    List<AccountResp> queryPage(Page page, @Param("queryForm") AdvAccountQueryForm queryForm);

    /**
     * 查询银行信息详情
     */
    AccountResp getDetail(@Param("bankId") Long bankId, @Param("deletedFlag") Boolean deletedFlag);
}
