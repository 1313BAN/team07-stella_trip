<template>
  <section class="relative min-h-screen">
    <HeroSectionContent
      :isLoaded="isLoaded"
      @startFree="onStartFree"
      @learnMore="onLearnMore"
      @scrollToNext="scrollToNext"
    />
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import HeroSectionContent from './HeroSectionContent.vue';
import { useRouter } from 'vue-router';
const router = useRouter();

const isLoaded = ref(false);

const onStartFree = () => {
  router.push('/main');
};

const onLearnMore = () => {
  console.log('더 알아보기 클릭');
};

const scrollToNext = () => {
  window.scrollTo({
    top: window.innerHeight,
    behavior: 'smooth',
  });
};

onMounted(() => {
  setTimeout(() => {
    isLoaded.value = true;
  }, 100);
});
</script>
