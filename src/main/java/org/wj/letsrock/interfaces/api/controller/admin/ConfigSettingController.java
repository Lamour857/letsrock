package org.wj.letsrock.interfaces.api.controller.admin;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wj.letsrock.application.config.ConfigApplicationService;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.enums.article.PushStatusEnum;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.config.model.dto.ConfigDTO;
import org.wj.letsrock.domain.config.model.request.ConfigReq;
import org.wj.letsrock.domain.config.model.request.SearchConfigReq;
import org.wj.letsrock.domain.config.service.ConfigSettingService;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-12:44
 **/
@RestController
@PreAuthorize("hasRole('admin')")
@Api(value = "后台运营配置管理控制器", tags = "配置管理")
@RequestMapping(path = { "admin/config/"})
public class ConfigSettingController {
    @Autowired
    private ConfigApplicationService configService;

   // //@Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public ResultVo<String> save(@RequestBody ConfigReq configReq) {
        configService.saveConfig(configReq);
        return ResultVo.ok();
    }

    //@Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public ResultVo<String> delete(@RequestParam(name = "configId") Integer configId) {
        configService.deleteConfig(configId);
        return ResultVo.ok();
    }

    //@Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public ResultVo<String> operate(@RequestParam(name = "configId") Integer configId,
                                 @RequestParam(name = "pushStatus") Integer pushStatus) {
        if (pushStatus != PushStatusEnum.OFFLINE.getCode() && pushStatus!= PushStatusEnum.ONLINE.getCode()) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        configService.operateConfig(configId, pushStatus);
        return ResultVo.ok();
    }

    /**
     * 获取配置列表
     *
     * @return
     */
    @PostMapping(path = "list")
    public ResultVo<PageResultVo<ConfigDTO>> list(@RequestBody SearchConfigReq req) {
        PageResultVo<ConfigDTO> bannerDTOPageVo = configService.getConfigList(req);
        return ResultVo.ok(bannerDTOPageVo);
    }
}
