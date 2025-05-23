package com.ssafy.stella_trip.chat.controller;

import com.ssafy.stella_trip.chat.dto.response.ChatListResponseDTO;
import com.ssafy.stella_trip.chat.dto.response.RoomListResponseDTO;
import com.ssafy.stella_trip.chat.service.ChatRoomService;
import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.dao.chat.ChatDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
@RequestMapping("/v1/chat/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/list")
    CommonResponse<RoomListResponseDTO> getRooms() {
        return new CommonResponse<>(chatRoomService.getRooms(), HttpStatus.OK);
    }

    @GetMapping("/{room_id}")
    CommonResponse<ChatListResponseDTO> getChatListByRoomId(
            @PathVariable("room_id") int roomId,
            @RequestParam(value = "lastChatId", defaultValue = "-1") int lastChatId,
            @RequestParam("size") int size) {
        return new CommonResponse<>(chatRoomService.getChatList(size, roomId, lastChatId), HttpStatus.OK);
    }
}
