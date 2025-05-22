<template>
  <div>
    <NavigationBar />
    <HeroBackground />
    <main class="relative min-h-screen pt-16">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import NavigationBar from '@/components/NavigationBar/NavigationBar.vue';
import { HeroBackground } from '@/components/background';
</script>
