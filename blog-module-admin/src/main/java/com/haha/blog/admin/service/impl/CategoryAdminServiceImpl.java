package com.haha.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haha.blog.admin.service.ICategoryAdminService;
import com.haha.blog.common.domain.dos.ArticleCategoryRelDO;
import com.haha.blog.common.domain.dos.CategoryDO;

import com.haha.blog.admin.domain.dto.category.AddCategoryDTO;
import com.haha.blog.admin.domain.dto.category.DeleteCategoryDTO;
import com.haha.blog.admin.domain.query.category.CategoryPageQuery;
import com.haha.blog.admin.domain.vo.category.CategoryPageVO;
import com.haha.blog.admin.domain.vo.category.CategorySelectVO;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.ArticleCategoryRelMapper;
import com.haha.blog.common.mapper.CategoryMapper;

import com.haha.blog.common.utils.PageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryAdminServiceImpl extends ServiceImpl<CategoryMapper, CategoryDO> implements ICategoryAdminService {

    private final CategoryMapper categoryMapper;
    private final ArticleCategoryRelMapper articleCategoryRelMapper;

    @Override
    public void saveCategory(AddCategoryDTO dto) {
        String categoryName = dto.getName();
        CategoryDO categoryDO = lambdaQuery()
                .eq(CategoryDO::getName, categoryName)
                .one();
        if (Objects.nonNull(categoryDO)) {
            throw new BizException("分类已存在，请勿重复添加");
        }
        categoryDO = CategoryDO.builder()
                .name(categoryName)
                .build();
        save(categoryDO);
    }

    @Override
    public PageDTO<CategoryPageVO> queryCategoryPage(CategoryPageQuery query) {
        // 分页数据
        Long current = query.getCurrent();
        Long size = query.getSize();
        // 获取查询条件
        String categoryName = query.getName();
        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        // 分页查询
        Page<CategoryDO> page = lambdaQuery()
                .like(StringUtils.isNoneBlank(categoryName), CategoryDO::getName, categoryName)
                .ge(Objects.nonNull(startDateTime), CategoryDO::getCreateTime, startDateTime)
                .le(Objects.nonNull(endDateTime), CategoryDO::getCreateTime, endDateTime)
                .orderByDesc(CategoryDO::getCreateTime)
                .page(new Page<>(current, size));
        List<CategoryDO> records = page.getRecords();
        List<CategoryPageVO> vos = null;
        if (CollectionUtils.isEmpty(records)) {
            return PageDTO.success(page, Collections.emptyList());
        }
        // do转vo
        List<CategoryPageVO> list = records.stream()
                .map(categoryDO -> CategoryPageVO.builder()
                        .id(categoryDO.getId())
                        .name(categoryDO.getName())
                        .createTime(categoryDO.getCreateTime())
                        .articlesTotal(categoryDO.getArticlesTotal())
                        .build())
                .collect(Collectors.toList());
        return PageDTO.success(page,list);
    }

    @Override
    public void deleteCategory(DeleteCategoryDTO dto) {
        Long categoryId = dto.getId();
        // 校验是否有文章关联该分类
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectOne(
                Wrappers.<ArticleCategoryRelDO>lambdaQuery()
                        .eq(ArticleCategoryRelDO::getCategoryId, categoryId)
                        .last("limit 1")
        );
        if (Objects.nonNull(articleCategoryRelDO)) {
            throw new BizException("该分类已关联文章，无法删除");
        }
        int row = categoryMapper.deleteById(categoryId);
        if (row != 1) {
            throw new BizException("删除分类失败");
        }
    }

    @Override
    public List<CategorySelectVO> queryCategorySelect() {
        List<CategoryDO> categoryDOS = categoryMapper.selectList(null);
        if (CollectionUtils.isEmpty(categoryDOS)) {
            return Collections.emptyList();
        }
        List<CategorySelectVO> voList = categoryDOS.stream()
                .map(categoryDO -> CategorySelectVO.builder()
                        .label(categoryDO.getName())
                        .value(categoryDO.getId())
                        .build())
                .collect(Collectors.toList());
        return voList;
    }
}
