<template>
  <GridCardListContainer title="인기 여행 태그">
    <TagCardSkeleton v-for="i in 8" :key="i" />
  </GridCardListContainer>
</template>

<script setup lang="ts">
import GridCardListContainer from '@/components/common/GridCardListContainer/GridCardListContainer.vue';
import TagCardSkeleton from '@/components/skeleton/TagCardSkeleton/TagCardSkeleton.vue';
</script>
