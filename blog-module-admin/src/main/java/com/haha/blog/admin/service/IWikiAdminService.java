package com.haha.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.admin.domain.dto.wiki.*;
import com.haha.blog.admin.domain.query.wiki.WikiCatalogListQuery;
import com.haha.blog.admin.domain.query.wiki.WikiPageListQuery;
import com.haha.blog.admin.domain.vo.wiki.WikiCatalogListVO;
import com.haha.blog.admin.domain.vo.wiki.WikiPageListVO;
import com.haha.blog.common.domain.dos.WikiDO;
import com.haha.blog.common.utils.PageDTO;

import java.util.List;

public interface IWikiAdminService extends IService<WikiDO> {
    void insertWiki(InsertWikiDTO dto);

    void deleteWiki(DeleteWikiDTO dto);

    PageDTO<WikiPageListVO> queryWikiPageList(WikiPageListQuery query);

    void updateWikiWeight(UpdateWikiWeightDTO dto);

    void updateWikiPublishStatus(UpdateWikiPublishStatusDTO dto);

    void updateWiki(UpdateWikiDTO dto);

    List<WikiCatalogListVO> queryWikiCatalogList(WikiCatalogListQuery query);

    void updateWikiCatalogs(UpdateWikiCatalogDTO dto);
}
