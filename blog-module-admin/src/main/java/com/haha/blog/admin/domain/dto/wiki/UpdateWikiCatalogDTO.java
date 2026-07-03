package com.haha.blog.admin.domain.dto.wiki;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateWikiCatalogDTO {
    /**
     * 知识库 ID
     */
    @NotNull(message = "知识库 ID 不能为空")
    private Long id;
    /**
     * 目录
     */
    @Valid
    private List<UpdateWikiCatalogItemDTO> catalogs;
}
