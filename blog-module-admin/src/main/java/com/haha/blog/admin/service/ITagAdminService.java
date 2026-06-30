package com.haha.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.common.domain.dos.TagDO;
import com.haha.blog.admin.domain.dto.tag.AddTagDTO;
import com.haha.blog.admin.domain.dto.tag.DeleteTagDTO;
import com.haha.blog.admin.domain.dto.tag.SearchTagDTO;
import com.haha.blog.admin.domain.query.tag.TagPageQuery;
import com.haha.blog.common.domain.vo.QuerySelectListVO;
import com.haha.blog.admin.domain.vo.Tag.TagPageListVO;
import com.haha.blog.common.utils.PageDTO;

import java.util.List;

public interface ITagAdminService extends IService<TagDO> {
    void addTags(AddTagDTO dto);

    PageDTO<TagPageListVO> queryTagPageList(TagPageQuery query);

    void deleteTagById(DeleteTagDTO dto);

    List<QuerySelectListVO> searchTags(SearchTagDTO dto);

    List<QuerySelectListVO> queryTagSelectList();
}
