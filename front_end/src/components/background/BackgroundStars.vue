<script setup lang="ts">
import { ref, onMounted } from 'vue';
import type { BackgroundStarsProps } from './types';

interface Star {
  id: number;
  x: number;
  y: number;
  size: number;
  delay: number;
  opacity: number;
}

const props = withDefaults(defineProps<BackgroundStarsProps>(), {
  starCount: 200,
  maxSize: 3,
  minSize: 1,
});

const stars = ref<Star[]>([]);

const createStars = () => {
  const newStars: Star[] = [];

  for (let i = 0; i < props.starCount; i++) {
    newStars.push({
      id: i,
      x: Math.random() * 100,
      y: Math.random() * 100,
      size: Math.random() * (props.maxSize - props.minSize) + props.minSize,
      delay: Math.random() * 5,
      opacity: Math.random() * 0.8 + 0.2,
    });
  }

  stars.value = newStars;
};

onMounted(() => {
  createStars();
});
</script>

<template>
  <div
    v-for="star in stars"
    :key="star.id"
    class="absolute animate-pulse rounded-full bg-white"
    :style="{
      left: `${star.x}%`,
      top: `${star.y}%`,
      width: `${star.size}px`,
      height: `${star.size}px`,
      animationDelay: `${star.delay}s`,
      opacity: star.opacity,
    }"
  ></div>
</template>
