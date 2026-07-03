package com.haha.blog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.haha.blog.common.domain.dos.*;
import com.haha.blog.common.enums.PublishStatus;
import com.haha.blog.common.enums.ResponseCodeEnum;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.*;
import com.haha.blog.common.service.ArticleViewCountService;
import com.haha.blog.common.utils.BeanUtils;
import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.query.article.ArticleDetailQuery;
import com.haha.blog.web.domain.query.article.ArticlePageQuery;
import com.haha.blog.web.domain.vo.article.ArticleDetailVO;
import com.haha.blog.web.domain.vo.article.ArticleVO;
import com.haha.blog.web.domain.vo.article.PreOrNextArticleVO;
import com.haha.blog.web.domain.vo.category.CategoryVO;
import com.haha.blog.web.domain.vo.tag.TagVO;
import com.haha.blog.web.markdown.MarkdownHelper;
import com.haha.blog.web.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haha.blog.web.utils.MarkdownStatsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author li
 * @since 2026-06-21
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleDO> implements IArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleContentMapper articleContentMapper;
    private final CategoryMapper categoryMapper;
    private final ArticleCategoryRelMapper articleCategoryRelMapper;
    private final TagMapper tagMapper;
    private final ArticleTagRelMapper articleTagRelMapper;
    private final ArticleViewCountService articleViewCountService;

    @Override
    public PageDTO<ArticleVO> queryArticlePage(ArticlePageQuery query) {
        Long current = query.getCurrent();
        Long size = query.getSize();
        // 分页查询文章
        Page<ArticleDO> articleDOPage = lambdaQuery()
                .eq(ArticleDO::getIsPublish, PublishStatus.PUBLISHED)
                .orderByDesc(ArticleDO::getWeight) // 按权重倒序
                .orderByDesc(ArticleDO::getCreateTime)
                .page(new Page<>(current, size));
        List<ArticleDO> articleDOS = articleDOPage.getRecords();
        List<ArticleVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            // 文章 DO 转 VO
            vos = articleDOS.stream()
                    .map(articleDO -> {
                        ArticleVO articleVO = BeanUtils.copyBean(articleDO, ArticleVO.class);
                        articleVO.setCreateDate(articleDO.getCreateTime().toLocalDate());
                        articleVO.setIsTop(articleDO.getWeight() > 0);
                        return articleVO;
                    })
                    .collect(Collectors.toList());
            // 拿到所有文章的 ID 集合
            List<Long> articleIds = articleDOS.stream().map(ArticleDO::getId).collect(Collectors.toList());
            // 设置文章所属分类
            // 查询所有分类
            List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.emptyWrapper());
            // 转 Map, 方便后续根据分类 ID 拿到对应的分类名称
            Map<Long, String> categoryIdNameMap = categoryDOS.stream().collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));
            // 根据文章 ID 批量查询所有关联记录
            List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper.selectList(
                    Wrappers.<ArticleCategoryRelDO>lambdaQuery().in(ArticleCategoryRelDO::getArticleId,articleIds)
            );
            vos.forEach(vo -> {
                Long currArticleId = vo.getId();
                // 过滤出当前文章对应的关联数据
                Optional<ArticleCategoryRelDO> optional = articleCategoryRelDOS.stream().filter(rel -> Objects.equals(rel.getArticleId(), currArticleId)).findAny();
                // 若不为空
                if (optional.isPresent()) {
                    ArticleCategoryRelDO articleCategoryRelDO = optional.get();
                    Long categoryId = articleCategoryRelDO.getCategoryId();
                    // 通过分类 ID 从 map 中拿到对应的分类名称
                    String categoryName = categoryIdNameMap.get(categoryId);
                    CategoryVO categoryVO = new CategoryVO()
                            .setId(categoryId)
                            .setName(categoryName);
                    // 设置到当前 vo 类中
                    vo.setCategory(categoryVO);
                }
            });
            // 设置文章标签
            // 查询所有标签
            List<TagDO> tagDOS = tagMapper.selectList(Wrappers.emptyWrapper());
            // 转 Map, 方便后续根据标签 ID 拿到对应的标签名称
            Map<Long, String> mapIdNameMap = tagDOS.stream().collect(Collectors.toMap(TagDO::getId, TagDO::getName));
            // 拿到所有文章的标签关联记录
            List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectList(
                    Wrappers.<ArticleTagRelDO>lambdaQuery().in(ArticleTagRelDO::getArticleId,articleIds));
            vos.forEach(vo -> {
                Long currArticleId = vo.getId();
                // 过滤出当前文章的标签关联记录
                List<ArticleTagRelDO> articleTagRelDOList = articleTagRelDOS.stream().filter(rel -> Objects.equals(rel.getArticleId(), currArticleId)).collect(Collectors.toList());
                List<TagVO> TagVOS = Lists.newArrayList();
                // 将关联记录 DO 转 VO, 并设置对应的标签名称
                articleTagRelDOList.forEach(articleTagRelDO -> {
                    Long tagId = articleTagRelDO.getTagId();
                    String tagName = mapIdNameMap.get(tagId);
                    TagVO findTagListRspVO = new TagVO()
                            .setId(tagId)
                            .setName(tagName);
                    TagVOS.add(findTagListRspVO);
                });
                // 设置转换后的标签数据
                vo.setTags(TagVOS);
            });
        }
        return PageDTO.success(articleDOPage,vos);
    }

    @Override
    public ArticleDetailVO queryArticleDetail(ArticleDetailQuery query) {
        Long articleId = query.getArticleId();
        // 文章
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if (Objects.isNull(articleDO)) {
            log.warn("==> 该文章不存在, articleId: {}", articleId);
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_FOUND);
        }
        ArticleDetailVO articleDetailVO = BeanUtils.copyBean(articleDO, ArticleDetailVO.class);

        // 正文
        ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleId);
        if(Objects.isNull(articleContentDO)) {
            log.warn("==> 该文章正文不存在, articleId: {}", articleId);
            throw new BizException("该文章正文不存在");
        }
        // 将markdown转html
        articleDetailVO.setContent(MarkdownHelper.convertMarkdownToHtml(articleContentDO.getContent()));

        // 计算总字数和阅读时长
        Integer totalWords = MarkdownStatsUtil.calculateWordCount(articleContentDO.getContent());
        String readTime = MarkdownStatsUtil.calculateReadingTime(totalWords);
        articleDetailVO.setTotalWords(totalWords);
        articleDetailVO.setReadTime(readTime);

        // 标签
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByArticleId(articleId);
        if(CollectionUtils.isEmpty(articleTagRelDOS)) {
            log.warn("==> 该文章缺少标签, articleId: {}", articleId);
            throw new BizException("该文章缺少标签");
        }
        List<Long> tagIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getTagId).collect(Collectors.toList());
        List<TagDO> tagDOS = tagMapper.selectBatchIds(tagIds);
        if(CollectionUtils.isEmpty(tagDOS)) {
            throw new BizException("标签不存在");
        }
        List<TagVO> tagVOS = BeanUtils.copyToList(tagDOS, TagVO.class);
        articleDetailVO.setTags(tagVOS);

        // 分类
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectByArticleId(articleId);
        if(Objects.isNull(articleCategoryRelDO)) {
            log.warn("==> 该文章缺少分类, articleId: {}", articleId);
            throw new BizException("该文章缺少分类");
        }
        CategoryDO categoryDO = categoryMapper.selectById(articleCategoryRelDO.getCategoryId());
        if(Objects.isNull(categoryDO)) {
            throw new BizException("该分类不存在");
        }
        articleDetailVO.setCategoryId(categoryDO.getId());
        articleDetailVO.setCategoryName(categoryDO.getName());

        // 前一篇文章
        ArticleDO articlePre = articleMapper.selectPreArticle(articleId);
        if(Objects.nonNull(articlePre)) {
            PreOrNextArticleVO preArticleVO = new PreOrNextArticleVO()
                    .setArticleId(articlePre.getId())
                    .setArticleTitle(articlePre.getTitle());
            articleDetailVO.setPreArticle(preArticleVO);
        }

        // 后一篇文章
        ArticleDO articleNext = articleMapper.selectNextArticle(articleId);
        if(Objects.nonNull(articleNext)) {
            PreOrNextArticleVO nextArticleVO = new PreOrNextArticleVO()
                    .setArticleId(articleNext.getId())
                    .setArticleTitle(articleNext.getTitle());
            articleDetailVO.setNextArticle(nextArticleVO);
        }

        // 浏览量+1，缓存到redis
        try {
            articleViewCountService.incrementViewCount(articleId);
        } catch (Exception e) {
            log.warn("==> 浏览量自增失败, articleId: {}", articleId, e);
        }
        return articleDetailVO;
    }
}
