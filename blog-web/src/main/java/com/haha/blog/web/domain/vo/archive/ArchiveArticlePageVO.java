package com.haha.blog.web.domain.vo.archive;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.YearMonth;
import java.util.List;

@Data
@Accessors(chain = true)
public class ArchiveArticlePageVO {

    private YearMonth month;
    private List<ArchiveArticleVO> articles;
}
