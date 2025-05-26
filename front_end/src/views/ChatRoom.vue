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

        <!-- 이전 메시지 로딩 버튼 -->
        <div v-if="hasMoreMessages && !isLoading" class="flex justify-center py-2">
          <button @click="loadMoreMessages" class="text-sm text-purple-400 hover:text-purple-300">
            이전 메시지 불러오기
          </button>
        </div>

        <!-- 추가 로딩 (스크롤 위로 당겨서 더 보기) -->
        <div v-if="isLoading && messages.length > 0" class="flex justify-center py-2">
          <div
            class="h-4 w-4 animate-spin rounded-full border-2 border-purple-400 border-t-transparent"
          ></div>
        </div>

        <!-- 메시지 리스트 -->
        <div
          v-for="msg in sortedMessages"
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
import { ref, onMounted, onUnmounted, nextTick, watch, computed } from 'vue';
import { useAuthStore } from '@/stores/auth';
import socketService from '@/services/socket';
import { getRoomChats } from '@/services/api/domains/chat';
import type { ChatMessage } from '@/services/socket/types';
import type { ChatResponse } from '@/services/api/domains/chat/types';
import { RotateCcw, AlertTriangle, MessageCircle, Send } from 'lucide-vue-next';
import { toast } from 'vue-sonner';

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
const hasMoreMessages = ref(true);

let subscription: SocketSubscription | null = null;
let oldestChatId: number | null = null;

// 메시지를 시간순으로 정렬 (오래된 것이 위, 최신이 아래)
const sortedMessages = computed(() => {
  return [...messages.value].sort((a, b) => {
    const timeA = new Date(a.createdAt || 0).getTime();
    const timeB = new Date(b.createdAt || 0).getTime();
    return timeA - timeB;
  });
});

// roomId 변경 감지
watch(
  () => props.roomId,
  async (newRoomId, oldRoomId) => {
    if (newRoomId !== oldRoomId) {
      // 이전 구독 해제
      subscription?.unsubscribe();

      // 새 채팅방 데이터 리셋
      messages.value = [];
      oldestChatId = null;
      hasMoreMessages.value = true;

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
      lastChatId: oldestChatId || undefined,
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

      // 중복 제거를 위해 기존 메시지 ID 체크
      const existingIds = new Set(messages.value.map(msg => msg.chatId));
      const filteredNewMessages = newMessages.filter(msg => !existingIds.has(msg.chatId));

      // 새 메시지 추가
      messages.value = [...messages.value, ...filteredNewMessages];

      // oldestChatId 업데이트 (가장 오래된 메시지의 ID로)
      if (filteredNewMessages.length > 0) {
        const chatIds = filteredNewMessages
          .map(m => m.chatId)
          .filter((id): id is number => id !== undefined);

        if (chatIds.length > 0) {
          // API가 최신순으로 반환한다면 마지막이 가장 오래된 것
          oldestChatId = Math.min(...chatIds);
        }
      }

      // 더 이상 불러올 메시지가 없는지 체크
      if (chatHistory.chats.length < 50) {
        hasMoreMessages.value = false;
      }

      await nextTick();
      // 처음 로드시에만 맨 아래로 스크롤
      if (oldestChatId === null || messages.value.length <= 50) {
        scrollToBottom();
      }
    } else {
      hasMoreMessages.value = false;
    }
  } catch (error) {
    console.error('채팅 히스토리 로드 실패:', error);
    messagesError.value = true;
  } finally {
    isLoading.value = false;
  }
};

const loadMoreMessages = async () => {
  if (isLoading.value || !hasMoreMessages.value) return;

  const scrollHeight = messageContainer.value?.scrollHeight || 0;
  const scrollTop = messageContainer.value?.scrollTop || 0;

  await loadChatHistory();

  // 스크롤 위치 조정 (새로 로드된 메시지로 인한 스크롤 점프 방지)
  await nextTick();
  if (messageContainer.value) {
    const newScrollHeight = messageContainer.value.scrollHeight;
    messageContainer.value.scrollTop = scrollTop + (newScrollHeight - scrollHeight);
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
    // 중복 메시지 체크
    const exists = messages.value.some(msg => msg.chatId === message.chatId);
    if (!exists) {
      // 새 메시지는 현재 시간으로 설정 (서버 시간이 없을 경우)
      if (!message.createdAt) {
        message.createdAt = new Date().toISOString();
      }

      messages.value.push(message);

      // 부모 컴포넌트에 메시지 수신 알림
      emit(
        'messageReceived',
        roomId,
        message.message,
        message.createdAt,
        message.username === currentUser.value
      );

      nextTick(() => scrollToBottom());
    }
  });
};

const sendMessage = () => {
  if (!newMessage.value.trim()) return;

  if (socketService.sendMessage(props.roomId, newMessage.value)) {
    newMessage.value = '';
  } else {
    toast.error('메시지 전송에 실패했습니다. 연결 상태를 확인해주세요.');
  }
};

const refreshMessages = async () => {
  if (!isLoading.value) {
    oldestChatId = null;
    messages.value = [];
    hasMoreMessages.value = true;
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

  try {
    // 서버에서 오는 시간을 Date 객체로 변환
    const date = new Date(dateString);

    // 유효하지 않은 날짜 체크
    if (isNaN(date.getTime())) {
      return '';
    }

    // 현재 시간 (한국 시간)
    const now = new Date();

    // 서버 시간을 한국 시간으로 변환 (UTC라고 가정하고 9시간 추가)
    const koreaDate = new Date(date.getTime() + 9 * 60 * 60 * 1000);

    // 현재 한국 시간
    const nowKorea = new Date(
      now.getTime() + now.getTimezoneOffset() * 60 * 1000 + 9 * 60 * 60 * 1000
    );

    // 날짜 비교를 위한 문자열 (YYYY-MM-DD 형식)
    const nowDateStr = nowKorea.toISOString().split('T')[0];
    const msgDateStr = koreaDate.toISOString().split('T')[0];

    const isToday = nowDateStr === msgDateStr;

    // 어제 확인
    const yesterday = new Date(nowKorea.getTime() - 24 * 60 * 60 * 1000);
    const yesterdayStr = yesterday.toISOString().split('T')[0];
    const isYesterday = msgDateStr === yesterdayStr;

    // 한국 시간 기준 시:분 추출
    const hours = koreaDate.getHours().toString().padStart(2, '0');
    const minutes = koreaDate.getMinutes().toString().padStart(2, '0');
    const timeStr = `${hours}:${minutes}`;

    if (isToday) {
      return timeStr;
    } else if (isYesterday) {
      return `어제 ${timeStr}`;
    } else {
      // 1주일 이내인지 확인
      const diffTime = nowKorea.getTime() - koreaDate.getTime();
      const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));

      if (diffDays >= 0 && diffDays <= 7) {
        const weekdays = ['일', '월', '화', '수', '목', '금', '토'];
        const dayOfWeek = weekdays[koreaDate.getDay()];
        return `${dayOfWeek} ${timeStr}`;
      } else {
        // 1주일 이상 전
        const month = koreaDate.getMonth() + 1;
        const day = koreaDate.getDate();
        return `${month}월 ${day}일 ${timeStr}`;
      }
    }
  } catch (error) {
    console.error('시간 포맷 에러:', error, 'Input:', dateString);
    return '';
  }
};
</script>
