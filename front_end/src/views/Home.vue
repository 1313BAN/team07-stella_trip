<template>
  <HeroBackground />
  <main>
    <HeroSection />
    <IntroductionSection />
  </main>
</template>

<script setup lang="ts">
import HeroBackground from '@/components/background/HeroBackground.vue';
import HeroSection from '@/components/views/home/HeroSection.vue';
import IntroductionSection from '@/components/views/home/IntroductionSection.vue';
</script>
