export interface ChatResponse {
  createdAt: string;
  chatId: number;
  userName: string;
  content: string;
}

export interface RoomChatsResponse {
  size: number;
  chats: ChatResponse[];
}

export interface ChatRoom {
  roomId: number;
  sidoCode: number;
  sidoName: string;
  gugunCode: number;
  gugunName: string;
}

export interface SidoWithRooms {
  sidoCode: number;
  sidoName: string;
  rooms: ChatRoom[];
}

export interface RoomListResponse {
  sidoList: SidoWithRooms[];
}

// 요청 파라미터 타입
export interface GetRoomChatsParams {
  lastChatId?: number;
  size?: number;
}
