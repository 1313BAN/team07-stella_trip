<template>
  <Dialog :open="isOpen" @update:open="$emit('update:open', $event)">
    <DialogContent class="border-slate-700 bg-slate-800 sm:max-w-md">
      <DialogHeader>
        <DialogTitle class="flex items-center gap-2 text-white">
          <component :is="getIcon()" :class="getIconClass()" class="h-5 w-5" />
          {{ title }}
        </DialogTitle>
        <DialogDescription v-if="description" class="mt-2 text-gray-300">
          {{ description }}
        </DialogDescription>
      </DialogHeader>

      <!-- 추가 내용이 있는 경우 -->
      <div v-if="$slots.default || details" class="py-4">
        <slot />

        <!-- 세부 정보 목록 -->
        <div v-if="details && details.length > 0" class="space-y-2">
          <div
            v-for="(detail, index) in details"
            :key="index"
            class="flex items-center justify-between rounded-md bg-slate-700/50 px-3 py-2 text-sm"
          >
            <span class="text-gray-300">{{ detail.label }}</span>
            <span class="font-medium text-white">{{ detail.value }}</span>
          </div>
        </div>
      </div>

      <DialogFooter class="flex gap-2">
        <Button
          variant="outline"
          @click="handleCancel"
          class="border-slate-600 text-gray-300 hover:bg-slate-700"
          :disabled="isLoading"
        >
          {{ cancelText }}
        </Button>
        <Button
          :variant="variant"
          @click="handleConfirm"
          :class="getConfirmButtonClass()"
          :disabled="isLoading"
        >
          <LoaderCircle v-if="isLoading" class="mr-2 h-4 w-4 animate-spin" />
          <component v-else-if="getConfirmIcon()" :is="getConfirmIcon()" class="mr-2 h-4 w-4" />
          {{ confirmText }}
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>

<script setup lang="ts">
import {
  AlertTriangle,
  Trash2,
  LogOut,
  CheckCircle,
  Info,
  LoaderCircle,
  Save,
} from 'lucide-vue-next';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';

// 세부 정보 타입
interface ConfirmDetail {
  label: string;
  value: string;
}

const props = withDefaults(
  defineProps<{
    isOpen: boolean;
    title: string;
    description?: string;
    confirmText?: string;
    cancelText?: string;
    variant?: 'default' | 'destructive' | 'secondary' | 'ghost' | 'link' | 'outline';
    type?: 'warning' | 'danger' | 'info' | 'success' | 'delete' | 'leave';
    details?: ConfirmDetail[];
    isLoading?: boolean;
  }>(),
  {
    confirmText: '확인',
    cancelText: '취소',
    variant: 'default',
    type: 'warning',
    isLoading: false,
  }
);

const emit = defineEmits<{
  'update:open': [value: boolean];
  'confirm': [];
  'cancel': [];
}>();

// 타입에 따른 아이콘 선택
const getIcon = () => {
  switch (props.type) {
    case 'danger':
    case 'delete':
      return Trash2;
    case 'leave':
      return LogOut;
    case 'success':
      return CheckCircle;
    case 'info':
      return Info;
    case 'warning':
    default:
      return AlertTriangle;
  }
};

// 타입에 따른 아이콘 색상 클래스
const getIconClass = () => {
  switch (props.type) {
    case 'danger':
    case 'delete':
    case 'leave':
      return 'text-red-400';
    case 'success':
      return 'text-green-400';
    case 'info':
      return 'text-blue-400';
    case 'warning':
    default:
      return 'text-yellow-400';
  }
};

// 확인 버튼 아이콘
const getConfirmIcon = () => {
  switch (props.type) {
    case 'delete':
      return Trash2;
    case 'leave':
      return LogOut;
    case 'success':
      return Save;
    default:
      return null;
  }
};

// 확인 버튼 클래스
const getConfirmButtonClass = () => {
  const baseClass = 'font-medium';

  switch (props.variant) {
    case 'destructive':
      return `${baseClass} bg-red-600 hover:bg-red-700 text-white`;
    case 'secondary':
      return `${baseClass} bg-slate-600 hover:bg-slate-700 text-white`;
    default:
      return `${baseClass} bg-blue-600 hover:bg-blue-700 text-white`;
  }
};

// 이벤트 핸들러
const handleConfirm = () => {
  emit('confirm');
};

const handleCancel = () => {
  emit('cancel');
  emit('update:open', false);
};
</script>
