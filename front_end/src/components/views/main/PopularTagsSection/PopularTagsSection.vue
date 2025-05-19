<template>
  <GridCardListContainer title="인기 여행 태그">
    <TagCard v-for="tag in tags" :key="tag.tagId" :label="tag.name" :labelCount="tag.count" />
  </GridCardListContainer>
</template>

<script setup lang="ts">
import GridCardListContainer from '@/components/GridCardListContainer/GridCardListContainer.vue';
import TagCard from '@/components/TagCard/TagCard.vue';
import { getPopularTags, type Tag } from '@/services/api/domains/plan';

const tags: Tag[] = await getPopularTags();
</script>
