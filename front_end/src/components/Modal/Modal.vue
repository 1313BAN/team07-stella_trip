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
import { computed, ref, watch, onMounted } from 'vue';

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

// Generate random stars for background
const backgroundStars = Array.from({ length: 30 }, () => ({
  x: Math.random() * 100,
  y: Math.random() * 100,
  r: Math.random() * 0.8 + 0.2,
  brightness: Math.random() * 0.4 + 0.2,
  duration: Math.random() * 2 + 2,
}));

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

// 모달이 열릴 때 배경 강제 처리
const onModalOpen = () => {
  // 짧은 지연 후 백그라운드 강제 적용 (컴포넌트가 DOM에 마운트된 후)
  setTimeout(() => {
    const dialogElements = document.querySelectorAll('[data-slot="dialog-content"]');
    dialogElements.forEach(el => {
      el.classList.add('space-bg');
      el.style.backgroundColor = 'transparent';
      el.style.background = `
        linear-gradient(to bottom, 
          rgba(15, 23, 42, 0.9) 0%,
          rgba(88, 28, 135, 0.8) 50%,
          rgba(30, 27, 75, 0.9) 100%
        ),
        linear-gradient(135deg, 
          rgba(79, 70, 229, 0.15) 0%,
          rgba(139, 92, 246, 0.08) 20%,
          rgba(109, 40, 217, 0.1) 40%,
          rgba(67, 56, 202, 0.12) 60%,
          rgba(139, 92, 246, 0.1) 80%,
          rgba(79, 70, 229, 0.15) 100%
        )
      `;
      
      // 백그라운드 클래스를 가진 모든 하위 요소 처리
      el.querySelectorAll('[class*="bg-"]').forEach(bgEl => {
        bgEl.style.backgroundColor = 'transparent';
        bgEl.classList.add('space-bg');
      });
    });
  }, 50);
};
</script>

<template>  <Dialog v-model:open="isOpen" class="stellatrip-modal">
    <DialogContent
      class="dialog-content space-bg !bg-transparent"
      style="background-color: transparent !important"
      :style="{
        maxWidth: props.maxWidth,
        borderRadius: '1rem',
        border: '1px solid rgba(255, 255, 255, 0.2)',
        padding: '1.5rem',
        backdropFilter: 'blur(8px)',
        borderColor: 'rgba(255, 255, 255, 0.2)',
        backgroundColor: 'transparent !important',
        background: `
          linear-gradient(to bottom, 
            rgba(15, 23, 42, 0.9) 0%,
            rgba(88, 28, 135, 0.8) 50%,
            rgba(30, 27, 75, 0.9) 100%
          ),
          linear-gradient(135deg, 
            rgba(79, 70, 229, 0.15) 0%,
            rgba(139, 92, 246, 0.08) 20%,
            rgba(109, 40, 217, 0.1) 40%,
            rgba(67, 56, 202, 0.12) 60%,
            rgba(139, 92, 246, 0.1) 80%,
            rgba(79, 70, 229, 0.15) 100%
          )
        `,
        boxShadow: '0 0 40px rgba(139, 92, 246, 0.25), inset 0 0 60px rgba(109, 40, 217, 0.1)',
        position: 'relative',
        overflow: 'hidden',
      }"
    >
      <!-- Stars background -->
      <div style="position: absolute; inset: 0; opacity: 0.7; pointer-events: none">
        <svg
          width="100%"
          height="100%"
          viewBox="0 0 100 100"
          preserveAspectRatio="none"
          style="position: absolute; inset: 0"
        >
          <circle
            v-for="(star, index) in backgroundStars"
            :key="index"
            :cx="star.x"
            :cy="star.y"
            :r="star.r"
            :style="{
              fill: 'white',
              opacity: star.brightness,
              animation: `twinkle ${star.duration}s ease-in-out infinite alternate`,
            }"
          />
        </svg>
      </div>
      <!-- 닫기 버튼 -->
      <button
        v-if="showCloseButton"
        aria-label="모달 닫기"
        :style="{
          position: 'absolute',
          top: '1rem',
          right: '1rem',
          height: '1.75rem',
          width: '1.75rem',
          borderRadius: '9999px',
          padding: '0.25rem',
          border: 'none',
          transition: 'all 0.2s',
          backgroundColor: 'rgba(255, 255, 255, 0.1)',
          color: 'rgba(255, 255, 255, 0.7)',
          cursor: 'pointer',
        }"
        @click="closeModal"
      >
        <X style="height: 100%; width: 100%" />
      </button>

      <!-- 제목과 설명 -->
      <DialogHeader v-if="title || description">
        <DialogTitle
          v-if="title"
          :style="{
            fontSize: '1.25rem',
            fontWeight: '600',
            color: 'white',
          }"
        >
          {{ title }}
        </DialogTitle>
        <DialogDescription
          v-if="description"
          :style="{
            fontSize: '0.875rem',
            color: 'rgb(209, 213, 219)',
          }"
        >
          {{ description }}
        </DialogDescription>
      </DialogHeader>

      <!-- 컨텐츠 영역 -->
      <div style="padding-top: 0.5rem; padding-bottom: 0.5rem; color: white">
        <slot></slot>
      </div>

      <!-- 푸터 영역 -->
      <DialogFooter
        v-if="$slots.footer"
        style="
          margin-top: 1rem;
          display: flex;
          align-items: center;
          justify-content: flex-end;
          gap: 0.75rem;
        "
      >
        <slot name="footer"></slot>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>

<style scoped>
/* 오버레이 스타일링 */
:deep(.dialog-overlay) {
  backdrop-filter: blur(4px);
  background-color: rgba(0, 0, 0, 0.7);
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
  box-shadow: 0 0 30px rgba(168, 85, 247, 0.3);
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
