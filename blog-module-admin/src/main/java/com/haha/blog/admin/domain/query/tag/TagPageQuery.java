package com.haha.blog.admin.domain.query.tag;

import com.haha.blog.common.utils.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagPageQuery extends BasePageQuery {

    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
}
