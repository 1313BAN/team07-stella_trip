export const SOCKET_ENDPOINTS = {
  WS_URL: `${import.meta.env.VITE_API_BASE_URL}/v1/websocket`,
  SUBSCRIBE_ROOM: (roomId: number) => `/sub/chat/room/${roomId}`,
  PUBLISH_MESSAGE: '/pub/chat/message',
} as const;
