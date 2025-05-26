<template>
  <div class="flex h-full flex-col bg-slate-900/50 backdrop-blur-sm">
    <!-- 채팅방 헤더 -->
    <div
      class="flex items-center justify-between border-b border-purple-800/30 bg-slate-800/30 p-4"
    >
      <div class="flex items-center gap-3">
        <div class="flex items-center gap-3">
          <h3 class="text-xl font-bold text-purple-300"># {{ roomName }}</h3>
          <span
            :class="[
              'inline-flex items-center rounded-full border px-3 py-1 text-xs font-semibold',
              connected
                ? 'border-green-500/20 bg-green-500/10 text-green-400'
                : 'border-red-500/20 bg-red-500/10 text-red-400',
            ]"
          >
            <div
              :class="[
                'mr-2 h-2 w-2 rounded-full',
                connected
                  ? 'bg-green-400 shadow-sm shadow-green-400/50'
                  : 'bg-red-400 shadow-sm shadow-red-400/50',
              ]"
            ></div>
            {{ connected ? '연결됨' : '연결 끊김' }}
          </span>
        </div>
      </div>

      <div class="flex items-center gap-3">
        <span class="text-sm font-medium text-slate-400">여행 정보 공유방</span>
        <button
          class="rounded-lg p-2 text-slate-400 transition-all duration-300 hover:bg-slate-700/50 hover:text-purple-300"
          @click="refreshMessages"
          :disabled="isLoading"
        >
          <RotateCcw :class="['h-5 w-5', isLoading ? 'animate-spin' : '']" />
        </button>
      </div>
    </div>

    <!-- 메시지 영역 -->
    <div
      class="scrollbar-thin scrollbar-track-slate-800 scrollbar-thumb-purple-700/50 flex-1 space-y-4 overflow-y-auto p-4"
      ref="messageContainer"
    >
      <!-- 로딩 상태 -->
      <div v-if="isLoading && messages.length === 0" class="flex justify-center py-12">
        <div class="text-center">
          <div
            class="mx-auto mb-4 h-8 w-8 animate-spin rounded-full border-2 border-purple-400 border-t-transparent"
          ></div>
          <p class="text-slate-400">메시지를 불러오는 중...</p>
        </div>
      </div>

      <!-- 에러 상태 -->
      <div v-else-if="messagesError" class="flex justify-center py-12">
        <div class="text-center">
          <div
            class="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-full border border-red-500/20 bg-red-500/10"
          >
            <AlertTriangle class="h-8 w-8 text-red-400" />
          </div>
          <h3 class="mb-2 text-lg font-semibold text-slate-200">메시지를 불러올 수 없습니다</h3>
          <p class="mb-4 text-slate-400">네트워크 연결을 확인해주세요.</p>
          <button
            @click="loadChatHistory"
            class="rounded-xl bg-purple-600 px-6 py-3 text-white transition-all duration-300 hover:bg-purple-500 hover:shadow-lg hover:shadow-purple-500/20"
          >
            다시 시도
          </button>
        </div>
      </div>

      <!-- 메시지 목록 -->
      <template v-else>
        <!-- 빈 채팅방 안내 -->
        <div v-if="messages.length === 0 && !isLoading" class="flex justify-center py-12">
          <div class="text-center">
            <div
              class="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-full border border-purple-500/20 bg-purple-500/10"
            >
              <MessageCircle class="h-8 w-8 text-purple-400" />
            </div>
            <h3 class="mb-2 text-lg font-bold text-purple-300">대화를 시작해보세요!</h3>
            <p class="text-slate-400">{{ roomName }}의 첫 번째 메시지를 남겨보세요.</p>
          </div>
        </div>

        <!-- 메시지 리스트 -->
        <div
          v-for="msg in messages"
          :key="msg.chatId"
          :class="['mb-4 flex', msg.username === currentUser ? 'justify-end' : 'justify-start']"
        >
          <!-- 다른 사용자의 메시지 (왼쪽) -->
          <div v-if="msg.username !== currentUser" class="flex max-w-[75%] min-w-0">
            <div class="flex min-w-0 flex-1 flex-col">
              <div class="mb-1 ml-1 text-xs font-medium text-slate-400">
                {{ msg.username }}
              </div>
              <div
                class="min-w-0 rounded-2xl rounded-bl-sm border border-slate-600/40 bg-slate-700/70 px-4 py-3 shadow-lg backdrop-blur-sm"
              >
                <div
                  class="word-break overflow-wrap-anywhere text-sm leading-relaxed break-words whitespace-pre-wrap text-slate-100"
                >
                  {{ msg.message }}
                </div>
                <div class="mt-2 text-xs text-slate-400">
                  {{ formatTime(msg.createdAt) }}
                </div>
              </div>
            </div>
          </div>

          <!-- 내 메시지 (오른쪽) -->
          <div v-else class="flex max-w-[75%] min-w-0">
            <div class="flex w-full min-w-0 flex-col items-end">
              <div
                class="min-w-0 rounded-2xl rounded-br-sm border border-purple-400/30 bg-gradient-to-br from-purple-500 to-purple-600 px-4 py-3 shadow-lg"
              >
                <div
                  class="word-break overflow-wrap-anywhere text-sm leading-relaxed break-words whitespace-pre-wrap text-white"
                >
                  {{ msg.message }}
                </div>
                <div class="mt-2 text-xs text-purple-100/80">
                  {{ formatTime(msg.createdAt) }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>

      <!-- 추가 로딩 (스크롤 위로 당겨서 더 보기) -->
      <div v-if="isLoading && messages.length > 0" class="flex justify-center py-2">
        <div
          class="h-4 w-4 animate-spin rounded-full border-2 border-purple-400 border-t-transparent"
        ></div>
      </div>
    </div>

    <!-- 메시지 입력 영역 -->
    <div class="border-t border-purple-800/30 bg-slate-800/30 p-4">
      <div class="flex gap-3">
        <input
          v-model="newMessage"
          @keyup.enter="sendMessage"
          :disabled="!connected"
          placeholder="여행 정보를 공유해보세요..."
          class="flex-1 rounded-xl border border-slate-600/50 bg-slate-800/50 px-4 py-3 text-slate-100 placeholder-slate-400 backdrop-blur-sm transition-all duration-300 focus:border-purple-500/50 focus:ring-2 focus:ring-purple-500/20 focus:outline-none disabled:cursor-not-allowed disabled:bg-slate-800/30 disabled:text-slate-500"
        />
        <button
          @click="sendMessage"
          :disabled="!connected || !newMessage.trim()"
          class="rounded-xl bg-purple-600 px-4 py-3 text-white transition-all duration-300 hover:bg-purple-500 hover:shadow-lg hover:shadow-purple-500/20 focus:ring-2 focus:ring-purple-500/50 focus:ring-offset-2 focus:ring-offset-slate-900 focus:outline-none disabled:cursor-not-allowed disabled:bg-slate-600/50 disabled:text-slate-400"
        >
          <Send class="h-5 w-5" />
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick, watch } from 'vue';
import { useAuthStore } from '@/stores/auth';
import socketService from '@/services/socket';
import { getRoomChats } from '@/services/api/domains/chat';
import type { ChatMessage } from '@/services/socket/types';
import type { ChatResponse } from '@/services/api/domains/chat/types';
import { RotateCcw, AlertTriangle, MessageCircle, Send } from 'lucide-vue-next';

interface Props {
  roomId: number;
  roomName: string;
}

// 소켓 구독을 위한 타입 정의
interface SocketSubscription {
  unsubscribe: () => void;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  messageReceived: [roomId: number, message: string, time: string, isFromCurrentUser: boolean];
}>();

const authStore = useAuthStore();

const messages = ref<ChatMessage[]>([]);
const newMessage = ref('');
const connected = ref(false);
const currentUser = ref('');
const messageContainer = ref<HTMLElement>();
const isLoading = ref(false);
const messagesError = ref(false);

let subscription: SocketSubscription | null = null;
let lastChatId = -1;

// roomId 변경 감지
watch(
  () => props.roomId,
  async (newRoomId, oldRoomId) => {
    if (newRoomId !== oldRoomId) {
      // 이전 구독 해제
      subscription?.unsubscribe();

      // 새 채팅방 데이터 로드
      messages.value = [];
      lastChatId = -1;
      await loadChatHistory();

      // 새 채팅방 구독
      if (connected.value) {
        subscribeToRoom(newRoomId);
      }
    }
  },
  { immediate: false }
);

onMounted(async () => {
  const token = authStore.getAccessToken;
  const user = authStore.getUser;

  if (!token || !user) {
    messagesError.value = true;
    return;
  }

  // authStore에서 사용자 정보 가져오기
  currentUser.value = user.name;
  // 채팅 히스토리 로딩
  await loadChatHistory();

  // 소켓 연결
  await connectSocket(token);
});

onUnmounted(() => {
  subscription?.unsubscribe();
  socketService.disconnect();
});

const loadChatHistory = async () => {
  try {
    isLoading.value = true;
    messagesError.value = false;

    const chatHistory = await getRoomChats(props.roomId, {
      lastChatId: lastChatId,
      size: 50,
    });

    if (chatHistory.chats && chatHistory.chats.length > 0) {
      const newMessages: ChatMessage[] = chatHistory.chats.map((chat: ChatResponse) => ({
        chatId: chat.chatId,
        roomId: props.roomId,
        username: chat.userName,
        message: chat.content,
        createdAt: chat.createdAt || new Date().toISOString(),
      }));

      // 새 메시지를 기존 메시지 앞에 추가 (오래된 메시지가 위에)
      messages.value = [...newMessages, ...messages.value];

      // lastChatId 업데이트 (가장 오래된 메시지의 ID)
      if (newMessages.length > 0) {
        const chatIds = newMessages
          .map(m => m.chatId)
          .filter((id): id is number => id !== undefined);

        if (chatIds.length > 0) {
          lastChatId = Math.min(...chatIds);
        }
      }

      await nextTick();
      scrollToBottom();
    }
  } catch (error) {
    console.error('채팅 히스토리 로드 실패:', error);
    messagesError.value = true;
  } finally {
    isLoading.value = false;
  }
};

const connectSocket = async (token: string) => {
  try {
    await socketService.connect(token);
    connected.value = true;
    await subscribeToRoom(props.roomId);
  } catch (error) {
    console.error('소켓 연결 실패:', error);
    connected.value = false;
  }
};

const subscribeToRoom = async (roomId: number) => {
  subscription = socketService.subscribeRoom(roomId, (message: ChatMessage) => {
    messages.value.push(message);

    // 부모 컴포넌트에 메시지 수신 알림
    emit(
      'messageReceived',
      roomId,
      message.message,
      message.createdAt || new Date().toISOString(),
      message.username === currentUser.value
    );

    nextTick(() => scrollToBottom());
  });
};

const sendMessage = () => {
  if (!newMessage.value.trim()) return;

  console.log('메시지 전송 시도:', newMessage.value);
  console.log('연결 상태:', connected.value);

  if (socketService.sendMessage(props.roomId, newMessage.value)) {
    console.log('메시지 전송 성공');
    newMessage.value = '';
  } else {
    console.error('메시지 전송 실패 - 소켓이 연결되지 않음');
    // 사용자에게 알림 표시 또는 재연결 시도
  }
};

const refreshMessages = async () => {
  if (!isLoading.value) {
    lastChatId = -1;
    messages.value = [];
    await loadChatHistory();
  }
};

const scrollToBottom = () => {
  if (messageContainer.value) {
    messageContainer.value.scrollTop = messageContainer.value.scrollHeight;
  }
};

const formatTime = (dateString?: string) => {
  if (!dateString) return '';

  const now = new Date();
  const date = new Date(dateString);

  const isToday = now.toDateString() === date.toDateString();

  if (isToday) {
    return date.toLocaleTimeString('ko-KR', {
      hour: '2-digit',
      minute: '2-digit',
    });
  } else {
    return date.toLocaleDateString('ko-KR', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  }
};
</script>
