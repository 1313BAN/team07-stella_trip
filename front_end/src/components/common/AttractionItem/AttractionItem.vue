<template>
  <div class="flex gap-3 rounded-lg p-2 transition-all duration-200 hover:bg-white/5">
    <!-- 드래그 핸들 (수정 모드일 때만 표시) -->
    <div v-if="planStore.isModifying" class="flex items-center">
      <div class="drag-handle mr-1 cursor-grab active:cursor-grabbing">
        <svg
          class="h-4 w-4 text-gray-500 hover:text-purple-400"
          fill="currentColor"
          viewBox="0 0 16 16"
        >
          <path
            d="M7 2a1 1 0 1 1-2 0 1 1 0 0 1 2 0zM7 5a1 1 0 1 1-2 0 1 1 0 0 1 2 0zM7 8a1 1 0 1 1-2 0 1 1 0 0 1 2 0zM7 11a1 1 0 1 1-2 0 1 1 0 0 1 2 0zM11 2a1 1 0 1 1-2 0 1 1 0 0 1 2 0zM11 5a1 1 0 1 1-2 0 1 1 0 0 1 2 0zM11 8a1 1 0 1 1-2 0 1 1 0 0 1 2 0zM11 11a1 1 0 1 1-2 0 1 1 0 0 1 2 0z"
          />
        </svg>
      </div>
    </div>

    <div
      class="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-full bg-purple-900/50 text-purple-200"
    >
      {{ order }}
    </div>

    <div class="flex-1">
      <div class="mb-1 flex items-center justify-between">
        <h5 class="font-medium text-white">{{ name }}</h5>
        <div class="flex items-center gap-2">
          <span class="text-sm text-purple-300">
            {{ formattedTime }}
          </span>
          <!-- 삭제 버튼 (수정 모드일 때만 표시) -->
          <button
            v-if="planStore.isModifying"
            class="flex h-6 w-6 items-center justify-center rounded-full bg-red-900/50 text-red-300 transition-colors hover:bg-red-800/70 hover:text-red-200"
            @click="handleDelete"
          >
            <X class="h-3 w-3" />
          </button>
        </div>
      </div>

      <div class="flex items-start gap-1 text-xs text-gray-400">
        <MapPin class="mt-0.5 h-3 w-3 flex-shrink-0 text-purple-400" />
        <span class="line-clamp-1">{{ address }}</span>
      </div>

      <div v-if="memo" class="mt-1 text-sm text-gray-300">
        <span class="line-clamp-2">{{ memo }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { MapPin, X } from 'lucide-vue-next';
import { usePlanStore } from '@/stores/plan';

const planStore = usePlanStore();

const props = defineProps<{
  order: number;
  name: string;
  visitTime?: string;
  address: string;
  memo?: string;
}>();

const emit = defineEmits<{
  delete: [];
}>();

/**
 * 시간 문자열을 HH:MM 형식으로 포맷팅합니다.
 * @returns 포맷된 시간 문자열, visitTime이 없으면 빈 문자열
 */
const formattedTime = computed(() => {
  if (!props.visitTime) return '';
  const [hours, minutes] = props.visitTime.split(':');
  return `${hours}:${minutes}`;
});

const handleDelete = () => {
  emit('delete');
};
</script>

<style scoped>
.drag-handle:active {
  @apply cursor-grabbing;
}
</style>
