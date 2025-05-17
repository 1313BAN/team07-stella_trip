<script setup lang="ts">
interface Props {
  label: string;
  labelCount: number;
}

defineProps<Props>();

const emit = defineEmits<{
  click: [];
}>();

const handleClick = () => {
  emit('click');
};
</script>

<template>
  <div
    class="flex min-w-72 cursor-pointer flex-col justify-center rounded-2xl bg-indigo-950 p-4 transition-all duration-300 hover:-translate-y-2 hover:bg-indigo-900/60 hover:shadow-md hover:shadow-purple-500/20"
    role="button"
    tabindex="0"
    @click="handleClick"
  >
    <p class="text-xl font-medium text-purple-300"># {{ label }}</p>
    <p class="mt-1 text-slate-300">{{ labelCount.toLocaleString() }} 여행</p>
  </div>
</template>
