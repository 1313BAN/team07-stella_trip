<script setup lang="ts">
import { ref } from 'vue';
import RowCardListContainer from '@/components/common/RowCardListContainer/RowCardListContainer.vue';
import AttractionCard from '@/components/card/AttractionCard/AttractionCard.vue';
import { getAttractions, type Attraction } from '@/services/api/domains/attraction';
import type { AttractionsParams } from '@/services/api/domains/attraction';

interface Props {
  title: string;
  apiParams?: AttractionsParams;
  showMoreButton?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  apiParams: () => ({ page: 1, size: 10 }),
  showMoreButton: true,
});

const emit = defineEmits<{
  (e: 'cardClick', attraction: Attraction): void;
  (e: 'likeClick', attraction: Attraction): void;
  (e: 'tagClick', contentType: number): void;
  (e: 'moreClick'): void;
}>();

const attractions = ref<Attraction[]>([]);
const response = await getAttractions(props.apiParams);
attractions.value = response.content;
</script>

<template>
  <RowCardListContainer
    :title="title"
    :showMoreButton="showMoreButton"
    @moreClick="emit('moreClick')"
  >
    <AttractionCard
      v-for="attraction in attractions"
      :key="attraction.attractionId"
      :attraction="attraction"
      :showRating="true"
      @cardClick="emit('cardClick', attraction)"
      @likeClick="emit('likeClick', attraction)"
      @tagClick="emit('tagClick', attraction.contentType)"
    />
  </RowCardListContainer>
</template>
