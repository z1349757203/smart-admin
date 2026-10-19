package net.lab1024.sa.admin.module.business.ad.tk.account.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ad.tk.account.dao.AdvAccountDao;
import net.lab1024.sa.admin.module.business.ad.tk.account.domain.req.AdvAccountQueryForm;
import net.lab1024.sa.admin.module.business.ad.tk.account.domain.resp.AccountResp;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Version 1.0.0
 * @Date 2026-10-1 17:05
 * @Author by zjh
 */
@Service
@Slf4j
public class AccountService {

    private AdvAccountDao advAccountDao;

    public PageResult<AccountResp> queryByPage(AdvAccountQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AccountResp> respList = advAccountDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, respList);
    }


}
