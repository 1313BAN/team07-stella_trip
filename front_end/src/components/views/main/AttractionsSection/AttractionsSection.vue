<script setup lang="ts">
import { ref } from 'vue';
import RowCardListContainer from '@/components/common/RowCardListContainer/RowCardListContainer.vue';
import AttractionCard from '@/components/card/AttractionCard/AttractionCard.vue';
import { getFeaturedAttractions } from '@/services/api/domains/attraction';
import { type Attraction } from '@/services/api/domains/attraction/types';
import { ContentTypeId } from '@/constants/constant';

interface Props {
  title: string;
  showMoreButton?: boolean;
  contentType?: number;
}

const props = withDefaults(defineProps<Props>(), {
  showMoreButton: true,
  contentType: ContentTypeId.ACCOMMODATION,
});

const emit = defineEmits<{
  (e: 'cardClick', attraction: Attraction): void;
  (e: 'likeClick', attraction: Attraction): void;
  (e: 'tagClick', contentType: number): void;
  (e: 'moreClick'): void;
}>();

const attractions = ref<Attraction[]>([]);

const fetchAttractions = async () => {
  const response = await getFeaturedAttractions(props.contentType);
  attractions.value = response.featuredAttractions;
};

await fetchAttractions();
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
