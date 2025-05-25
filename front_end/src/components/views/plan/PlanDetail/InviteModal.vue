<template>
  <Dialog :open="isOpen" @update:open="handleOpenModal">
    <DialogContent class="border-slate-700 bg-slate-800 sm:max-w-md">
      <DialogHeader>
        <DialogTitle class="flex items-center gap-2 text-white">
          <UserPlus class="h-5 w-5 text-blue-400" />
          {{ planTitle }} 여행에 초대하기
        </DialogTitle>
        <DialogDescription class="text-gray-300">
          이메일 주소를 입력하여 다른 사용자를 이 여행에 초대하세요.
        </DialogDescription>
      </DialogHeader>

      <div class="space-y-4 py-4">
        <!-- 이메일 입력 -->
        <div class="space-y-2">
          <label class="text-sm font-medium text-gray-300">이메일 주소</label>
          <Input
            v-model="inviteEmail"
            placeholder="example@email.com"
            type="email"
            class="border-slate-600 bg-slate-700 text-white placeholder:text-gray-400 focus:border-blue-500"
            :disabled="isLoading"
            @keyup.enter="sendInvite"
          />
        </div>

        <!-- 메시지 입력 (선택사항) -->
        <div class="space-y-2">
          <label class="text-sm font-medium text-gray-300">초대 메시지 (선택사항)</label>
          <Textarea
            v-model="inviteMessage"
            placeholder="함께 여행하실래요? 🌟"
            class="resize-none border-slate-600 bg-slate-700 text-white placeholder:text-gray-400 focus:border-blue-500"
            rows="3"
            :disabled="isLoading"
          />
        </div>

        <!-- 초대된 사용자 목록 -->
        <div v-if="invitedUsers.length > 0" class="space-y-2">
          <label class="text-sm font-medium text-gray-300">이미 초대된 사용자</label>
          <div class="max-h-24 space-y-1 overflow-y-auto">
            <div
              v-for="user in invitedUsers"
              :key="user.email"
              class="flex items-center justify-between rounded-md bg-slate-700 px-3 py-2 text-sm"
            >
              <span class="text-gray-300">{{ user.email }}</span>
              <Badge
                :variant="
                  user.status === 'pending'
                    ? 'secondary'
                    : user.status === 'accepted'
                      ? 'default'
                      : 'destructive'
                "
                class="text-xs"
              >
                {{ getStatusText(user.status) }}
              </Badge>
            </div>
          </div>
        </div>
      </div>

      <DialogFooter class="flex gap-2">
        <Button
          variant="outline"
          @click="$emit('update:open', false)"
          class="border-slate-600 text-gray-300 hover:bg-slate-700"
          :disabled="isLoading"
        >
          취소
        </Button>
        <Button
          @click="sendInvite"
          class="bg-blue-600 text-white hover:bg-blue-700"
          :disabled="!isValidEmail || isLoading"
        >
          <LoaderCircle v-if="isLoading" class="mr-2 h-4 w-4 animate-spin" />
          <Send v-else class="mr-2 h-4 w-4" />
          초대 보내기
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { toast } from 'vue-sonner';
import { UserPlus, Send, LoaderCircle } from 'lucide-vue-next';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Badge } from '@/components/ui/badge';
import { inviteToMyPlan } from '@/services/api/domains/plan'; // 새로운 API 함수 import

// 초대된 사용자 타입
interface InvitedUser {
  email: string;
  status: 'pending' | 'accepted' | 'rejected';
  invitedAt: string;
}

const props = defineProps<{
  isOpen: boolean;
  planId?: number;
  planTitle?: string;
}>();

const emit = defineEmits<{
  'update:open': [value: boolean];
}>();

const handleOpenModal = (isOpen: boolean) => {
  emit('update:open', isOpen);
};

// 상태 관리
const inviteEmail = ref('');
const inviteMessage = ref('');
const isLoading = ref(false);
const invitedUsers = ref<InvitedUser[]>([]);

// 이메일 유효성 검사
const isValidEmail = computed(() => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(inviteEmail.value.trim());
});

// 상태 텍스트 변환
const getStatusText = (status: string) => {
  switch (status) {
    case 'pending':
      return '대기중';
    case 'accepted':
      return '수락됨';
    case 'rejected':
      return '거절됨';
    default:
      return '알 수 없음';
  }
};

// 초대 보내기
const sendInvite = async () => {
  if (!isValidEmail.value || !props.planId || isLoading.value) return;

  const email = inviteEmail.value.trim();

  // 이미 초대된 사용자인지 확인
  if (invitedUsers.value.some(user => user.email === email)) {
    toast.error('이미 초대된 사용자입니다', {
      description: '다른 이메일 주소를 입력해주세요.',
      duration: 3000,
    });
    return;
  }

  try {
    isLoading.value = true;

    // API 호출 - 초대 보내기 (업데이트된 함수 사용)
    const result = await inviteToMyPlan(props.planId, email);

    if (result) {
      // 성공 시 로컬 상태에 추가
      invitedUsers.value.push({
        email,
        status: 'pending',
        invitedAt: new Date().toISOString(),
      });

      toast.success('초대를 보냈습니다', {
        description: `${email}에게 초대 메일이 발송되었습니다.`,
        duration: 4000,
      });

      // 입력 필드 초기화
      inviteEmail.value = '';
      inviteMessage.value = '';
    } else {
      throw new Error('초대 보내기 실패');
    }
  } catch (error) {
    console.error('초대 실패:', error);
    toast.error('초대 보내기에 실패했습니다', {
      description: '잠시 후 다시 시도해주세요.',
      duration: 4000,
    });
  } finally {
    isLoading.value = false;
  }
};

// 초대된 사용자 목록 가져오기
const loadInvitedUsers = async () => {
  if (!props.planId) return;

  try {
    // API 호출 - 초대된 사용자 목록 가져오기
    // const users = await getPlanInvitations(props.planId);
    // invitedUsers.value = users;

    // 임시 데이터 (실제 API 구현 시 제거)
    invitedUsers.value = [
      // {
      //   email: 'example@test.com',
      //   status: 'pending',
      //   invitedAt: new Date().toISOString(),
      // }
    ];
  } catch (error) {
    console.error('초대 목록 로드 실패:', error);
  }
};

// 모달이 열릴 때 초대된 사용자 목록 로드
watch(
  () => props.isOpen,
  isOpen => {
    if (isOpen) {
      loadInvitedUsers();
      // 입력 필드 초기화
      inviteEmail.value = '';
      inviteMessage.value = '';
    }
  }
);
</script>
