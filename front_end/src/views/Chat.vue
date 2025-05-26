<template>
  <section class="relative h-[calc(100vh-4rem)]">
    <ResizablePanelGroup direction="horizontal" class="relative z-10 h-full">
      <!-- 채팅방 리스트 패널 -->
      <ResizablePanel :defaultSize="30" :minSize="25" :maxSize="50">
        <div
          class="flex h-full flex-col border-r border-purple-800/30 bg-slate-900/50 backdrop-blur-sm"
        >
          <!-- 헤더 -->
          <div class="border-b border-purple-800/30 bg-slate-800/30 p-6">
            <h2 class="text-2xl font-bold text-purple-300">지역별 채팅방</h2>
            <p class="mt-2 text-sm text-slate-400">여행 정보를 공유해보세요</p>
          </div>

          <!-- 로딩 상태 -->
          <div v-if="isLoadingRooms" class="flex flex-1 items-center justify-center">
            <div class="text-center">
              <div
                class="mx-auto mb-4 h-8 w-8 animate-spin rounded-full border-2 border-purple-400 border-t-transparent"
              ></div>
              <p class="text-slate-400">채팅방 목록을 불러오는 중...</p>
            </div>
          </div>

          <!-- 에러 상태 -->
          <div v-else-if="roomsError" class="flex flex-1 items-center justify-center p-4">
            <div class="text-center">
              <div
                class="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-full border border-red-500/20 bg-red-500/10"
              >
                <svg
                  class="h-8 w-8 text-red-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.082 15.5c-.77.833.192 2.5 1.732 2.5z"
                  ></path>
                </svg>
              </div>
              <h3 class="mb-2 text-lg font-medium text-slate-200">채팅방을 불러올 수 없습니다</h3>
              <p class="mb-4 text-slate-400">네트워크 연결을 확인해주세요.</p>
              <button
                class="rounded-xl bg-purple-600 px-6 py-3 text-white transition-all duration-300 hover:bg-purple-500 hover:shadow-lg hover:shadow-purple-500/20"
                @click="loadRoomList"
              >
                다시 시도
              </button>
            </div>
          </div>

          <!-- 채팅방 리스트 -->
          <div
            v-else
            class="scrollbar-thin scrollbar-track-slate-800 scrollbar-thumb-purple-700/50 flex-1 overflow-y-auto"
          >
            <div class="p-4">
              <!-- 아코디언으로 시도별 그룹 -->
              <Accordion
                type="single"
                collapsible
                :defaultValue="roomList.map(sido => `sido-${sido.sidoCode}`)"
                class="w-full space-y-3"
              >
                <AccordionItem
                  v-for="sido in roomList"
                  :key="sido.sidoCode"
                  :value="`sido-${sido.sidoCode}`"
                  class="overflow-hidden rounded-xl border border-purple-800/20 bg-slate-800/20 backdrop-blur-sm"
                >
                  <AccordionTrigger
                    class="px-4 py-3 font-semibold text-purple-300 hover:text-purple-200 hover:no-underline [&[data-state=open]>div>svg]:rotate-180"
                  >
                    <div class="mr-2 flex w-full items-center justify-between">
                      <span class="text-left">{{ sido.sidoName }}</span>
                      <div class="flex items-center gap-2">
                        <span class="text-xs text-slate-400">{{ sido.rooms.length }}개 지역</span>
                        <span
                          v-if="getSidoUnreadTotal(sido) > 0"
                          class="inline-flex items-center justify-center rounded-full bg-red-500 px-2 py-1 text-xs font-bold text-white shadow-sm"
                        >
                          {{ getSidoUnreadTotal(sido) }}
                        </span>
                      </div>
                    </div>
                  </AccordionTrigger>
                  <AccordionContent class="px-2 pb-3">
                    <div class="space-y-3 pt-2">
                      <div
                        v-for="room in sido.rooms"
                        :key="room.roomId"
                        :class="[
                          'cursor-pointer rounded-xl border p-4 transition-all duration-300 hover:-translate-y-1',
                          currentRoomId === room.roomId
                            ? 'border-purple-500/50 bg-indigo-950/80 shadow-lg shadow-purple-500/20'
                            : 'border-slate-700/50 bg-indigo-950/40 hover:border-purple-600/50 hover:bg-indigo-950/60 hover:shadow-md hover:shadow-purple-500/10',
                        ]"
                        @click="selectRoom(room)"
                      >
                        <div class="flex items-start justify-between">
                          <div class="min-w-0 flex-1">
                            <div class="mb-2 flex items-center gap-2">
                              <h3 class="truncate text-lg font-semibold text-purple-300">
                                # {{ room.gugunName }}
                              </h3>
                              <span
                                v-if="getRoomUnreadCount(room.roomId) > 0"
                                class="inline-flex items-center justify-center rounded-full bg-red-500 px-2 py-1 text-xs font-bold text-white shadow-sm"
                              >
                                {{ getRoomUnreadCount(room.roomId) }}
                              </span>
                            </div>

                            <p class="mb-3 truncate text-sm text-slate-300">
                              {{ getRoomLastMessage(room.roomId) || '대화를 시작해보세요!' }}
                            </p>

                            <div class="flex items-center justify-between">
                              <span class="text-xs text-slate-400">
                                {{ getRoomLastMessageTime(room.roomId) || '' }}
                              </span>
                              <div class="flex items-center gap-2">
                                <div
                                  class="h-2 w-2 rounded-full bg-green-400 shadow-sm shadow-green-400/50"
                                ></div>
                                <span class="text-xs text-slate-400">여행 정보 공유</span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </AccordionContent>
                </AccordionItem>
              </Accordion>

              <!-- 채팅방이 없는 경우 -->
              <div v-if="roomList.length === 0" class="py-12 text-center">
                <div
                  class="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-full border border-slate-700/50 bg-slate-800/50"
                >
                  <svg
                    class="h-8 w-8 text-slate-400"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-3.582 8-8 8a8.955 8.955 0 01-4.126-.98L3 21l1.98-5.874A8.955 8.955 0 013 12c0-4.418 3.582-8 8-8s8 3.582 8 8z"
                    ></path>
                  </svg>
                </div>
                <p class="text-slate-400">아직 채팅방이 없습니다.</p>
              </div>
            </div>
          </div>
        </div>
      </ResizablePanel>

      <ResizableHandle
        class="bg-purple-800/30 transition-colors hover:bg-purple-700/50"
        withHandle
      />

      <!-- 채팅 화면 패널 -->
      <ResizablePanel :defaultSize="70">
        <div class="flex h-full flex-col">
          <template v-if="selectedRoom">
            <ChatRoom
              :key="selectedRoom.roomId"
              :roomId="selectedRoom.roomId"
              :roomName="getRoomDisplayName(selectedRoom)"
              @messageReceived="handleMessageReceived"
            />
          </template>
          <template v-else>
            <div class="flex h-full items-center justify-center bg-slate-900/30 backdrop-blur-sm">
              <div class="text-center">
                <div
                  class="mx-auto mb-6 flex h-24 w-24 items-center justify-center rounded-full border border-purple-700/30 bg-slate-800/50"
                >
                  <svg
                    class="h-12 w-12 text-purple-400"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
                    ></path>
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
                    ></path>
                  </svg>
                </div>
                <h3 class="mb-3 text-xl font-bold text-purple-300">지역 채팅방을 선택해주세요</h3>
                <p class="text-slate-400">
                  왼쪽에서 원하는 지역의 채팅방을 선택하면
                  <br />
                  여행 정보를 공유할 수 있습니다.
                </p>
              </div>
            </div>
          </template>
        </div>
      </ResizablePanel>
    </ResizablePanelGroup>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ResizablePanelGroup, ResizablePanel, ResizableHandle } from '@/components/ui/resizable';
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from '@/components/ui/accordion';
import { getRoomList } from '@/services/api/domains/chat';
import type { SidoWithRooms, ChatRoom as ChatRoomType } from '@/services/api/domains/chat/types';
import ChatRoom from './ChatRoom.vue';
import { toast } from 'vue-sonner';

const route = useRoute();
const router = useRouter();

const roomList = ref<SidoWithRooms[]>([]);
const isLoadingRooms = ref(false);
const roomsError = ref(false);

// 라우트 파라미터에서 현재 roomId 가져오기
const currentRoomId = computed(() => {
  const roomId = route.params.roomId;
  return roomId ? Number(roomId) : null;
});

// 각 방의 추가 정보를 저장할 맵
const roomMetadata = ref<
  Map<
    number,
    {
      lastMessage: string;
      lastMessageTime: string;
      unreadCount: number;
    }
  >
>(new Map());

const selectedRoom = computed(() => {
  if (!currentRoomId.value) return null;

  for (const sido of roomList.value) {
    const room = sido.rooms.find(r => r.roomId === currentRoomId.value);
    if (room) return room;
  }
  return null;
});

const selectRoom = (room: ChatRoomType) => {
  // 라우터를 통해 roomId 변경
  router.push(`/chats/${room.roomId}`);

  // 읽음 처리
  const metadata = roomMetadata.value.get(room.roomId);
  if (metadata) {
    metadata.unreadCount = 0;
    roomMetadata.value.set(room.roomId, metadata);
  }
};

const getRoomDisplayName = (room: ChatRoomType) => {
  return `${room.sidoName} ${room.gugunName}`;
};

const getRoomLastMessage = (roomId: number) => {
  return roomMetadata.value.get(roomId)?.lastMessage || '';
};

const getRoomLastMessageTime = (roomId: number) => {
  const time = roomMetadata.value.get(roomId)?.lastMessageTime;
  if (!time) return '';

  const now = new Date();
  const date = new Date(time);
  const diff = now.getTime() - date.getTime();
  const minutes = Math.floor(diff / (1000 * 60));
  const hours = Math.floor(diff / (1000 * 60 * 60));
  const days = Math.floor(diff / (1000 * 60 * 60 * 24));

  if (minutes < 1) return '방금 전';
  if (minutes < 60) return `${minutes}분 전`;
  if (hours < 24) return `${hours}시간 전`;
  if (days < 7) return `${days}일 전`;

  return date.toLocaleDateString('ko-KR', {
    month: 'short',
    day: 'numeric',
  });
};

const getRoomUnreadCount = (roomId: number) => {
  return roomMetadata.value.get(roomId)?.unreadCount || 0;
};

// 시도별 읽지 않은 메시지 총합 계산
const getSidoUnreadTotal = (sido: SidoWithRooms) => {
  return sido.rooms.reduce((total, room) => {
    return total + getRoomUnreadCount(room.roomId);
  }, 0);
};

const loadRoomList = async () => {
  try {
    isLoadingRooms.value = true;
    roomsError.value = false;

    const response = await getRoomList();
    roomList.value = response.sidoList;

    // 각 방에 대한 메타데이터 초기화
    for (const sido of response.sidoList) {
      for (const room of sido.rooms) {
        if (!roomMetadata.value.has(room.roomId)) {
          roomMetadata.value.set(room.roomId, {
            lastMessage: '',
            lastMessageTime: '',
            unreadCount: 0,
          });
        }
      }
    }
  } catch {
    toast.error('채팅방 목록을 불러올 수 없습니다.');
    roomsError.value = true;
  } finally {
    isLoadingRooms.value = false;
  }
};

// 새 메시지 수신 시 메타데이터 업데이트
const updateRoomMetadata = (
  roomId: number,
  message: string,
  time: string,
  isFromCurrentUser: boolean
) => {
  const metadata = roomMetadata.value.get(roomId);
  if (metadata) {
    metadata.lastMessage = message;
    metadata.lastMessageTime = time;

    // 현재 선택된 방이 아니고, 자신이 보낸 메시지가 아니면 읽지 않은 메시지 증가
    if (currentRoomId.value !== roomId && !isFromCurrentUser) {
      metadata.unreadCount += 1;
    }

    roomMetadata.value.set(roomId, metadata);
  }
};

// ChatRoom에서 메시지 수신 이벤트 처리
const handleMessageReceived = (
  roomId: number,
  message: string,
  time: string,
  isFromCurrentUser: boolean
) => {
  updateRoomMetadata(roomId, message, time, isFromCurrentUser);
};

// 유효하지 않은 roomId인 경우 첫 번째 채팅방으로 리다이렉트
watch(
  [currentRoomId, roomList],
  ([roomId, rooms]) => {
    if (roomId && rooms.length > 0) {
      const roomExists = rooms.some(sido => sido.rooms.some(room => room.roomId === roomId));

      if (!roomExists && rooms[0]?.rooms.length > 0) {
        router.replace(`/chats/${rooms[0].rooms[0].roomId}`);
      }
    }
  },
  { immediate: true }
);

onMounted(() => {
  loadRoomList();
});
</script>

<style scoped>
.scrollbar-thin {
  scrollbar-width: thin;
}

.scrollbar-track-slate-800 {
  scrollbar-color: rgb(71 85 105 / 0.5) transparent;
}

.scrollbar-thumb-purple-700\/50::-webkit-scrollbar {
  width: 6px;
}

.scrollbar-thumb-purple-700\/50::-webkit-scrollbar-track {
  background: transparent;
}

.scrollbar-thumb-purple-700\/50::-webkit-scrollbar-thumb {
  background-color: rgb(126 34 206 / 0.5);
  border-radius: 3px;
}

.scrollbar-thumb-purple-700\/50::-webkit-scrollbar-thumb:hover {
  background-color: rgb(126 34 206 / 0.7);
}
</style>
