<template>
  <div class="relative h-72 w-full bg-indigo-950 p-4">
    <!-- 별자리가 있으면 표시, 없으면 기본 배경 -->
    <div v-if="stella" class="h-full w-full">
      <Constellation
        :nodes="stella.nodes"
        :edges="stella.edges"
        :backgroundStars="backgroundStars"
        :isHovered="false"
      />
    </div>
    <div
      v-else
      class="absolute inset-0 bg-gradient-to-br from-slate-900 via-purple-950 to-indigo-950 opacity-70"
    ></div>
    <div
      class="absolute inset-0 bg-gradient-to-t from-black/40 via-transparent to-transparent"
    ></div>
  </div>
</template>

<script setup lang="ts">
import Constellation from '@/components/common/Constellation/Constellation.vue';
import type { StellaData, BackgroundStar } from '@/services/api/domains/plan/types';

defineProps<{
  stella?: StellaData | null;
  backgroundStars: BackgroundStar[];
}>();
</script>
