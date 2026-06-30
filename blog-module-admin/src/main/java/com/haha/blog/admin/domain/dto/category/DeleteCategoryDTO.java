package com.haha.blog.admin.domain.dto.category;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "删除分类 DTO")
public class DeleteCategoryDTO {
    @NotNull(message = "分类 ID 不能为空")
    private Long id;
}
