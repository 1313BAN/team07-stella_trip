<template>
  <GridCardListContainer title="인기 여행 태그" minItemWidth="18rem">
    <TagCard v-for="tag in tags" :key="tag.tagId" :label="tag.name" :labelCount="tag.count" />
  </GridCardListContainer>
</template>

<script setup lang="ts">
import GridCardListContainer from '@/components/common/GridCardListContainer/GridCardListContainer.vue';
import TagCard from '@/components/card/TagCard/TagCard.vue';
import { type FeaturedTags, type Tag } from '@/services/api/domains/plan';
import { getPopularTags } from '@/services/api/domains/attraction';

const featuredTages: FeaturedTags = await getPopularTags();
const tags: Tag[] = featuredTages.featuredTags;
</script>
