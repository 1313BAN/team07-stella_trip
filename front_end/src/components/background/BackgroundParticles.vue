<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import type { BackgroundParticlesProps } from './types';

interface Particle {
  id: number;
  x: number;
  y: number;
  delay: number;
  duration: number;
}

const props = withDefaults(defineProps<BackgroundParticlesProps>(), {
  particleCount: 20,
  baseAnimationDuration: 3,
  maxDelay: 3,
  particleClass: 'bg-purple-300/30',
});

const particles = ref<Particle[]>([]);

const createParticles = () => {
  const newParticles: Particle[] = [];

  for (let i = 0; i < props.particleCount; i++) {
    newParticles.push({
      id: i,
      x: Math.random() * 100,
      y: Math.random() * 100,
      delay: Math.random() * props.maxDelay,
      duration: props.baseAnimationDuration + Math.random() * 2,
    });
  }

  particles.value = newParticles;
};

onMounted(() => {
  createParticles();
});

watch(
  () => [props.particleCount, props.maxDelay, props.baseAnimationDuration],
  () => {
    createParticles();
  }
);
</script>

<template>
  <div
    v-for="particle in particles"
    :key="particle.id"
    class="absolute h-1 w-1 animate-bounce rounded-full"
    :class="props.particleClass"
    :style="{
      left: `${particle.x}%`,
      top: `${particle.y}%`,
      animationDelay: `${particle.delay}s`,
      animationDuration: `${particle.duration}s`,
    }"
  ></div>
</template>
