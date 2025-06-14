package org.wj.letsrock.domain.article.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wj.letsrock.domain.article.model.dto.ColumnArticleDTO;
import org.wj.letsrock.domain.article.model.request.*;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.domain.article.repository.ColumnArticleRepository;
import org.wj.letsrock.domain.article.repository.ColumnRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.ColumnArticleRepositoryImpl;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.ColumnRepositoryImpl;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.utils.NumUtil;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.domain.article.converter.ColumnConverter;
import org.wj.letsrock.domain.article.model.dto.ColumnDTO;
import org.wj.letsrock.domain.article.model.dto.SimpleColumnDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.model.entity.ColumnArticleDO;
import org.wj.letsrock.domain.article.model.entity.ColumnInfoDO;
import org.wj.letsrock.domain.article.model.param.SearchColumnArticleParams;
import org.wj.letsrock.domain.article.model.param.SearchColumnParams;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.ArticleRepositoryImpl;
import org.wj.letsrock.domain.article.service.ColumnSettingService;
import org.wj.letsrock.domain.user.model.dto.BaseUserInfoDTO;
import org.wj.letsrock.domain.user.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-19:39
 **/
@Service
public class ColumnSettingServiceImpl implements ColumnSettingService {
    @Autowired
    private ColumnArticleRepository columnArticleDao;
    @Autowired
    private ColumnRepository columnDao;
    @Autowired
    private ArticleRepository articleDao;
    @Autowired
    private UserService userService;
    @Override
    public void saveColumnArticle(Long articleId, Long columnId) {
        // 转换参数
        // 插入的时候，需要判断是否已经存在
        ColumnArticleDO exist = columnArticleDao.getOne(Wrappers.<ColumnArticleDO>lambdaQuery()
                .eq(ColumnArticleDO::getArticleId, articleId));
        if (exist != null) {
            if (!Objects.equals(columnId, exist.getColumnId())) {
                // 更新
                exist.setColumnId(columnId);
                columnArticleDao.updateById(exist);
            }
        } else {
            // 将文章保存到专栏中，章节序号+1
            ColumnArticleDO columnArticleDO = new ColumnArticleDO();
            columnArticleDO.setColumnId(columnId);
            columnArticleDO.setArticleId(articleId);
            // section 自增+1
            Integer maxSection = columnArticleDao.selectMaxSection(columnId);
            columnArticleDO.setSection(maxSection + 1);
            columnArticleDao.save(columnArticleDO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sortColumnArticleApi(SortColumnArticleReq req) {
        // 根据 req 的两个 ID 调换两篇文章的顺序
        ColumnArticleDO activeDO = columnArticleDao.getById(req.getActiveId());
        ColumnArticleDO overDO = columnArticleDao.getById(req.getOverId());
        if (activeDO != null && overDO != null && !activeDO.getId().equals(overDO.getId())) {
            Integer activeSection = activeDO.getSection();
            Integer overSection = overDO.getSection();
            // 假如原始顺序为1、2、3、4
            //
            //把 1 拖到 4 后面 2 变 1 3 变 2 4 变 3 1 变 4
            //把 1 拖到 3 后面 2 变 1 3 变 2 4 不变 1 变 3
            //把 1 拖到 2 后面 2 变 1 3 不变 4 不变 1 变 2
            //把 2 拖到 4 后面 1 不变 3 变 2 4 变 3 2 变 4
            //把 2 拖到 3 后面 1 不变 3 变 2 4 不变 2 变 3
            //把 3 拖到 4 后面 1 不变 2 不变 4 变 3 3 变 4
            //把 4 拖到 1 前面 1 变 2 2 变 3 3 变 4
            //把 4 拖到 2 前面 1 不变 2 变 3 3 变 4  4 变 1
            //把 4 拖到 3 前面 1 不变 2 不变 3 变 4 4 变 1
            //把 3 拖到 1 前面 1 变 2 2 变 3 3 变 4 4 变 1
            //依次类推
            // 1. 如果 activeSection > overSection，那么 activeSection - 1 到 overSection 的 section 都要 +1
            // 向上拖动
            if (activeSection > overSection) {
                // 当 activeSection 大于 overSection 时，表示文章被向上拖拽。
                // 需要将 activeSection 到 overSection（不包括 activeSection 本身）之间的所有文章的 section 加 1，
                // 并将 activeSection 设置为 overSection。
                columnArticleDao.update(null, Wrappers.<ColumnArticleDO>lambdaUpdate()
                        .setSql("section = section + 1") // 将符合条件的记录的 section 字段的值增加 1
                        .eq(ColumnArticleDO::getColumnId, overDO.getColumnId()) // 指定要更新记录的 columnId 条件
                        .ge(ColumnArticleDO::getSection, overSection) // 指定 section 字段的下限（包含此值）
                        .lt(ColumnArticleDO::getSection, activeSection)); // 指定 section 字段的上限

                // 将 activeDO 的 section 设置为 overSection
                activeDO.setSection(overSection);
                columnArticleDao.updateById(activeDO);
            } else {
                // 2. 如果 activeSection < overSection，
                // 那么 activeSection + 1 到 overSection 的 section 都要 -1
                // 向下拖动
                // 需要将 activeSection 到 overSection（包括 overSection）之间的所有文章的 section 减 1
                columnArticleDao.update(null, Wrappers.<ColumnArticleDO>lambdaUpdate()
                        .setSql("section = section - 1") // 将符合条件的记录的 section 字段的值减少 1
                        .eq(ColumnArticleDO::getColumnId, overDO.getColumnId()) // 指定要更新记录的 columnId 条件
                        .gt(ColumnArticleDO::getSection, activeSection) // 指定 section 字段的下限（不包含此值）
                        .le(ColumnArticleDO::getSection, overSection)); // 指定 section 字段的上限（包含此值）

                // 将 activeDO 的 section 设置为 overSection -1
                activeDO.setSection(overSection);
                columnArticleDao.updateById(activeDO);

            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sortColumnArticleByIDApi(SortColumnArticleByIdReq req) {
        // 获取要重新排序的专栏文章
        ColumnArticleDO columnArticleDO = columnArticleDao.getById(req.getId());
        // 不等于空
        if (columnArticleDO == null) {
            throw ExceptionUtil.of(StatusEnum.COLUMN_ARTICLE_EXISTS, "教程不存在");
        }
        // 如果顺序没变
        if (req.getSort().equals(columnArticleDO.getSection())) {
            return;
        }
        // 获取教程可以调整的最大顺序
        Integer maxSection = columnArticleDao.selectMaxSection(columnArticleDO.getColumnId());
        // 如果输入的顺序大于最大顺序，提示错误
        if (req.getSort() > maxSection) {
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "顺序超出范围");
        }
        // 查看输入的顺序是否存在
        ColumnArticleDO changeColumnArticleDO = columnArticleDao.selectBySection(columnArticleDO.getColumnId(), req.getSort());
        // 如果存在，交换顺序
        if (changeColumnArticleDO != null) {
            // 交换顺序
            columnArticleDao.update(null, Wrappers.<ColumnArticleDO>lambdaUpdate()
                    .set(ColumnArticleDO::getSection, columnArticleDO.getSection())
                    .eq(ColumnArticleDO::getId, changeColumnArticleDO.getId()));
            columnArticleDao.update(null, Wrappers.<ColumnArticleDO>lambdaUpdate()
                    .set(ColumnArticleDO::getSection, changeColumnArticleDO.getSection())
                    .eq(ColumnArticleDO::getId, columnArticleDO.getId()));
        } else {
            // 如果不存在，直接修改顺序
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "输入的顺序不存在，无法完成交换");
        }
    }

    @Override
    public PageResultVo<ColumnDTO> getColumnList(SearchColumnReq req) {
        // 转换参数
        SearchColumnParams params = ColumnConverter.reqToSearchParams(req);
        // 查询
        List<ColumnInfoDO> columnList = columnDao.listColumnsByParams(params, PageParam.newPageInstance(req.getPageNumber(), req.getPageSize()));
        // 转属性
        List<ColumnDTO> columnDTOS = ColumnConverter.infoToDTOs(columnList);

        // 进行优化，由原来的多次查询用户信息，改为一次查询用户信息
        // 获取所有需要的用户id
        // 判断 columnDTOS 是否为空
        if (CollUtil.isNotEmpty(columnDTOS)) {
            List<Long> userIds = columnDTOS.stream().map(ColumnDTO::getAuthor).collect(Collectors.toList());

            // 查询所有的用户信息
            List<BaseUserInfoDTO> users = userService.batchQueryBasicUserInfo(userIds);

            // 创建一个id到用户信息的映射
            Map<Long, BaseUserInfoDTO> userMap = users.stream().collect(Collectors.toMap(BaseUserInfoDTO::getId, Function.identity()));

            // 设置作者信息
            columnDTOS.forEach(columnDTO -> {
                BaseUserInfoDTO user = userMap.get(columnDTO.getAuthor());
                columnDTO.setAuthorName(user.getUsername());
                columnDTO.setAuthorAvatar(user.getAvatar());
                columnDTO.setAuthorProfile(user.getProfile());
            });
        }

        Integer totalCount = columnDao.countColumnsByParams(params);
        return PageResultVo.build(columnDTOS, req.getPageSize(), req.getPageNumber(), totalCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteColumnArticle(Long id) {
        ColumnArticleDO columnArticleDO = columnArticleDao.getById(id);
        if (columnArticleDO != null) {
            columnArticleDao.removeById(id);
            // 删除的时候，批量更新 section，比如说原来是 1,2,3,4,5,6,7,8,9,10，删除 5，那么 6-10 的 section 都要减 1
            columnArticleDao.update(null, Wrappers.<ColumnArticleDO>lambdaUpdate()
                    .setSql("section = section - 1")
                    .eq(ColumnArticleDO::getColumnId, columnArticleDO.getColumnId())
                    // section 大于 1
                    .gt(ColumnArticleDO::getSection, 1)
                    .gt(ColumnArticleDO::getSection, columnArticleDO.getSection()));
        }
    }

    @Override
    public void deleteColumn(Long columnId) {
        columnDao.deleteColumn(columnId);
    }

    @Override
    public void saveColumn(ColumnReq req) {
        ColumnInfoDO columnInfoDO = ColumnConverter.toDo(req);
        if (NumUtil.nullOrZero(req.getColumnId())) {
            columnDao.save(columnInfoDO);
        } else {
            columnInfoDO.setId(req.getColumnId());
            columnDao.updateById(columnInfoDO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveColumnArticle(ColumnArticleReq req) {
        // 转换参数
        ColumnArticleDO columnArticleDO = ColumnConverter.reqToDO(req);
        if (NumUtil.nullOrZero(columnArticleDO.getId())) {
            // 插入的时候，需要判断是否已经存在
            ColumnArticleDO exist = columnArticleDao.getOne(Wrappers.<ColumnArticleDO>lambdaQuery()
                    .eq(ColumnArticleDO::getColumnId, columnArticleDO.getColumnId())
                    .eq(ColumnArticleDO::getArticleId, columnArticleDO.getArticleId()));
            if (exist != null) {
                throw ExceptionUtil.of(StatusEnum.COLUMN_ARTICLE_EXISTS, "请勿重复添加");
            }

            // section 自增+1
            Integer maxSection = columnArticleDao.selectMaxSection(columnArticleDO.getColumnId());
            columnArticleDO.setSection(maxSection + 1);
            columnArticleDao.save(columnArticleDO);
        } else {
            columnArticleDao.updateById(columnArticleDO);
        }

        // 同时，更新 article 的 shortTitle 短标题
        if (req.getShortTitle() != null) {
            ArticleDO articleDO = new ArticleDO();
            articleDO.setShortTitle(req.getShortTitle());
            articleDO.setId(req.getArticleId());
            articleDao.updateById(articleDO);
        }
    }

    @Override
    public List<SimpleColumnDTO> listSimpleColumnBySearchKey(String key) {
        LambdaQueryWrapper<ColumnInfoDO> query = Wrappers.lambdaQuery();
        query.select(ColumnInfoDO::getId, ColumnInfoDO::getColumnName, ColumnInfoDO::getCover)
                .and(!StringUtils.isEmpty(key),
                        v -> v.like(ColumnInfoDO::getColumnName, key)
                )
                .orderByDesc(ColumnInfoDO::getId);
        List<ColumnInfoDO> articleDOS = columnDao.list(query);
        return ColumnConverter.infoToSimpleDTOs(articleDOS);
    }

    @Override
    public PageResultVo<ColumnArticleDTO> getColumnArticleList(SearchColumnArticleReq req) {
        // 转换参数
        SearchColumnArticleParams params = ColumnConverter.toSearchParams(req);
        // 查询
        List<ColumnArticleDTO> simpleArticleDTOS = columnDao.listColumnArticlesDetail(params, PageParam.newPageInstance(req.getPageNumber(), req.getPageSize()));
        int totalCount = columnDao.countColumnArticles(params);
        return PageResultVo.build(simpleArticleDTOS, req.getPageSize(), req.getPageNumber(), totalCount);
    }

}
