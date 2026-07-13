package com.haha.blog.web.domain.vo.chatroom;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OnlineUserVO {
    private String nickname;
    private String avatar;
    private Boolean online;
}
