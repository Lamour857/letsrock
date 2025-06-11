package org.wj.letsrock.domain.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.wj.letsrock.domain.user.model.dto.SimpleUserInfoDTO;
import org.wj.letsrock.domain.user.model.dto.UserFootStatisticDTO;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;
import org.wj.letsrock.model.vo.PageParam;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-17:23
 **/
public interface UserFootRepository extends IService<UserFootDO> {
    UserFootDO getByDocumentAndUserId(Long documentId, Integer type, Long userId);

    List<SimpleUserInfoDTO> listDocumentPraisedUsers(Long articleId, Integer type, int size);

    Long countCommentPraise(Long commentId);

    Page<UserFootDO> listReadArticleByUserId(Long userId, PageParam pageParam);

    Page<UserFootDO> listCollectedArticlesByUserId(Long userId, PageParam pageParam);

    UserFootStatisticDTO getFootCount();
}
