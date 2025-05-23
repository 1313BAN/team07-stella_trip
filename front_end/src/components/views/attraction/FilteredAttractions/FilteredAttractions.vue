<script setup lang="ts">
import { ref, watch } from 'vue';
import AttractionCard from '@/components/card/AttractionCard/AttractionCard.vue';
import { getAttractions, type Attraction } from '@/services/api/domains/attraction';
import type { AttractionsParams } from '@/services/api/domains/attraction';
import GridCardListContainer from '@/components/common/GridCardListContainer/GridCardListContainer.vue';

interface Props {
  apiParams?: AttractionsParams;
}

const props = withDefaults(defineProps<Props>(), {
  apiParams: () => ({ page: 1, size: 10 }),
});

const emit = defineEmits<{
  (e: 'cardClick', attraction: Attraction): void;
  (e: 'likeClick', attraction: Attraction): void;
  (e: 'tagClick', contentType: number): void;
}>();

const attractions = ref<Attraction[]>([]);

const fetchAttractions = async () => {
  const response = await getAttractions(props.apiParams);
  attractions.value = response.content;
};

await fetchAttractions();

watch(
  () => props.apiParams,
  () => fetchAttractions(),
  { deep: true }
);
</script>

<template>
  <template v-if="attractions.length === 0">
    <div class="h-full w-full pt-16 text-center">
      <p class="text-lg text-gray-200">검색 결과가 없습니다.</p>
    </div>
  </template>
  <template v-else>
    <GridCardListContainer :showTitle="false">
      <AttractionCard
        v-for="attraction in attractions"
        :key="attraction.attractionId"
        :attraction="attraction"
        :showRating="true"
        @cardClick="emit('cardClick', attraction)"
        @likeClick="emit('likeClick', attraction)"
        @tagClick="emit('tagClick', attraction.contentType)"
      />
    </GridCardListContainer>
  </template>
</template>
