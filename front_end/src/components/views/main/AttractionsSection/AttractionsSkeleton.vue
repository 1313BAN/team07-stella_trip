<template>
  <RowCardListContainer :title="title">
    <CardSkeleton v-for="i in 5" :key="i" />
  </RowCardListContainer>
</template>

<script setup lang="ts">
import RowCardListContainer from '@/components/common/RowCardListContainer/RowCardListContainer.vue';
import CardSkeleton from '@/components/skeleton/CardSkeleton/CardSkeleton.vue';

interface Props {
  title: string;
}

defineProps<Props>();
</script>
