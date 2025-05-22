<template>
  <div
    class="flex h-32 flex-col items-center justify-center gap-2 rounded-lg border border-dashed border-white/20 p-4 text-center"
  >
    <ClipboardList class="h-8 w-8 text-purple-400/40" />
    <div class="text-sm text-gray-400">
      <p>아직 일정이 없습니다</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ClipboardList } from 'lucide-vue-next';
</script>
