<script setup lang="ts">
import { ref, watch } from 'vue';
import PlanCard from '@/components/card/PlanCard/PlanCard.vue';
import GridCardListContainer from '@/components/common/GridCardListContainer/GridCardListContainer.vue';
import { getPlans, type PlansParams, type Plan, type Tag } from '@/services/api/domains/plan';

const props = defineProps<{ filter: PlansParams }>();
const emit = defineEmits<{
  (e: 'cardClick', plan: Plan): void;
  (e: 'likeClick', plan: Plan): void;
  (e: 'tagClick', tag: Tag): void;
}>();
const plans = ref<Plan[]>([]);

const fetchPlans = async () => {
  const response = await getPlans(props.filter);
  plans.value = response.content;
};

watch(props.filter, () => {
  fetchPlans();
});

await fetchPlans();

const handleCardClick = (plan: Plan) => {
  emit('cardClick', plan);
};

const handleLikeClick = (plan: Plan) => {
  emit('likeClick', plan);
};

const handleTagClick = (tag: Tag) => {
  emit('tagClick', tag);
};
</script>

<template>
  <template v-if="plans.length === 0">
    <div class="h-full w-full pt-16 text-center">
      <p class="text-lg text-gray-200">검색 결과가 없습니다.</p>
    </div>
  </template>
  <template v-else>
    <GridCardListContainer :showTitle="false">
      <PlanCard
        v-for="plan in plans"
        :key="plan.planId"
        :plan="plan"
        @cardClick="handleCardClick"
        @likeClick="handleLikeClick"
        @tagClick="handleTagClick"
      />
    </GridCardListContainer>
  </template>
</template>
