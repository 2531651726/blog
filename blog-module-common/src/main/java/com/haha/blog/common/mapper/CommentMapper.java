package com.haha.blog.common.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.haha.blog.common.domain.dos.CommentDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haha.blog.common.enums.CommentStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author li
 * @since 2026-07-04
 */
@Mapper
public interface CommentMapper extends BaseMapper<CommentDO> {
    /**
     * 根据路由地址、状态查询对应的评论
     * @param routerUrl
     * @return
     */
    default List<CommentDO> selectByRouterUrlAndStatus(String routerUrl, CommentStatus status) {
        return selectList(Wrappers.<CommentDO>lambdaQuery()
                .eq(CommentDO::getRouterUrl, routerUrl) // 按路由地址查询
                .eq(CommentDO::getStatus, status) // 按状态查询
                .orderByDesc(CommentDO::getCreateTime) // 按创建时间倒序
        );
    }

    @Select("SELECT id FROM t_comment WHERE reply_comment_id = #{id}")
    List<Long> selectIdsByReplyCommentId(@Param("id") Long id);
}
