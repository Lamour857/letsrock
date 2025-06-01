package org.wj.letsrock.domain.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.user.repository.UserFootRepository;
import org.wj.letsrock.domain.user.service.UserFootService;
import org.wj.letsrock.enums.article.DocumentTypeEnum;
import org.wj.letsrock.enums.OperateTypeEnum;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.comment.model.entity.CommentDO;
import org.wj.letsrock.domain.user.model.dto.SimpleUserInfoDTO;
import org.wj.letsrock.domain.user.model.dto.UserFootStatisticDTO;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;
import org.wj.letsrock.infrastructure.persistence.mybatis.user.UserFootRepositoryImpl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-22:31
 **/
@Service
@Slf4j
public class UserFootServiceImpl implements UserFootService {
    @Autowired
    private UserFootRepository userFootDao;

    @Override
    public UserFootDO saveOrUpdateUserFoot(DocumentTypeEnum documentType, Long documentId, Long authorId, Long userId, OperateTypeEnum operateTypeEnum) {
        // 查询是否有该足迹；有则更新，没有则插入
        UserFootDO readUserFootDO = userFootDao.getByDocumentAndUserId(documentId, documentType.getCode(), userId);
        if (readUserFootDO == null) {
            readUserFootDO = new UserFootDO();
            readUserFootDO.setUserId(userId);
            readUserFootDO.setDocumentId(documentId);
            readUserFootDO.setDocumentType(documentType.getCode());
            readUserFootDO.setDocumentUserId(authorId);
            setUserFootStat(readUserFootDO, operateTypeEnum);
            userFootDao.save(readUserFootDO);
        } else if (setUserFootStat(readUserFootDO, operateTypeEnum)) {
            readUserFootDO.setUpdateTime(new Date());
            userFootDao.updateById(readUserFootDO);
        }
        return readUserFootDO;
    }

    @Override
    public Page<UserFootDO> queryUserReadArticleList(Long userId, PageParam pageParam) {
        return userFootDao.listReadArticleByUserId(userId, pageParam);
    }

    @Override
    public Page<UserFootDO> queryUserCollectionArticleList(Long userId, PageParam pageParam) {
        return userFootDao.listCollectedArticlesByUserId(userId, pageParam);
    }

    @Override
    public UserFootStatisticDTO getFootCount() {
        return userFootDao.getFootCount();
    }

    @Override
    public void removeCommentFoot(CommentDO comment, Long articleAuthor, Long parentCommentAuthor) {
        saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, comment.getArticleId(), articleAuthor, comment.getUserId(), OperateTypeEnum.DELETE_COMMENT);
        if (comment.getParentCommentId() != null) {
            // 如果需要展示父评论的子评论数量，authorId 需要传父评论的 userId
            saveOrUpdateUserFoot(DocumentTypeEnum.COMMENT, comment.getParentCommentId(), parentCommentAuthor, comment.getUserId(), OperateTypeEnum.DELETE_COMMENT);
        }
    }

    private boolean setUserFootStat(UserFootDO userFootDO, OperateTypeEnum operate) {
        switch (operate) {
            case READ:
                // 设置为已读
                userFootDO.setReadStat(1);
                // 需要更新时间，用于浏览记录
                return true;
            case PRAISE:    // 点赞相关
            case CANCEL_PRAISE:
                // operateSat
                return compareAndUpdate(userFootDO::getPraiseStat, userFootDO::setPraiseStat, operate.getDbStatCode());
            case COLLECTION:    // 收藏相关
            case CANCEL_COLLECTION:
                return compareAndUpdate(userFootDO::getCollectionStat, userFootDO::setCollectionStat, operate.getDbStatCode());
            case COMMENT:   // 评论相关
            case DELETE_COMMENT:
                return compareAndUpdate(userFootDO::getCommentStat, userFootDO::setCommentStat, operate.getDbStatCode());
            default:
                return false;
        }
    }

    private <T> boolean compareAndUpdate(Supplier<T> supplier, Consumer<T> consumer, T input) {
        if (Objects.equals(supplier.get(), input)) {
            return false;
        }
        consumer.accept(input);
        return true;
    }

    @Override
    public List<SimpleUserInfoDTO> queryArticlePraisedUsers(Long articleId) {
        return userFootDao.listDocumentPraisedUsers(articleId, DocumentTypeEnum.ARTICLE.getCode(), 10);
    }

    @Override
    public UserFootDO queryUserFoot(Long documentId, Integer type, Long userId) {
        return userFootDao.getByDocumentAndUserId(documentId, type, userId);
    }
    @Override
    public void saveCommentFoot(CommentDO comment, Long articleAuthor, Long parentCommentAuthor) {
        // 保存文章对应的评论足迹
        saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, comment.getArticleId(), articleAuthor, comment.getUserId(), OperateTypeEnum.COMMENT);
        // 如果是子评论，则找到父评论的记录，然后设置为已评
        if (comment.getParentCommentId() != null && comment.getParentCommentId() != 0) {
            // 如果需要展示父评论的子评论数量，authorId 需要传父评论的 userId
            saveOrUpdateUserFoot(DocumentTypeEnum.COMMENT, comment.getParentCommentId(), parentCommentAuthor, comment.getUserId(), OperateTypeEnum.COMMENT);
        }
    }
}
