import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { SOCKET_ENDPOINTS } from './endpoint';
import type { ChatMessage } from './types';

class SocketService {
  private client: Client | null = null;
  private connected = false;

  // 연결
  async connect(token: string): Promise<void> {
    if (this.connected) return;

    return new Promise((resolve, reject) => {
      this.client = new Client({
        webSocketFactory: () => new SockJS(SOCKET_ENDPOINTS.WS_URL),
        connectHeaders: {
          Authorization: token,
        },
        onConnect: () => {
          this.connected = true;
          resolve();
        },
        onStompError: frame => {
          reject(frame);
        },
      });

      this.client.activate();
    });
  }

  // 채팅방 구독
  subscribeRoom(roomId: number, callback: (message: ChatMessage) => void) {
    if (!this.client || !this.connected) return null;

    return this.client.subscribe(SOCKET_ENDPOINTS.SUBSCRIBE_ROOM(roomId), message => {
      const data = JSON.parse(message.body);
      callback(data);
    });
  }

  // 메시지 전송
  sendMessage(roomId: number, message: string): boolean {
    if (!this.client || !this.connected) return false;

    try {
      this.client.publish({
        destination: SOCKET_ENDPOINTS.PUBLISH_MESSAGE,
        body: JSON.stringify({ roomId, message }),
      });
      return true;
    } catch {
      return false;
    }
    return true;
  }

  // 연결 해제
  disconnect() {
    if (this.client) {
      this.client.deactivate();
      this.connected = false;
    }
  }

  isConnected() {
    return this.connected;
  }
}

export default new SocketService();
