package com.haha.blog.common.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.haha.blog.common.utils.InsertBatchMapper;
import com.haha.blog.common.domain.dos.ArticleTagRelDO;
import com.haha.blog.common.domain.dto.tag.TagAndArticlesTotalDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 文章对应标签关联表 Mapper 接口
 * </p>
 *
 * @author li
 * @since 2026-06-20
 */
@Mapper
public interface ArticleTagRelMapper extends InsertBatchMapper<ArticleTagRelDO> {

    /**
     * 查询该标签 ID 下所有关联记录
     * @param tagId
     * @return
     */
    default List<ArticleTagRelDO> selectByTagId(Long tagId) {
        return selectList(Wrappers.<ArticleTagRelDO>lambdaQuery()
                .eq(ArticleTagRelDO::getTagId, tagId));
    }

    default List<ArticleTagRelDO> selectByArticleId(Long articleId){
        return selectList(Wrappers.<ArticleTagRelDO>lambdaQuery()
                .eq(ArticleTagRelDO::getArticleId, articleId));
    }

    List<TagAndArticlesTotalDTO> selectCountGroupByTagId();
}
