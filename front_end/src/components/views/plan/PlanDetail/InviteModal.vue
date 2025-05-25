<template>
  <Dialog :open="isOpen" @update:open="handleOpenModal">
    <DialogContent class="border-slate-700 bg-slate-800 sm:max-w-md">
      <DialogHeader>
        <DialogTitle class="flex items-center gap-2 text-white">
          <UserPlus class="h-5 w-5 text-blue-400" />
          {{ planTitle }} 여행에 사용자 추가
        </DialogTitle>
        <DialogDescription class="text-gray-300">
          이메일 주소를 입력하여 다른 사용자를 이 여행에 추가하세요.
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
            @keyup.enter="addUser"
          />
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
          @click="addUser"
          class="bg-blue-600 text-white hover:bg-blue-700"
          :disabled="!isValidEmail || isLoading"
        >
          <LoaderCircle v-if="isLoading" class="mr-2 h-4 w-4 animate-spin" />
          <UserPlus v-else class="mr-2 h-4 w-4" />
          사용자 추가
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { toast } from 'vue-sonner';
import { UserPlus, LoaderCircle } from 'lucide-vue-next';
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
import { inviteToMyPlan } from '@/services/api/domains/plan';

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
const isLoading = ref(false);

// 이메일 유효성 검사
const isValidEmail = computed(() => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(inviteEmail.value.trim());
});

// 사용자 추가
const addUser = async () => {
  if (!isValidEmail.value || !props.planId || isLoading.value) return;

  const email = inviteEmail.value.trim();

  try {
    isLoading.value = true;

    const result = await inviteToMyPlan(props.planId, email);

    if (result) {
      toast.success('사용자가 여행에 추가되었습니다', {
        description: `${email} 사용자가 추가되었습니다.`,
        duration: 4000,
      });

      // 입력 필드 초기화 후 모달 닫기
      inviteEmail.value = '';
      emit('update:open', false);
    }
  } catch (error) {
    console.error('사용자 추가 실패:', error);
    toast.error('사용자 추가에 실패했습니다', {
      description: '존재하지 않는 사용자이거나 오류가 발생했습니다.',
      duration: 4000,
    });
  } finally {
    isLoading.value = false;
  }
};

// 모달이 열릴 때 입력 필드 초기화
watch(
  () => props.isOpen,
  isOpen => {
    if (isOpen) {
      inviteEmail.value = '';
    }
  }
);
</script>
