package com.haha.blog.admin.controller;

import com.haha.blog.common.aspect.ApiOperationLog;

import com.haha.blog.admin.domain.dto.category.AddCategoryDTO;
import com.haha.blog.admin.domain.dto.category.DeleteCategoryDTO;
import com.haha.blog.admin.domain.query.category.CategoryPageQuery;
import com.haha.blog.admin.domain.vo.category.CategoryPageVO;
import com.haha.blog.admin.domain.vo.category.CategorySelectVO;
import com.haha.blog.admin.service.ICategoryAdminService;
import com.haha.blog.common.utils.PageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Api(tags = "admin分类模块")
public class CategoryAdminController {

    private final ICategoryAdminService categoryService;

    @PostMapping("/category/add")
    @ApiOperation("添加分类")
    @ApiOperationLog(description = "添加分类")
    public void saveCategory(@Validated @RequestBody AddCategoryDTO dto) {
        categoryService.saveCategory(dto);
    }

    @PostMapping("/category/list")
    @ApiOperation("分页查询分类数据")
    @ApiOperationLog(description = "分页查询分类数据")
    public PageDTO<CategoryPageVO> queryCategoryPage(@Validated @RequestBody CategoryPageQuery query){
        return categoryService.queryCategoryPage(query);
    }

    @PostMapping("/category/delete")
    @ApiOperation("删除分类")
    @ApiOperationLog(description = "删除分类")
    public void deleteCategory(@Validated @RequestBody DeleteCategoryDTO dto) {
        categoryService.deleteCategory(dto);
    }

    @PostMapping("/category/select/list")
    @ApiOperation("查询分类Select下拉列表数据")
    @ApiOperationLog(description = "查询分类Select下拉列表数据")
    public List<CategorySelectVO> queryCategorySelect() {
        return categoryService.queryCategorySelect();
    }

}
