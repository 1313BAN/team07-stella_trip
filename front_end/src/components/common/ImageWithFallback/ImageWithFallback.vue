<script setup lang="ts">
import { ref } from 'vue';
import { ImageIcon } from 'lucide-vue-next';
import { cn } from '@/lib/shadcn/utils';

const props = defineProps<{
  src: string;
  alt: string;
  class?: string;
}>();

const imageLoaded = ref(false);
const imageError = ref(false);

const handleLoad = () => {
  imageLoaded.value = true;
};

const handleError = () => {
  imageError.value = true;
};
</script>

<template>
  <div class="relative h-full w-full">
    <!-- 로딩 중 -->
    <div
      v-if="!imageLoaded && !imageError"
      class="absolute inset-0 z-10 flex animate-pulse items-center justify-center bg-gray-900"
    >
      <ImageIcon class="h-12 w-12 text-gray-500" />
    </div>

    <!-- 에러 시 -->
    <div
      v-if="imageError"
      class="absolute inset-0 z-10 flex items-center justify-center bg-gray-900"
    >
      <ImageIcon class="h-12 w-12 text-red-400" />
    </div>

    <!-- 이미지 -->
    <img
      :src="src"
      :alt="alt"
      :class="
        cn(
          'h-full w-full object-cover transition-transform duration-500',
          { 'opacity-0': !imageLoaded },
          props.class
        )
      "
      @load="handleLoad"
      @error="handleError"
    />
  </div>
</template>
