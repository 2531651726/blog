package com.haha.blog.admin.domain.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchTagDTO {

    @NotBlank(message = "标签查询关键字不能为空")
    private String key;
}
