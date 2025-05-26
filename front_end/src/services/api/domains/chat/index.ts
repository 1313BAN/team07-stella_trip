import { CHAT } from '../../endpoint';
import api from '../../api';
import type { ApiResponse } from '../../types';
import type { RoomChatsResponse, RoomListResponse, GetRoomChatsParams } from './types';

/**
 * 채팅방의 채팅 내역 조회
 * @param roomId 채팅방 ID
 * @param params lastChatId, size 파라미터
 * @returns 채팅 내역
 */
export const getRoomChats = async (
  roomId: number,
  params?: GetRoomChatsParams
): Promise<RoomChatsResponse> => {
  const queryParams = {
    lastChatId: params?.lastChatId ?? -1,
    ...(params?.size && { size: params.size }),
  };

  const response = await api.get<ApiResponse<RoomChatsResponse>>(CHAT.ROOM_DETAIL(roomId), {
    params: queryParams,
  });

  return response.data.body;
};

/**
 * 채팅방 목록 조회
 * @returns 채팅방 목록 (시도별로 그룹화)
 */
export const getRoomList = async (): Promise<RoomListResponse> => {
  const response = await api.get<ApiResponse<RoomListResponse>>(CHAT.ROOM_LIST);

  return response.data.body;
};

export * from './types';
