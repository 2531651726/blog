package com.haha.blog.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haha.blog.common.domain.dos.ChatMessageDO;
import com.haha.blog.common.mapper.ChatMessageMapper;
import com.haha.blog.common.utils.BeanUtils;
import com.haha.blog.web.domain.query.chatroom.ChatMessagePageListQuery;
import com.haha.blog.web.domain.vo.chatroom.ChatMessagePageListVO;
import com.haha.blog.web.domain.vo.chatroom.ChatMessageVO;
import com.haha.blog.web.domain.vo.chatroom.OnlineUserVO;
import com.haha.blog.web.enums.ChatMessageType;
import com.haha.blog.web.service.IChatRoomService;
import com.haha.blog.web.utils.DateTimeFormatUtil;
import com.haha.blog.web.websocket.ChatWebSocketServer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessageDO> implements IChatRoomService {

    private static final int PAGE_SIZE = 10;

    private final ChatMessageMapper chatMessageMapper;

    @Override
    public ChatMessagePageListVO queryHistoryMessages(ChatMessagePageListQuery query) {
        Long lastId = query.getLastId();
        String sessionId = query.getSessionId();
        // 查询指定页码记录
        List<ChatMessageDO> messages = Objects.isNull(lastId)
                ? chatMessageMapper.selectRecentMessages(PAGE_SIZE) // 如果 lastId 为 null, 说明查询的是第一页消息
                : chatMessageMapper.selectMessagesBefore(lastId, PAGE_SIZE); // 若 lastId 不为 null, 则执行分页查询
        // 封装 VO
        ChatMessagePageListVO vo = null;
        if(!CollectionUtil.isEmpty(messages)){
            // 倒序变正序
            Collections.reverse(messages);
            List<ChatMessageVO> list = messages.stream()
                    .map(chatMessageDO -> {
                        // 判断是否是当前用户发送的消息
                        boolean isSelf = StringUtils.isNotBlank(chatMessageDO.getQq())
                                && StringUtils.isNotBlank(sessionId)
                                && Objects.equals(sessionId, chatMessageDO.getQq());
                        return new ChatMessageVO()
                                .setId(chatMessageDO.getId())
                                .setNickname(chatMessageDO.getNickname())
                                .setAvatar(chatMessageDO.getAvatar())
                                .setContent(chatMessageDO.getContent())
                                .setTime(DateTimeFormatUtil.formatChatTime(chatMessageDO.getCreateTime()))
                                .setType(ChatMessageType.CHAT)
                                .setIsSelf(isSelf);
                    })
                    .collect(Collectors.toList());
            vo = new ChatMessagePageListVO()
                    .setMessages(list)
                    .setHasMore(messages.size() >= PAGE_SIZE);
        }
        return vo;
    }

    @Override
    public List<OnlineUserVO> queryOnlineUsers() {
        List<OnlineUserVO> list = ChatWebSocketServer.getOnlineUsers();
        if(CollectionUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        return list;
    }
}
