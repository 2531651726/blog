package com.haha.blog.web.websocket;

import com.haha.blog.common.domain.dos.ChatMessageDO;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.ChatMessageMapper;
import com.haha.blog.common.utils.JsonUtil;
import com.haha.blog.web.domain.dto.chatroom.UserInfoDTO;
import com.haha.blog.web.domain.vo.chatroom.OnlineUserVO;
import com.haha.blog.web.domain.vo.chatroom.OnlineUsersMessageVO;
import com.haha.blog.web.domain.vo.chatroom.WebSocketChatMessageVO;
import com.haha.blog.web.enums.ChatMessageType;
import com.haha.blog.web.utils.SpringContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@ServerEndpoint("/ws/chat")
@RequiredArgsConstructor
public class ChatWebSocketServer {

    private static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    private static final Map<String, String> SESSION_ID_TO_KEY_MAP = new ConcurrentHashMap<>();
    private static final String USER_INFO = "user_info";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @OnOpen
    public void onOpen(Session session) {
        String sessionId = session.getId();
        // 获取昵称、头像
        Map<String, List<String>> params = session.getRequestParameterMap();
        String nickname = getFieldValueFromParam("nickname", params);
        String avatar = getFieldValueFromParam("avatar", params);
        String qq = getFieldValueFromParam("qq", params);
        // 如果是以 qq 号方式加入的聊天室，则以 qq 号作为会话 Key; 否则，以 sessionId 作为 Key
        String sessionKey = StringUtils.isNotBlank(qq) ? qq : sessionId;
        // 保存 session.getId() 到 sessionKey 的映射关系
        SESSION_ID_TO_KEY_MAP.put(sessionId, sessionKey);
        // 保存会话
        SESSION_MAP.put(sessionKey, session);
        // 将用户信息放入 session
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setNickname(nickname);
        userInfoDTO.setAvatar(avatar);
        userInfoDTO.setQq(qq);
        session.getUserProperties().put(USER_INFO, userInfoDTO);
        // 在线人数 +1
        int onlineCount = ONLINE_COUNT.incrementAndGet();
        log.info("## 用户 [sessionKey:{}] 加入了聊天室，当前在线人数: {}", sessionKey, onlineCount);
        // 返回自己的 sessionKey 给前端（只发送给当前会话）
        sendMessage(session, buildSessionIdMessage(sessionKey));
        // 广播系统消息,告诉所有人,有用户加入了聊天室
        broadcastMessage(buildMessage(ChatMessageType.SYSTEM, nickname, avatar, "加入了聊天室", null));
        // 广播在线用户列表
        broadcastOnlineUsers();
    }

    @OnClose
    public void onClose(Session session) {
        String sessionId = session.getId();
        String sessionKey = SESSION_ID_TO_KEY_MAP.remove(sessionId);
        if (StringUtils.isBlank(sessionKey)) {
            log.warn("## 未找到对应的 sessionKey，[session.getId:{}]", session.getId());
            return;
        }
        if (SESSION_MAP.containsKey(sessionKey)) {
            // 获取用户信息
            UserInfoDTO userInfo = (UserInfoDTO)session.getUserProperties().get(USER_INFO);
            // 校验用户信息是否存在
            if (Objects.isNull(userInfo)) {
                log.warn("## 用户信息不存在，[sessionKey:{}]", sessionKey);
                return;
            }
            // 删除对话
            SESSION_MAP.remove(sessionKey);
            // 在线人数 -1
            int onlineCount = ONLINE_COUNT.decrementAndGet();
            log.info("## 用户 [sessionKey:{}] 离开了聊天室，当前在线人数: {}", sessionKey, onlineCount);
            // 广播系统消息
            broadcastMessage(buildMessage(ChatMessageType.SYSTEM, userInfo.getNickname(), userInfo.getAvatar(), "加入了聊天室", null));
            // 广播在线用户列表
            broadcastOnlineUsers();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        String sessionId = session.getId();
        String sessionKey = SESSION_ID_TO_KEY_MAP.get(sessionId);
        if (StringUtils.isBlank(sessionKey)) {
            log.warn("## 未找到对应的 sessionKey，[sessionId:{}]", session.getId());
            return;
        }
        UserInfoDTO userInfo = (UserInfoDTO)session.getUserProperties().get(USER_INFO);
        // 校验用户信息是否存在
        if (Objects.isNull(userInfo)) {
            log.warn("## 用户信息不存在，[sessionId:{}]", sessionId);
            return;
        }
        log.info("## 收到用户 [sessionKey:{}] 的消息: {}", sessionKey, message);
        // 持久化聊天记录
        saveMessage(userInfo.getNickname(), userInfo.getAvatar(), userInfo.getQq(), message);
        // 广播聊天信息
        broadcastMessage(ChatMessageType.CHAT, userInfo.getNickname(), userInfo.getAvatar(), message, sessionKey);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("## WebSocket 连接发生错误：", error);
        onClose(session);
    }

    private void sendMessage(Session session, String message) {
        if (session == null || !session.isOpen()) {
            log.warn("## 连接已关闭，无需推送消息");
            return;
        }
        try{
            session.getBasicRemote().sendText(message);
        }catch (Exception e){
            log.error("## 发送消息失败：", e);
        }
    }

    private void broadcastMessage(String message) {
        SESSION_MAP.values().forEach(session -> {
            if (session != null && session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    log.error("## 广播单条消息发送失败", e);
                }
            }
        });
    }

    /**
     * 广播聊天消息给所有在线用户
     * 给发送者返回带 sessionKey 的消息，给其他用户返回不带 sessionKey 的消息
     */
    private void broadcastMessage(ChatMessageType type, String nickname, String avatar, String content, String senderSessionKey) {
        // 构建不带 sessionKey 的消息（发送给其他用户）
        String messageWithoutSessionKey = JsonUtil.toJsonString(new WebSocketChatMessageVO()
                .setType(type)
                .setNickname(nickname)
                .setAvatar(avatar)
                .setContent(content)
                .setSessionId(null)
                .setTime(LocalDateTime.now().format(TIME_FORMATTER))
                .setOnlineCount(ONLINE_COUNT.get())
        );
        // 构建带 sessionKey 的消息（发送给发送者自己）
        String messageWithSessionKey = JsonUtil.toJsonString(new WebSocketChatMessageVO()
                .setType(type)
                .setNickname(nickname)
                .setAvatar(avatar)
                .setContent(content)
                .setSessionId(senderSessionKey)
                .setTime(LocalDateTime.now().format(TIME_FORMATTER))
                .setOnlineCount(ONLINE_COUNT.get())
        );
        SESSION_MAP.forEach((sessionKey, session) -> {
            // 如果当前会话是发送者，则发送带 sessionKey 的消息；否则发送不带 sessionKey 的消息
            if (Objects.equals(sessionKey, senderSessionKey)) {
                sendMessage(session, messageWithSessionKey);
            } else {
                sendMessage(session, messageWithoutSessionKey);
            }
        });
    }

    private String getFieldValueFromParam(String fieldName, Map<String, List<String>> params) {
        // 如果包含对应字段，且不为空，则取出
        if (params.containsKey(fieldName)
                && !CollectionUtils.isEmpty(params.get(fieldName))) {
            return params.get(fieldName).get(0);
        }
        return null;
    }

    private String buildMessage(ChatMessageType type, String nickname, String avatar, String content, String sessionKey) {
        WebSocketChatMessageVO chatMessageVO = new WebSocketChatMessageVO()
                .setType(type)
                .setNickname(nickname)
                .setAvatar(avatar)
                .setContent(content)
                .setTime(LocalDateTime.now().format(TIME_FORMATTER))
                .setOnlineCount(ONLINE_COUNT.get());
        if (Objects.nonNull(sessionKey)) {
            chatMessageVO.setSessionId(sessionKey);
        }
        return JsonUtil.toJsonString(chatMessageVO);
    }

    private void saveMessage(String nickname, String avatar, String qq, String content) {
        ChatMessageDO chatMessage = new ChatMessageDO()
                .setNickname(nickname)
                .setAvatar(avatar)
                .setContent(content)
                .setQq(qq);
        ChatMessageMapper chatMessageMapper = SpringContext.getBean(ChatMessageMapper.class);
        int row = chatMessageMapper.insert(chatMessage);
        if (row != 1) {
            throw new BizException("聊天记录新增失败");
        }
    }

    private void broadcastOnlineUsers() {
        // 构建在线用户列表 VO
        OnlineUsersMessageVO msg = new OnlineUsersMessageVO()
                .setType(ChatMessageType.ONLINE_USERS)
                .setOnlineCount(ONLINE_COUNT.get())
                .setUsers(getOnlineUsers());
        // 广播消息
        broadcastMessage(JsonUtil.toJsonString(msg));
    }

    public static List<OnlineUserVO> getOnlineUsers() {
        List<OnlineUserVO> users = new ArrayList<>();
        SESSION_MAP.forEach((sessionId, session) ->{
            // 获取用户信息
            UserInfoDTO userInfo = (UserInfoDTO)session.getUserProperties().get(USER_INFO);
            users.add(new OnlineUserVO()
                    .setNickname(userInfo.getNickname())
                    .setAvatar(userInfo.getAvatar())
                    .setOnline(true));
        });
        return users;
    }

    private String buildSessionIdMessage(String sessionKey) {
        return JsonUtil.toJsonString(new WebSocketChatMessageVO()
                .setType(ChatMessageType.INIT)
                .setSessionId(sessionKey));
    }
}
