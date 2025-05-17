<script setup lang="ts">
import HeroBackground from '@/components/background/HeroBackground.vue';
import TagOverviewSection from '@/components/views/main/TagOverviewSection.vue';
import HeroSection from '@/components/views/main/HeroSection.vue';
</script>

<template>
  <HeroBackground />
  <div class="min-h-screen">
    <HeroSection />
    <TagOverviewSection />
  </div>
</template>
