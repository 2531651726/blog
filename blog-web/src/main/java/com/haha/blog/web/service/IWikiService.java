package com.haha.blog.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.common.domain.dos.WikiDO;
import com.haha.blog.web.domain.query.wiki.WikiArticlePreNextQuery;
import com.haha.blog.web.domain.query.wiki.WikiCatalogQuery;
import com.haha.blog.web.domain.vo.wiki.WikiArticlePreNextVO;
import com.haha.blog.web.domain.vo.wiki.WikiCatalogVO;
import com.haha.blog.web.domain.vo.wiki.WikiVO;

import java.util.List;

public interface IWikiService extends IService<WikiDO> {
    List<WikiVO> queryWikiPage();

    List<WikiCatalogVO> queryWikiCatalog(WikiCatalogQuery query);

    WikiArticlePreNextVO queryWikiArticlePreAndNext(WikiArticlePreNextQuery query);
}
