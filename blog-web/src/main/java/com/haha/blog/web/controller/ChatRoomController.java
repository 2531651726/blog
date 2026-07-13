package com.haha.blog.web.controller;

import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.web.domain.query.chatroom.ChatMessagePageListQuery;
import com.haha.blog.web.domain.vo.chatroom.ChatMessagePageListVO;
import com.haha.blog.web.domain.vo.chatroom.OnlineUserVO;
import com.haha.blog.web.service.IChatRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
@Api(tags = "聊天室")
@RequiredArgsConstructor
public class ChatRoomController {

    private final IChatRoomService chatRoomService;

    @PostMapping("/message/history")
    @ApiOperation(value = "获取历史消息")
    @ApiOperationLog(description = "获取历史消息")
    public ChatMessagePageListVO queryHistoryMessages(@RequestBody @Validated ChatMessagePageListQuery query) {
        return chatRoomService.queryHistoryMessages(query);
    }

    @PostMapping("/online/users")
    @ApiOperation(value = "获取所有在线用户")
    @ApiOperationLog(description = "获取所有在线用户")
    public List<OnlineUserVO> queryOnlineUsers() {
        return chatRoomService.queryOnlineUsers();
    }
}
