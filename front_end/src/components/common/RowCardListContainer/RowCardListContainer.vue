<script setup lang="ts">
import { ChevronRight } from 'lucide-vue-next';

interface Props {
  title: string;
  showMoreButton?: boolean;
}

withDefaults(defineProps<Props>(), { showMoreButton: true });

const emit = defineEmits<{
  moreClick: [];
}>();

const handleMoreClick = () => {
  emit('moreClick');
};
</script>

<template>
  <div>
    <!-- 카테고리 헤더 -->
    <div class="flex items-center justify-between px-4">
      <div
        class="flex cursor-pointer items-center gap-1"
        role="button"
        tabindex="0"
        @click="handleMoreClick"
      >
        <h3 class="text-2xl font-bold text-white">{{ title }}</h3>
        <ChevronRight class="h-5 w-5 text-purple-400" />
      </div>
      <button
        v-if="showMoreButton"
        class="text-sm text-purple-400 hover:text-purple-300"
        @click="handleMoreClick"
      >
        더보기
      </button>
    </div>

    <!-- 카드 리스트 (가로 스크롤) -->
    <div class="no-scrollbar flex gap-4 overflow-x-scroll px-4 pt-4 pb-8">
      <slot></slot>
    </div>
  </div>
</template>

<style scoped>
.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.no-scrollbar::-webkit-scrollbar {
  display: none;
}
</style>
