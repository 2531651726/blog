package com.haha.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.common.domain.dos.CategoryDO;

import com.haha.blog.admin.domain.dto.category.AddCategoryDTO;
import com.haha.blog.admin.domain.dto.category.DeleteCategoryDTO;
import com.haha.blog.admin.domain.query.category.CategoryPageQuery;
import com.haha.blog.admin.domain.vo.category.CategoryPageVO;
import com.haha.blog.admin.domain.vo.category.CategorySelectVO;
import com.haha.blog.common.utils.PageDTO;

import java.util.List;

public interface ICategoryAdminService extends IService<CategoryDO> {
    void saveCategory(AddCategoryDTO dto);

    PageDTO<CategoryPageVO> queryCategoryPage(CategoryPageQuery query);

    void deleteCategory(DeleteCategoryDTO dto);

    List<CategorySelectVO> queryCategorySelect();
}
