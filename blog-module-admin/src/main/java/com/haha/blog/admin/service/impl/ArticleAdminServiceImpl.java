package com.haha.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haha.blog.admin.event.message.ArticleChangeMessage;
import com.haha.blog.admin.service.IArticleAdminService;
import com.haha.blog.common.domain.dos.*;
import com.haha.blog.admin.domain.dto.Article.DeleteArticleDTO;
import com.haha.blog.admin.domain.dto.Article.UpdateArticleDTO;
import com.haha.blog.admin.domain.query.article.ArticleDetailQuery;
import com.haha.blog.admin.domain.query.article.ArticlePageListQuery;
import com.haha.blog.admin.domain.query.article.PublishArticleQuery;
import com.haha.blog.admin.domain.vo.article.ArticleDetailVO;
import com.haha.blog.admin.domain.vo.article.ArticlePageListVO;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haha.blog.common.utils.BeanUtils;
import com.haha.blog.common.utils.PageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author li
 * @since 2026-06-20
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleAdminServiceImpl extends ServiceImpl<ArticleMapper, ArticleDO> implements IArticleAdminService {


    private final ArticleMapper articleMapper;
    private final ArticleContentMapper articleContentMapper;
    private final ArticleCategoryRelMapper articleCategoryRelMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final ArticleTagRelMapper articleTagRelMapper;
    private final ApplicationContext applicationContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishArticle(PublishArticleQuery query) {
        // 1. 保存文章
        ArticleDO articleDO = BeanUtils.copyBean(query, ArticleDO.class);
        articleMapper.insert(articleDO);
        // 2. 获取文章id
        Long articleId = articleDO.getId();
        // 3. 保存文章正文
        ArticleContentDO contentDO = new ArticleContentDO()
                .setArticleId(articleId)
                .setContent(query.getContent());
        articleContentMapper.insert(contentDO);
        // 4. 保存文章关联的分类
        // 4.1 判断分类是否存在
        CategoryDO categoryDO = categoryMapper.selectById(query.getCategoryId());
        if (Objects.isNull(categoryDO)) {
            log.warn("==> 分类不存在, categoryId: {}", query.getCategoryId());
            throw new BizException("分类不存在，发布文章失败");
        }
        ArticleCategoryRelDO categoryRelDO = new ArticleCategoryRelDO()
                .setArticleId(articleId)
                .setCategoryId(query.getCategoryId());
        articleCategoryRelMapper.insert(categoryRelDO);
        // 5. 保存文章关联的标签
        List<String> tags = query.getTags();
        insertTags(articleId, tags);
        // 发布消息，更新分类下的文章数量
        applicationContext.publishEvent(new ArticleChangeMessage(this));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(DeleteArticleDTO dto) {
        Long articleId = dto.getId();
        // 1. 删除文章分类关联记录
        articleCategoryRelMapper.delete(Wrappers.<ArticleCategoryRelDO>lambdaQuery()
                .eq(ArticleCategoryRelDO::getArticleId, articleId));
        // 2. 删除文章标签关联记录
        articleTagRelMapper.delete(Wrappers.<ArticleTagRelDO>lambdaQuery()
                .eq(ArticleTagRelDO::getArticleId, articleId));
        // 3. 删除文章正文记录
        articleContentMapper.delete(Wrappers.<ArticleContentDO>lambdaQuery()
                .eq(ArticleContentDO::getArticleId, articleId));
        // 4. 删除文章记录
        baseMapper.deleteById(articleId);
        // 发布消息，更新分类下的文章数量
        applicationContext.publishEvent(new ArticleChangeMessage(this));
    }

    @Override
    public PageDTO<ArticlePageListVO> queryArticlePageList(ArticlePageListQuery query) {
        Long current = query.getCurrent();
        Long size = query.getSize();
        // 获取查询条件
        String title = query.getTitle();
        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        // 分页查询
        Page<ArticleDO> page = lambdaQuery()
                .like(StringUtils.isNoneBlank(title), ArticleDO::getTitle, title)
                .ge(Objects.nonNull(startDateTime), ArticleDO::getCreateTime, startDateTime)
                .le(Objects.nonNull(endDateTime), ArticleDO::getCreateTime, endDateTime)
                .orderByDesc(ArticleDO::getCreateTime)
                .page(new Page<>(current, size));
        List<ArticleDO> ArticleDOList = page.getRecords();
        if (CollectionUtils.isEmpty(ArticleDOList)) {
            return PageDTO.success(page, Collections.emptyList());
        }
        List<ArticlePageListVO> voList = ArticleDOList.stream()
                .map(articleDO -> BeanUtils.copyBean(articleDO, ArticlePageListVO.class))
                .collect(Collectors.toList());
        return PageDTO.success(page, voList);
    }

    @Override
    public ArticleDetailVO queryArticleDetail(ArticleDetailQuery query) {
        Long articleId = query.getId();
        // 查询文章
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if (Objects.isNull(articleDO)) {
            throw new BizException("该文章不存在");
        }
        // 查询文章正文
        ArticleContentDO articleContentDO = articleContentMapper.selectOne(Wrappers.<ArticleContentDO>lambdaQuery()
                .eq(ArticleContentDO::getArticleId, articleId));
        if (Objects.isNull(articleContentDO)) {
            throw new BizException("该文章正文不存在");
        }
        // 查询分类
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectOne(Wrappers.<ArticleCategoryRelDO>lambdaQuery()
                .eq(ArticleCategoryRelDO::getArticleId, articleId));
        if (Objects.isNull(articleCategoryRelDO)) {
            throw new BizException("该文章分类不存在");
        }
        // 查询标签
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectList(Wrappers.<ArticleTagRelDO>lambdaQuery()
                .eq(ArticleTagRelDO::getArticleId, articleId));
        if (CollectionUtils.isEmpty(articleTagRelDOS)) {
            throw new BizException("该文章标签不存在");
        }
        List<Long> tagIds = articleTagRelDOS.stream()
                .map(ArticleTagRelDO::getTagId)
                .collect(Collectors.toList());
        // 封装vo
        ArticleDetailVO articleDetailVO = BeanUtils.copyBean(articleDO, ArticleDetailVO.class);
        articleDetailVO.setContent(articleContentDO.getContent());
        articleDetailVO.setTagIds(tagIds);
        articleDetailVO.setCategoryId(articleCategoryRelDO.getCategoryId());
        return articleDetailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(UpdateArticleDTO dto) {
        Long articleId = dto.getId();
        // 1. 更新文章表
        ArticleDO articleDO = BeanUtils.copyBean(dto, ArticleDO.class);
        int articleRow = articleMapper.updateById(articleDO);
        if (articleRow == 0) {
            log.warn("==> 该文章不存在, articleId: {}", articleId);
            throw new BizException("文章不存在，更新失败");
        }
        // 2. 更新正文
        LambdaUpdateWrapper<ArticleContentDO> contentWrapper = Wrappers.lambdaUpdate();
        contentWrapper.set(ArticleContentDO::getContent,dto.getContent());
        contentWrapper.eq(ArticleContentDO::getArticleId, articleId);
        int contentRow = articleContentMapper.update(null, contentWrapper);
        if (contentRow == 0) {
            throw new BizException("文章正文不存在，更新失败");
        }
        // 3. 更新分类，先删除分类关联记录再更新
        Long categoryId = dto.getCategoryId();
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (categoryDO == null) {
            throw new BizException("该分类不存在，更新失败");
        }
        articleCategoryRelMapper.delete(Wrappers.<ArticleCategoryRelDO>lambdaQuery()
                .eq(ArticleCategoryRelDO::getArticleId, articleId));
        ArticleCategoryRelDO articleCategoryRelDO = new ArticleCategoryRelDO()
                .setArticleId(articleId)
                .setCategoryId(categoryId);
        articleCategoryRelMapper.insert(articleCategoryRelDO);
        // 更新标签,先删除标签关联记录再更新
        articleTagRelMapper.delete(Wrappers.<ArticleTagRelDO>lambdaQuery()
                .eq(ArticleTagRelDO::getArticleId, articleId));
        List<String> tags = dto.getTags();
        insertTags(articleId, tags);
    }

    private void insertTags(Long articleId, List<String> publishTags) {
        // 筛选提交的标签（表中不存在的标签）
        List<String> notExistTags = null;
        // 筛选提交的标签（表中已存在的标签）
        List<String> existedTags = null;
        // 查询出所有标签
        List<TagDO> tagDOS = tagMapper.selectList(null);
        // 如果表中还没有添加任何标签
        if (CollectionUtils.isEmpty(tagDOS)) {
            notExistTags = publishTags;
        } else {
            List<String> tagIds = tagDOS.stream().map(tagDO -> String.valueOf(tagDO.getId())).collect(Collectors.toList());
            // 表中已添加相关标签，则需要筛选
            // 通过标签 ID 来筛选，包含对应 ID 则表示提交的标签是表中存在的
            existedTags = publishTags.stream().filter(publishTag -> tagIds.contains(publishTag)).collect(Collectors.toList());
            // 否则则是不存在的
            notExistTags = publishTags.stream().filter(publishTag -> !tagIds.contains(publishTag)).collect(Collectors.toList());
            // 还有一种可能：按字符串名称提交上来的标签，也有可能是表中已存在的，比如表中已经有了 Java 标签，用户提交了个 java 小写的标签，需要内部装换为 Java 标签
            Map<String, Long> tagNameIdMap = tagDOS.stream().collect(Collectors.toMap(tagDO -> tagDO.getName().toLowerCase(), TagDO::getId));
            // 使用迭代器进行安全的删除操作
            Iterator<String> iterator = notExistTags.iterator();
            while (iterator.hasNext()) {
                String notExistTag = iterator.next();
                // 转小写, 若 Map 中相同的 key，则表示该新标签是重复标签
                if (tagNameIdMap.containsKey(notExistTag.toLowerCase())) {
                    // 从不存在的标签集合中清除
                    iterator.remove();
                    // 并将对应的 ID 添加到已存在的标签集合
                    existedTags.add(String.valueOf(tagNameIdMap.get(notExistTag.toLowerCase())));
                }
            }
        }
        // 将提交的上来的，已存在于表中的标签，文章-标签关联关系入库
        if (!CollectionUtils.isEmpty(existedTags)) {
            List<ArticleTagRelDO> articleTagRelDOS = new ArrayList<>();
            existedTags.forEach(tagId -> {
                ArticleTagRelDO articleTagRelDO = new ArticleTagRelDO()
                        .setArticleId(articleId)
                        .setTagId(Long.valueOf(tagId));
                articleTagRelDOS.add(articleTagRelDO);
            });
            // 批量插入
            articleTagRelMapper.insertBatchSomeColumn(articleTagRelDOS);
        }
        // 将提交的上来的，不存在于表中的标签，入库保存
        if (!CollectionUtils.isEmpty(notExistTags)) {
            // 需要先将标签入库，拿到对应标签 ID 后，再把文章-标签关联关系入库
            List<ArticleTagRelDO> articleTagRelDOS = new ArrayList<>();
            notExistTags.forEach(tagName -> {
                TagDO tagDO = TagDO.builder()
                        .name(tagName)
                        .build();
                tagMapper.insert(tagDO);

                // 拿到保存的标签 ID
                Long tagId = tagDO.getId();

                // 文章-标签关联关系
                ArticleTagRelDO articleTagRelDO = new ArticleTagRelDO()
                        .setArticleId(articleId)
                        .setTagId(tagId);
                articleTagRelDOS.add(articleTagRelDO);
            });
            // 批量插入
            articleTagRelMapper.insertBatchSomeColumn(articleTagRelDOS);
        }
    }


}
