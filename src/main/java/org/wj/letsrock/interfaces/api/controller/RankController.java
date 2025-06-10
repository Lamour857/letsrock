package org.wj.letsrock.interfaces.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wj.letsrock.application.user.UserApplicationService;
import org.wj.letsrock.domain.user.service.UserService;
import org.wj.letsrock.enums.ActivityRankTimeEnum;
import org.wj.letsrock.infrastructure.security.annotation.AnonymousAccess;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.user.model.dto.RankInfoDTO;
import org.wj.letsrock.domain.user.model.dto.RankItemDTO;
import org.wj.letsrock.domain.user.service.UserActivityRankService;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-22:01
 **/
@RestController
@RequestMapping("rank")
public class RankController {
    @Autowired
    private UserApplicationService userService;
    /**
     * 活跃用户排行榜
     *
     * @param time
     * @param model
     * @return
     */
    @AnonymousAccess
    @RequestMapping(path = "/rank/{time}")
    public ResultVo<RankInfoDTO> rank(@PathVariable(value = "time") String time) {
        return ResultVo.ok(userService.queryUserRank(time));
    }
}
