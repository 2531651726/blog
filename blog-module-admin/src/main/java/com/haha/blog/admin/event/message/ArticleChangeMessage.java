package com.haha.blog.admin.event.message;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 文章变更消息（新增 / 修改 / 删除）
 */
@Getter
public class ArticleChangeMessage extends ApplicationEvent {

    /** 文章ID */
    private final Long articleId;
    /** 变更类型 */
    private final ChangeType changeType;

    public ArticleChangeMessage(Object source, Long articleId, ChangeType changeType) {
        super(source);
        this.articleId = articleId;
        this.changeType = changeType;
    }

    public enum ChangeType {
        /** 新增 */
        CREATE,
        /** 修改 */
        UPDATE,
        /** 删除 */
        DELETE
    }
}
