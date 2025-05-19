<script setup lang="ts">
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog';
import { X } from 'lucide-vue-next';
import { computed } from 'vue';

interface Props {
  modelValue: boolean;
  title?: string;
  description?: string;
  maxWidth?: string;
  closeOnOutsideClick?: boolean;
  showCloseButton?: boolean;
  hideOverlay?: boolean;
  overlayOpacity?: string;
}

const props = withDefaults(defineProps<Props>(), {
  title: '',
  description: '',
  maxWidth: '28rem',
  closeOnOutsideClick: true,
  showCloseButton: true,
  hideOverlay: false,
  overlayOpacity: '0.6',
});

const emit = defineEmits<{
  'update:modelValue': [value: boolean];
  'close': [];
}>();

// 모달 상태를 v-model과 연결
const isOpen = computed({
  get: () => props.modelValue,
  set: value => {
    emit('update:modelValue', value);
    if (!value) {
      emit('close');
    }
  },
});

// 모달 닫기
const closeModal = () => {
  isOpen.value = false;
};
</script>

<template>
  <Dialog v-model:open="isOpen" class="stellatrip-modal">
    <DialogContent
      class="dialog-content relative overflow-hidden rounded-2xl border border-purple-400/30 bg-indigo-950 p-6 backdrop-blur-md"
    >
      <!-- 닫기 버튼 -->
      <button
        v-if="showCloseButton"
        aria-label="모달 닫기"
        class="absolute top-4 right-4 h-7 w-7 cursor-pointer rounded-full border-none bg-white/10 p-1 text-white/70 transition-all duration-200"
        @click="closeModal"
      >
        <X class="h-full w-full" />
      </button>
      <!-- 제목과 설명 -->
      <DialogHeader v-if="title || description">
        <DialogTitle v-if="title" class="text-xl font-semibold text-white">
          {{ title }}
        </DialogTitle>
        <DialogDescription v-if="description" class="text-sm text-gray-300">
          {{ description }}
        </DialogDescription>
      </DialogHeader>

      <!-- 컨텐츠 영역 -->
      <div class="py-2 text-white">
        <slot></slot>
      </div>

      <!-- 푸터 영역 -->
      <DialogFooter v-if="$slots.footer" class="mt-4 flex items-center justify-end gap-3">
        <slot name="footer"></slot>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>

<style scoped>
/* 오버레이 스타일링 */
:deep(.dialog-overlay) {
  backdrop-filter: blur(8px);
  background-color: rgba(0, 0, 0, 0.75);
}

/* 모달 콘텐츠 스타일링 */
:deep(.dialog-content) {
  transition: all 0.3s ease;
  background-color: transparent !important;
}

/* 별 배경 스타일링 */
:deep(.dialog-content > div:first-child) {
  z-index: 0;
}

/* bg-background 클래스 덮어쓰기 */
:deep(.bg-background) {
  background-color: transparent !important;
}

/* 다이얼로그 콘텐츠의 모든 배경 요소 */
:deep([data-slot='dialog-content']) {
  background-color: transparent !important;
  background: transparent !important;
}

/* 다른 배경 관련 클래스들 덮어쓰기 */
:deep([class*='bg-']) {
  background-color: transparent !important;
}
:deep([style*='background']) {
  background-color: transparent !important;
}

/* 새로운 우주 배경 클래스 */
.space-bg {
  background-color: transparent !important;
  background-image: none !important;
}

/* 모달 안에 모든 배경 관련 요소 강제 투명화 */
:deep(.dialog-content *) {
  background-color: transparent;
}

/* 다이얼로그의 모든 자식 요소에 대한 강제 배경 제거 */
:deep([data-slot='dialog-content'] *[class*='bg-']) {
  background-color: transparent !important;
}

/* 상위 컨테이너 타겟팅 */
:deep(.fixed) {
  background-color: transparent !important;
}

/* tailwind 배경 관련 클래스 모두 덮어쓰기 */
:deep([class*='bg-']) {
  background-color: transparent !important;
  background: transparent !important;
}

/* 최상위 모달 컨테이너 */
:deep(.stellatrip-modal) {
  --tw-bg-opacity: 0 !important;
  background-color: transparent !important;
}

/* 다이얼로그 루트 요소에 직접 적용 */
:deep([data-slot='dialog']) {
  background-color: transparent !important;
}

/* 호버 시 강조 효과 */
:deep(.dialog-content:hover) {
  border-color: rgba(168, 85, 247, 0.5);
  box-shadow: 0 0 30px rgba(147, 51, 234, 0.4);
  transform: scale(1.01);
}

/* 애니메이션 효과 강화 */
:deep([data-state='open']) {
  animation-duration: 0.3s;
}

:deep([data-state='closed']) {
  animation-duration: 0.2s;
}

/* 애니메이션 클래스 정의 */
.data-\[state\=open\]\:animate-in {
  animation: fadeIn 0.3s ease forwards;
}

.data-\[state\=closed\]\:animate-out {
  animation: fadeOut 0.2s ease forwards;
}

.data-\[state\=open\]\:fade-in-0 {
  opacity: 0;
  animation: fadeIn 0.3s ease forwards;
}

.data-\[state\=closed\]\:fade-out-0 {
  opacity: 1;
  animation: fadeOut 0.2s ease forwards;
}

.data-\[state\=open\]\:zoom-in-95 {
  transform: scale(0.95);
  animation: zoomIn 0.3s ease forwards;
}

.data-\[state\=closed\]\:zoom-out-95 {
  transform: scale(1);
  animation: zoomOut 0.2s ease forwards;
}

.data-\[state\=open\]\:slide-in-from-top-\[2\%\] {
  transform: translateY(-2%);
  animation: slideDown 0.3s ease forwards;
}

.data-\[state\=closed\]\:slide-out-to-top-\[2\%\] {
  transform: translateY(0);
  animation: slideUp 0.2s ease forwards;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes fadeOut {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}

@keyframes zoomIn {
  from {
    transform: scale(0.95);
  }
  to {
    transform: scale(1);
  }
}

@keyframes zoomOut {
  from {
    transform: scale(1);
  }
  to {
    transform: scale(0.95);
  }
}

@keyframes slideDown {
  from {
    transform: translateY(-2%);
  }
  to {
    transform: translateY(0);
  }
}

@keyframes slideUp {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(-2%);
  }
}

@keyframes twinkle {
  0% {
    opacity: 0.2;
  }
  100% {
    opacity: 1;
  }
}
</style>
