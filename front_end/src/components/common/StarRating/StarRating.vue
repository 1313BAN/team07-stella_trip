<script setup lang="ts">
import { Star, StarHalf } from 'lucide-vue-next';
import { getStarData } from '@/utils/util';

interface Props {
  rating: number;
}

const props = defineProps<Props>();

const stars = getStarData(props.rating);
</script>

<template>
  <div class="flex w-fit items-center gap-2 rounded-full bg-black/50 px-3 py-2 backdrop-blur-sm">
    <div class="flex items-center">
      <Star
        v-for="i in stars.full"
        :key="`full-${i}`"
        class="h-4 w-4 fill-yellow-400 text-yellow-400"
      />
      <StarHalf v-if="stars.half" class="h-4 w-4 fill-yellow-400 text-yellow-400" />
      <Star v-for="i in stars.empty" :key="`empty-${i}`" class="h-4 w-4 text-gray-400" />
    </div>
    <span class="text-sm font-semibold text-white">
      {{ rating.toFixed(1) }}
    </span>
  </div>
</template>
