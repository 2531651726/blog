package com.haha.blog.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haha.blog.common.domain.dos.ArticleDO;
import com.haha.blog.common.domain.dos.ArticleTagRelDO;
import com.haha.blog.common.domain.dos.CategoryDO;
import com.haha.blog.common.domain.dos.TagDO;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.ArticleMapper;
import com.haha.blog.common.mapper.ArticleTagRelMapper;
import com.haha.blog.common.mapper.TagMapper;
import com.haha.blog.common.utils.BeanUtils;
import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.query.tag.TagArticlePageQuery;
import com.haha.blog.web.domain.vo.category.CategoryVO;
import com.haha.blog.web.domain.vo.tag.TagArticleVO;
import com.haha.blog.web.domain.vo.tag.TagVO;
import com.haha.blog.web.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTML;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章标签表 服务实现类
 * </p>
 *
 * @author li
 * @since 2026-06-21
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, TagDO> implements ITagService {

    private final TagMapper tagMapper;
    private final ArticleTagRelMapper articleTagRelMapper;
    private final ArticleMapper articleMapper;

    @Override
    public List<TagVO> queryTagList() {
        List<TagDO> dos = lambdaQuery().list();
        List<TagVO> list = null;
        if(CollectionUtil.isNotEmpty(dos)){
            list = dos.stream()
                    .map(DO -> new TagVO()
                            .setId(DO.getId())
                            .setName(DO.getName())
                            .setArticlesTotal(DO.getArticlesTotal()))
                    .collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public PageDTO<TagArticleVO> queryArticleByTagPage(TagArticlePageQuery query) {
        Long current = query.getCurrent();
        Long size = query.getSize();
        Long tagId = query.getId();
        // 查询标签数据
        TagDO tagDO = lambdaQuery().eq(TagDO::getId, tagId).one();
        if(Objects.isNull(tagDO)){
            log.warn("==> 该标签不存在, tagId: {}", tagId);
            throw new BizException("该标签不存在");
        }
        // 查询标签下的文章
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByTagId(tagId);
        if(CollectionUtil.isEmpty(articleTagRelDOS)){
            log.info("==> 该标签下没有文章, tagId: {}", tagId);
            return PageDTO.success(null,null);
        }
        // 获取文章id集合
        Set<Long> tagIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getArticleId).collect(Collectors.toSet());
        // 根据文章id查询文章分页数据
        Page<ArticleDO> page = articleMapper.selectPageListByArticleIds(current, size, tagIds);
        List<ArticleDO> records = page.getRecords();
        if(CollectionUtil.isEmpty(records)){
            return PageDTO.success(page,Collections.emptyList());
        }
        // do转vo
        List<TagArticleVO> list = records.stream()
                .map(DO -> {
                    TagArticleVO vo = BeanUtils.copyBean(DO, TagArticleVO.class);
                    vo.setCreateDate(LocalDate.from(DO.getCreateTime()));
                    return vo;
                })
                .collect(Collectors.toList());
        return PageDTO.success(page,list);
    }
}
