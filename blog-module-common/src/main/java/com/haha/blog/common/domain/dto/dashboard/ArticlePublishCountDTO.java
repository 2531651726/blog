package com.haha.blog.common.domain.dto.dashboard;


import lombok.Data;

import java.time.LocalDate;

@Data
public class ArticlePublishCountDTO {

    private LocalDate date;
    private Long count;
}
