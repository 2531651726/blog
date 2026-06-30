package com.haha.blog.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haha.blog.common.domain.dos.CategoryDO;
import com.haha.blog.common.domain.dto.category.CategoryAndArticlesTotalDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<CategoryDO> {

    // 批量更新分类下的文章数量
    int batchUpdateArticlesTotalCount(@Param("list") List<CategoryAndArticlesTotalDTO> list);


}
