package net.lab1024.sa.admin.module.business.ad.tk.account.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.ad.tk.account.domain.req.AdvAccountQueryForm;
import net.lab1024.sa.admin.module.business.ad.tk.account.domain.resp.AccountResp;
import net.lab1024.sa.admin.module.business.ad.tk.account.service.AccountService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Version 1.0.0
 * @Date 2026-10-1 16:48
 * @Author by zjh
 */
@RestController
@RequestMapping("/ad/tk/account")
@Tag(name = AdminSwaggerTagConst.Business.ADV_ACCOUNT)
public class AccountController {

    @Resource
    private AccountService accountService;

    @Operation(summary = "分页查询广告主账户信息 @author zjh")
    @PostMapping("/page/query")
    @SaCheckPermission("ad:tk:account:query")
    public ResponseDTO<PageResult<AccountResp>> queryByPage(@RequestBody @Valid AdvAccountQueryForm queryForm) {
        PageResult<AccountResp> accountRespPageResult = accountService.queryByPage(queryForm);
        return ResponseDTO.ok(accountRespPageResult);
    }
}
