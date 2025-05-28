package org.wj.letsrock.domain.user.service.impl;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.user.service.UserActivityRankService;
import org.wj.letsrock.domain.user.service.UserService;
import org.wj.letsrock.enums.ActivityRankTimeEnum;
import org.wj.letsrock.domain.user.model.dto.RankItemDTO;
import org.wj.letsrock.domain.user.model.dto.SimpleUserInfoDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-22:07
 **/
@Service
public class UserActivityRankServiceImpl implements UserActivityRankService {
    @Autowired
    private UserService userService;
    @Autowired
    private CacheService cacheService;




    @Override
    public List<RankItemDTO> queryRankList(ActivityRankTimeEnum time, int size) {

        // 1. 获取topN的活跃用户

        String rankKey = time == ActivityRankTimeEnum.DAY ? CacheKey.todayRankKey() :CacheKey.monthRankKey();
        List<ImmutablePair<String, Double>> rankList = cacheService.zTopNScore(rankKey, size);
        if (CollectionUtils.isEmpty(rankList)) {
            return Collections.emptyList();
        }

        // 2. 查询用户对应的基本信息
        // 构建userId -> 活跃评分的map映射，用于补齐用户信息
        Map<Long, Integer> userScoreMap = rankList.stream().collect(Collectors.toMap(s -> Long.valueOf(s.getLeft()), s -> s.getRight().intValue()));
        List<SimpleUserInfoDTO> users = userService.batchQuerySimpleUserInfo(userScoreMap.keySet());

        // 3. 根据评分进行排序
        List<RankItemDTO> rank = users.stream()
                .map(user -> new RankItemDTO().setUser(user).setScore(userScoreMap.getOrDefault(user.getUserId(), 0)))
                .sorted((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()))
                .collect(Collectors.toList());

        // 4. 补齐每个用户的排名
        IntStream.range(0, rank.size()).forEach(i -> rank.get(i).setRank(i + 1));
        return rank;
    }
}
