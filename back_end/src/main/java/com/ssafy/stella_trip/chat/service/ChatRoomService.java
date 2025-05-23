package com.ssafy.stella_trip.chat.service;

import com.ssafy.stella_trip.chat.dto.ChatDTO;
import com.ssafy.stella_trip.chat.dto.ChatRoomDTO;
import com.ssafy.stella_trip.chat.dto.response.ChatListResponseDTO;
import com.ssafy.stella_trip.chat.dto.response.ChatResponseDTO;
import com.ssafy.stella_trip.chat.dto.response.ChatRoomResponseDTO;
import com.ssafy.stella_trip.chat.dto.response.RoomListResponseDTO;
import com.ssafy.stella_trip.dao.chat.ChatDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatDAO chatDAO;

    public RoomListResponseDTO getRooms(){
        List<ChatRoomDTO> rooms = chatDAO.getChatRooms();

        RoomListResponseDTO roomListResponseDTO = new RoomListResponseDTO();
        Map<Integer, RoomListResponseDTO.Sido> sidoMap = new HashMap<>();
        rooms.forEach(room -> {
            if(!sidoMap.containsKey(room.getSidoCode())) {
                sidoMap.put(
                        room.getSidoCode(),
                        new RoomListResponseDTO.Sido(room.getSidoCode(), room.getSidoName(), new ArrayList<>())
                );
            }
            sidoMap.get(room.getSidoCode()).getRooms().add(
                    ChatRoomResponseDTO.builder()
                            .sidoCode(room.getSidoCode())
                            .sidoName(room.getSidoName())
                            .gugunCode(room.getGugunCode())
                            .gugunName(room.getGugunName())
                            .roomId(room.getRoomId())
                            .build()
            );
        });
        roomListResponseDTO.setSidoList(sidoMap.values().stream().toList());
        return roomListResponseDTO;
    }

    public ChatListResponseDTO getChatList(int size, int roomId, int lastChatId) {
        List<ChatDTO> chats = chatDAO.getChats(size, roomId, lastChatId);

        ChatListResponseDTO chatListResponseDTO = new ChatListResponseDTO();
        List<ChatResponseDTO> chatResponseDTOList = new ArrayList<>();
        chats.forEach(chat -> {
            chatResponseDTOList.add(
                    ChatResponseDTO.builder()
                            .chatId(chat.getChatId())
                            .content(chat.getContent())
                            .userName(chat.getUserName())
                            .build()
            );
        });

        chatListResponseDTO.setSize(size);
        chatListResponseDTO.setChats(chatResponseDTOList);
        return chatListResponseDTO;
    }
}
