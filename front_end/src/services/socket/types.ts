export interface ChatMessage {
  chatId?: number;
  roomId: number;
  username?: string;
  message: string;
  createdAt?: string;
}
