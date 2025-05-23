package com.ssafy.stella_trip.dao.chat;

import com.ssafy.stella_trip.chat.dto.ChatDTO;
import com.ssafy.stella_trip.chat.dto.ChatRoomDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatDAO {
    List<ChatRoomDTO> getChatRooms();

    List<ChatDTO> getChats(@Param("size") int size, @Param("roomId") int roomId, @Param("lastChatId") int lastChatId);

    void addChat(@Param("chat") ChatDTO chat);

    boolean existsById(@Param("roomId") int roomId);
}
