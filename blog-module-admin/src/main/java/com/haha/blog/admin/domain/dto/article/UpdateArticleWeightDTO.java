package com.haha.blog.admin.domain.dto.article;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "更新文章置顶状态 DTO")
public class UpdateArticleWeightDTO {
    @NotNull(message = "文章 ID 不能为空")
    private Long id;

    @NotNull(message = "文章置顶状态不能为空")
    private Boolean isTop;

    @NotNull(message = "文章权重不能为空")
    @Min(value = 0, message = "权重不能小于0")
    private Integer weight;
}
