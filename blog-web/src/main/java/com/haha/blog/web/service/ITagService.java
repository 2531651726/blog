package com.haha.blog.web.service;

import com.haha.blog.common.domain.dos.TagDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.query.tag.TagArticlePageQuery;
import com.haha.blog.web.domain.vo.tag.TagArticleVO;
import com.haha.blog.web.domain.vo.tag.TagVO;

import java.util.List;

/**
 * <p>
 * 文章标签表 服务类
 * </p>
 *
 * @author li
 * @since 2026-06-21
 */
public interface ITagService extends IService<TagDO> {

    List<TagVO> queryTagList();

    PageDTO<TagArticleVO> queryArticleByTagPage(TagArticlePageQuery query);
}
