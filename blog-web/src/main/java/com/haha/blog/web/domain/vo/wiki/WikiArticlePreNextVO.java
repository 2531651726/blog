package com.haha.blog.web.domain.vo.wiki;

import com.haha.blog.web.domain.vo.article.PreOrNextArticleVO;
import lombok.Data;

@Data
public class WikiArticlePreNextVO {
    private PreOrNextArticleVO preArticle;
    private PreOrNextArticleVO nextArticle;
}
