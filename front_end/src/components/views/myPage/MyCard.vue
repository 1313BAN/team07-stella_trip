<script setup lang="ts">
import type { StellaResponse } from '@/services/api/domains/stella/types';
import { ref } from 'vue';

const stellaList = ref<StellaResponse[]>([]);
</script>

<template>
  <div class="mb-4">
    <template v-if="stellaList.length === 0">
      <div class="h-full w-full pt-16 text-center">
        <p class="text-lg text-gray-200">검색 결과가 없습니다.</p>
      </div>
    </template>
    <template v-else>
      <GridCardListContainer :showTitle="false"></GridCardListContainer>
    </template>
  </div>
</template>
