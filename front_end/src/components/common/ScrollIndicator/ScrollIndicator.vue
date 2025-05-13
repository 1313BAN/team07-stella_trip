<script setup lang="ts">
import { ChevronDown } from 'lucide-vue-next';

interface Props {
  label?: string;
}

const props = withDefaults(defineProps<Props>(), {
  label: '스크롤하여 더 보기',
});

const emit = defineEmits<{
  click: [];
}>();

const handleClick = () => {
  emit('click');
};
</script>

<template>
  <div
    class="group absolute bottom-8 left-1/2 -translate-x-1/2 transform cursor-pointer"
    @click="handleClick"
  >
    <div
      class="flex flex-col items-center text-purple-300/70 transition-colors duration-300 hover:text-purple-300"
    >
      <span class="mb-2 animate-pulse text-sm font-medium">{{ props.label }}</span>
      <div
        class="flex h-10 w-6 justify-center rounded-full border-2 border-purple-400/50 transition-colors duration-300 group-hover:border-purple-400/80"
      >
        <div class="mt-2 h-3 w-1 animate-bounce rounded-full bg-purple-400"></div>
      </div>
      <ChevronDown class="mt-2 h-6 w-6 animate-bounce delay-1000" />
    </div>
  </div>
</template>
