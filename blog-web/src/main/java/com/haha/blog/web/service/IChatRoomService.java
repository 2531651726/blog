package com.haha.blog.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.common.domain.dos.ChatMessageDO;
import com.haha.blog.web.domain.query.chatroom.ChatMessagePageListQuery;
import com.haha.blog.web.domain.vo.chatroom.ChatMessagePageListVO;
import com.haha.blog.web.domain.vo.chatroom.OnlineUserVO;

import java.util.List;

public interface IChatRoomService extends IService<ChatMessageDO> {
    ChatMessagePageListVO queryHistoryMessages(ChatMessagePageListQuery query);

    List<OnlineUserVO> queryOnlineUsers();
}
