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
      :starCount="props.starCount"
      :maxSize="props.maxSize"
      :minSize="props.minSize"
    />

    <FloatingParticles
      :particleCount="props.particleCount"
      :baseAnimationDuration="props.baseAnimationDuration"
      :maxDelay="props.maxDelay"
      :particleClass="props.particleClass"
    />
  </div>
</template>
