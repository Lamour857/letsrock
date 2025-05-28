package org.wj.letsrock.interfaces.api.controller.admin;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wj.letsrock.application.config.ConfigApplicationService;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.config.model.dto.GlobalConfigDTO;
import org.wj.letsrock.domain.config.model.request.GlobalConfigReq;
import org.wj.letsrock.domain.config.model.request.SearchGlobalConfigReq;
import org.wj.letsrock.domain.config.service.GlobalConfigService;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-13:05
 **/
@RestController
@Api(value = "全局配置管理控制器", tags = "全局配置")
@RequestMapping(path = {"admin/global/config/"})
public class GlobalConfigController {

    @Autowired
    private ConfigApplicationService configService;

   //@Permission
    @PostMapping(path = "save")
    public ResultVo<String> save(@RequestBody GlobalConfigReq req) {
        configService.save(req);
        return ResultVo.ok();
    }

   //@Permission
    @GetMapping(path = "delete")
    public ResultVo<String> delete(@RequestParam(name = "id") Long id) {
        configService.delete(id);
        return ResultVo.ok();
    }

    @PostMapping(path = "list")
   //@Permission
    public ResultVo<PageResultVo<GlobalConfigDTO>> list(@RequestBody SearchGlobalConfigReq req) {
        PageResultVo<GlobalConfigDTO> page = configService.getList(req);
        return ResultVo.ok(page);
    }
}
