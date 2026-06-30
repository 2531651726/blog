package com.haha.blog.admin.domain.vo.Tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagPageListVO {

    private Long id;
    private String name;
    private Integer articlesTotal;
    private LocalDateTime createTime;
}
