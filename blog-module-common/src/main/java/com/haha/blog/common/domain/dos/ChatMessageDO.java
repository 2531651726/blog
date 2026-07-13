package com.haha.blog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 聊天室消息表
 * </p>
 *
 * @author li
 * @since 2026-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_chat_message")
public class ChatMessageDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送者昵称
     */
    private String nickname;

    /**
     * 发送者头像
     */
    private String avatar;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间
     */
    private LocalDateTime createTime;

    private String qq;


}
