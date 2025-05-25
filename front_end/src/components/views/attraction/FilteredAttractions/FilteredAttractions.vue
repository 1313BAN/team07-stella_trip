<script setup lang="ts">
import AttractionCard from '@/components/card/AttractionCard/AttractionCard.vue';
import type { AttractionsParams, Attraction } from '@/services/api/domains/attraction/types';
import { ContentTypeId } from '@/constants/constant';
import GridCardListContainer from '@/components/common/GridCardListContainer/GridCardListContainer.vue';

interface Props {
  apiParams?: AttractionsParams;
  attractions?: Attraction[];
  showAddButton?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  apiParams: () => ({ page: 1, size: 10 }),
  attractions: () => [],
  showAddButton: false,
});

const emit = defineEmits<{
  (e: 'cardClick', attraction: Attraction): void;
  (e: 'likeClick', attraction: Attraction): void;
  (e: 'tagClick', contentType: number): void;
  (e: 'filteredSearch', filters: Attraction[]): void;
}>();

const handleCardClick = (attraction: Attraction) => {
  emit('cardClick', attraction);
};

const handleLikeClick = (attraction: Attraction) => {
  emit('likeClick', attraction);
  // if (attraction.isLiked) {
  //   attraction.isLiked = false;
  //   attraction.likeCount = attraction.likeCount == 0 ? 0 : attraction.likeCount - 1;
  // } else {
  //   attraction.isLiked = true;
  //   attraction.likeCount += 1;
  // }
};

const handleTagClick = (contentType: ContentTypeId) => {
  emit('tagClick', contentType);
};
</script>

<template>
  <template v-if="props.attractions.length === 0">
    <div class="h-full w-full pt-16 text-center">
      <p class="text-lg text-gray-200">검색 결과가 없습니다.</p>
    </div>
  </template>
  <template v-else>
    <GridCardListContainer :showTitle="false">
      <AttractionCard
        v-for="attraction in props.attractions"
        :key="attraction.attractionId"
        :attraction="attraction"
        :showRating="true"
        @cardClick="handleCardClick(attraction)"
        @likeClick="handleLikeClick(attraction)"
        @tagClick="handleTagClick(attraction.contentType)"
      />
    </GridCardListContainer>
  </template>
</template>
