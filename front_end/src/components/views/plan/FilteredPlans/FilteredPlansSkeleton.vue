<template>
  <GridCardListContainer>
    <CardSkeleton v-for="i in 10" :key="i" />
  </GridCardListContainer>
</template>

<script setup lang="ts">
import GridCardListContainer from '@/components/common/GridCardListContainer/GridCardListContainer.vue';
import CardSkeleton from '@/components/skeleton/CardSkeleton/CardSkeleton.vue';
</script>
