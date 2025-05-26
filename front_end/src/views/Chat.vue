<!-- src/components/Chat.vue -->
<template>
  <div class="chat-container">
    <div class="chat-header">
      <h3>채팅방 1</h3>
      <span :class="connected ? 'connected' : 'disconnected'">
        {{ connected ? '연결됨' : '연결 끊김' }}
      </span>
    </div>

    <div class="messages" ref="messageList">
      <div
        v-for="msg in messages"
        :key="msg.chatId"
        :class="['message', msg.username === currentUser ? 'my-message' : 'other-message']"
      >
        <div class="message-info">
          <span class="username">{{ msg.username }}</span>
          <span class="time">{{ formatTime(msg.createdAt) }}</span>
        </div>
        <div class="message-content">{{ msg.message }}</div>
      </div>
    </div>

    <div class="input-area">
      <input
        v-model="newMessage"
        @keyup.enter="sendMessage"
        :disabled="!connected"
        placeholder="메시지 입력..."
      />
      <button @click="sendMessage" :disabled="!connected || !newMessage.trim()">전송</button>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { useAuthStore } from '@/stores/auth';
import socketService from '@/services/socket';
import { getRoomChats } from '@/services/api/domains/chat';
import type { ChatMessage } from '@/services/socket/types';
import type { ChatResponse } from '@/services/api/domains/chat/types';

const authStore = useAuthStore();

const messages = ref<ChatMessage[]>([]);
const newMessage = ref('');
const connected = ref(false);
const currentUser = ref('');
const messageList = ref<HTMLElement>();
let subscription: any = null;
onMounted(async () => {
  const token = authStore.getAccessToken;
  if (!token) {
    console.error('토큰이 없습니다');
    return;
  }

  console.log('토큰:', token);

  try {
    currentUser.value = JSON.parse(atob(token.split('.')[1])).sub;
  } catch (error) {
    console.error('토큰 파싱 실패:', error);
    return;
  }

  console.log('현재 사용자:', currentUser.value);

  // 채팅 히스토리 로딩 (실패해도 계속 진행)
  try {
    console.log('채팅 히스토리 로딩 중...');
    const chatHistory = await getRoomChats(1);
    if (chatHistory.chats) {
      messages.value = chatHistory.chats.map((chat: ChatResponse) => ({
        chatId: chat.chatId,
        roomId: 1,
        username: chat.userName,
        message: chat.content,
      }));
      nextTick(() => scrollToBottom());
    }
    console.log('채팅 히스토리 로드 완료');
  } catch (historyError) {
    console.warn('채팅 히스토리 로드 실패 (계속 진행):', historyError);
  }

  // 소켓 연결 (별도 try-catch)
  try {
    console.log('소켓 연결 시도 중...');
    await socketService.connect(token);
    console.log('소켓 연결 성공!');
    connected.value = true;

    console.log(`채팅방 1 구독 중...`);
    subscription = socketService.subscribeRoom(1, (message: ChatMessage) => {
      console.log('새 메시지 수신:', message);
      messages.value.push(message);
      nextTick(() => scrollToBottom());
    });
    console.log('채팅방 구독 완료');
  } catch (socketError) {
    console.error('소켓 연결 실패:', socketError);
    connected.value = false;
  }
});

onUnmounted(() => {
  subscription?.unsubscribe();
  socketService.disconnect();
});

const sendMessage = () => {
  if (!newMessage.value.trim()) return;

  if (socketService.sendMessage(1, newMessage.value)) {
    newMessage.value = '';
  }
};

const scrollToBottom = () => {
  if (messageList.value) {
    messageList.value.scrollTop = messageList.value.scrollHeight;
  }
};

const formatTime = (dateString?: string) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleTimeString('ko-KR', {
    hour: '2-digit',
    minute: '2-digit',
  });
};
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 500px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: #f5f5f5;
  border-bottom: 1px solid #ddd;
}

.connected {
  color: green;
}
.disconnected {
  color: red;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.message {
  margin-bottom: 12px;
  padding: 8px 12px;
  border-radius: 8px;
  max-width: 70%;
}

.my-message {
  align-self: flex-end;
  margin-left: auto;
  background: #007bff;
  color: white;
}

.other-message {
  background: white;
  border: 1px solid #ddd;
}

.message-info {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  margin-bottom: 4px;
  opacity: 0.8;
}

.input-area {
  display: flex;
  padding: 16px;
  border-top: 1px solid #ddd;
}

.input-area input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 8px;
}

.input-area button {
  padding: 8px 16px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.input-area button:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>
