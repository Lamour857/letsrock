package org.wj.letsrock.domain.statistics.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.service.ArticleReadService;
import org.wj.letsrock.domain.article.service.ColumnService;
import org.wj.letsrock.domain.statistics.model.dto.StatisticsCountDTO;
import org.wj.letsrock.domain.statistics.model.dto.StatisticsDayDTO;
import org.wj.letsrock.domain.statistics.service.RequestCountService;
import org.wj.letsrock.domain.statistics.service.StatisticsService;
import org.wj.letsrock.domain.user.model.dto.UserFootStatisticDTO;
import org.wj.letsrock.domain.user.service.UserFootService;
import org.wj.letsrock.domain.user.service.UserService;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-16:24
 **/
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private UserFootService userFootService;
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleReadService articleReadService;
    @Autowired
    private RequestCountService requestCountService;
    @Autowired
    private ColumnService columnService;


    @Override
    public StatisticsCountDTO getStatisticsCount() {
        // 从 user_foot 表中查询点赞数、收藏数、留言数、阅读数
        UserFootStatisticDTO userFootStatisticDTO =  userFootService.getFootCount();
        if (userFootStatisticDTO == null) {
            userFootStatisticDTO = new UserFootStatisticDTO();
        }
        return StatisticsCountDTO.builder()
                .userCount(userService.getUserCount())
                .articleCount(articleReadService.getArticleCount())
                .pvCount(requestCountService.getPvTotalCount())
                .tutorialCount(columnService.getTutorialCount())
                .commentCount(userFootStatisticDTO.getCommentCount())
                .collectCount(userFootStatisticDTO.getCollectionCount())
                .likeCount(userFootStatisticDTO.getPraiseCount())
                .readCount(userFootStatisticDTO.getReadCount())
                .build();
    }

    @Override
    public List<StatisticsDayDTO> getPvUvDayList(Integer day) {
        return requestCountService.getPvUvDayList(day);
    }
}
