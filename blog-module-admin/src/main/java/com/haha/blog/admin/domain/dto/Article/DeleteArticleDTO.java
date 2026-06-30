package com.haha.blog.admin.domain.dto.Article;

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
@ApiModel(value = "删除文章 DTO")
public class DeleteArticleDTO {
    @NotNull(message = "文章 ID 不能为空")
    private Long id;
}
