<script setup lang="ts">
import { Star, StarHalf } from 'lucide-vue-next';
import { getStarData } from '@/utils/util';
import { computed } from 'vue';

interface Props {
  rating: number;
}

const props = defineProps<Props>();

const stars = getStarData(props.rating);
const formattedRating = computed(() => props.rating.toFixed(1));
const ariaLabel = computed(() => `평점 ${formattedRating.value}점 (5점 만점)`);
</script>

<template>
  <div
    class="flex w-fit items-center gap-2 rounded-full bg-black/50 px-3 py-2 backdrop-blur-sm"
    :ariaLable="ariaLabel"
  >
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
      {{ formattedRating }}
    </span>
  </div>
</template>
