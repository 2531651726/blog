package com.haha.blog.admin.domain.dto.wiki;

import com.haha.blog.common.enums.WikiCatalogLevel;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateWikiCatalogItemDTO {

    @NotNull(message = "目录 ID 不能为空")
    private Long id;
    private Long articleId;
    @NotBlank(message = "目录标题不能为空")
    private String title;
    private Integer sort;
    private WikiCatalogLevel level;

    /**
     * 子目录
     */
    @Valid
    private List<UpdateWikiCatalogItemDTO> children;
}
