<script setup lang="ts">
import StarBackground from './BackgroundStars.vue';
import FloatingParticles from './BackgroundParticles.vue';
import GradientBackground from './BackgroundGradient.vue';
import type { BackgroundProps } from './types';

const props = defineProps<BackgroundProps>();
</script>

<template>
  <div class="fixed inset-0">
    <GradientBackground
      :from="props.from"
      :via="props.via"
      :to="props.to"
      :direction="props.direction"
    />

    <StarBackground
      :star-count="props.starCount"
      :max-size="props.maxSize"
      :min-size="props.minSize"
    />

    <FloatingParticles
      :particle-count="props.particleCount"
      :base-animation-duration="props.baseAnimationDuration"
      :max-delay="props.maxDelay"
      :particle-class="props.particleClass"
    />
  </div>
</template>
