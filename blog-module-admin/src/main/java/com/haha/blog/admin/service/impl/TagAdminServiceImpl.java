package com.haha.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haha.blog.admin.service.ITagAdminService;
import com.haha.blog.common.domain.dos.ArticleTagRelDO;
import com.haha.blog.common.domain.dos.TagDO;
import com.haha.blog.admin.domain.dto.tag.AddTagDTO;
import com.haha.blog.admin.domain.dto.tag.DeleteTagDTO;
import com.haha.blog.admin.domain.dto.tag.SearchTagDTO;
import com.haha.blog.admin.domain.query.tag.TagPageQuery;
import com.haha.blog.common.domain.vo.QuerySelectListVO;
import com.haha.blog.admin.domain.vo.Tag.TagPageListVO;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.ArticleTagRelMapper;
import com.haha.blog.common.mapper.TagMapper;

import com.haha.blog.common.utils.PageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagAdminServiceImpl extends ServiceImpl<TagMapper, TagDO> implements ITagAdminService {

    private final TagMapper tagMapper;
    private final ArticleTagRelMapper articleTagRelMapper;

    @Override
    public void addTags(AddTagDTO dto) {
        List<String> tagNameList = dto.getTags();
        // 1.先对参数去重
        Set<String> tagNameSet = new HashSet<>(tagNameList);
        // 2. 查数据库中已存在的 tag
        List<TagDO> existsTags = lambdaQuery()
                .in(TagDO::getName, tagNameSet)
                .list();
        // 3. 过滤：只保留数据库中不存在的
        Set<String> existsNames = existsTags.stream()
                .map(TagDO::getName)
                .collect(Collectors.toSet());
        List<String> newTags = tagNameSet.stream()
                .filter(name -> !existsNames.contains(name))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(newTags)){
            return;
        }
        List<TagDO> tags = newTags.stream()
                .map(tagName -> TagDO.builder()
                        .name(tagName)
                        .build())
                .collect(Collectors.toList());
        saveBatch(tags);
    }

    @Override
    public PageDTO<TagPageListVO> queryTagPageList(TagPageQuery query) {
        // 分页数据
        Long current = query.getCurrent();
        Long size = query.getSize();
        // 获取查询条件
        String tagName = query.getName();
        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        // 分页查询
        Page<TagDO> page = lambdaQuery()
                .like(StringUtils.isNoneBlank(tagName), TagDO::getName, tagName)
                .ge(Objects.nonNull(startDateTime), TagDO::getCreateTime, startDateTime)
                .le(Objects.nonNull(endDateTime), TagDO::getCreateTime, endDateTime)
                .orderByDesc(TagDO::getCreateTime)
                .page(new Page<>(current, size));
        List<TagDO> list = page.getRecords();
        if(CollectionUtils.isEmpty(list)){
            return PageDTO.success(page, Collections.emptyList());
        }
        // do转vo
        List<TagPageListVO> voList = list.stream()
                .map(TagDO -> TagPageListVO.builder()
                        .id(TagDO.getId())
                        .name(TagDO.getName())
                        .articlesTotal(TagDO.getArticlesTotal())
                        .createTime(TagDO.getCreateTime())
                        .build())
                .collect(Collectors.toList());
        return PageDTO.success(page,voList);
    }

    @Override
    public void deleteTagById(DeleteTagDTO dto) {
        Long tagId = dto.getId();
        // 校验当前标签是否关联文章
        ArticleTagRelDO articleTagRelDO = articleTagRelMapper.selectOne(
                Wrappers.<ArticleTagRelDO>lambdaQuery()
                        .eq(ArticleTagRelDO::getTagId, tagId)
                        .last("LIMIT 1")
        );
        if(Objects.nonNull(articleTagRelDO)){
            throw new BizException("该标签已关联文章，无法删除");
        }
        int row = baseMapper.deleteById(tagId);
        if(row != 1){
            throw new BizException("该标签不存在，删除失败");
        }
    }

    @Override
    public List<QuerySelectListVO> searchTags(SearchTagDTO dto) {
        String key = dto.getKey();
        List<TagDO> tagList = lambdaQuery()
                .apply(StringUtils.isNotBlank(key),
                        "LOWER(name) LIKE LOWER(CONCAT('%', {0}, '%'))", key)
                .orderByDesc(TagDO::getCreateTime)
                .list();
        if(CollectionUtils.isEmpty(tagList)){
            return Collections.emptyList();
        }
        List<QuerySelectListVO> voList = tagList.stream()
                .map(tag -> QuerySelectListVO.builder()
                        .label(tag.getName())
                        .value(tag.getId())
                        .build())
                .collect(Collectors.toList());
        return voList;
    }

    @Override
    public List<QuerySelectListVO> queryTagSelectList() {
        List<TagDO> tagDOS = tagMapper.selectList(null);
        if(CollectionUtils.isEmpty(tagDOS)){
            return Collections.emptyList();
        }
        List<QuerySelectListVO> voList = tagDOS.stream()
                .map(tag -> QuerySelectListVO.builder()
                        .label(tag.getName())
                        .value(tag.getId())
                        .build())
                .collect(Collectors.toList());
        return voList;
    }
}
