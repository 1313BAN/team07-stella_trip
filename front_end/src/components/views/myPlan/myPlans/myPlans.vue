<script setup lang="ts">
import { ref, watch } from 'vue';
import PlanCard from '@/components/card/PlanCard/PlanCard.vue';
import GridCardListContainer from '@/components/common/GridCardListContainer/GridCardListContainer.vue';
import AddPlanCard from '@/components/card/AddPlanCard/AddPlanCard.vue';
import { type PlansParams, type Plan, type Tag, getMyPlans } from '@/services/api/domains/plan';

interface Props {
  apiParams?: PlansParams;
}

const props = withDefaults(defineProps<Props>(), {
  apiParams: () => ({ page: 1, size: 10 }),
});

const emit = defineEmits<{
  (e: 'cardClick', plan: Plan): void;
  (e: 'likeClick', plan: Plan): void;
  (e: 'tagClick', tag: Tag): void;
  (e: 'addPlanClick'): void;
}>();

const plans = ref<Plan[]>([]);

const fetchPlans = async () => {
  const response = await getMyPlans();
  plans.value = response.content;
};

await fetchPlans();

watch(
  () => props.apiParams,
  () => fetchPlans(),
  { deep: true }
);

const handleCardClick = (plan: Plan) => emit('cardClick', plan);
const handleLikeClick = (plan: Plan) => emit('likeClick', plan);
const handleTagClick = (tag: Tag) => emit('tagClick', tag);
const handleAddPlanClick = () => emit('addPlanClick');
</script>

<template>
  <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
    <GridCardListContainer title="나의 여행 계획">
      <!-- 여행 계획 추가 카드 -->
      <AddPlanCard @addPlanClick="handleAddPlanClick" />

      <!-- 여행 계획 카드 리스트 -->
      <PlanCard
        v-for="plan in plans"
        :key="plan.planId"
        :plan="plan"
        @cardClick="handleCardClick"
        @likeClick="handleLikeClick"
        @tagClick="handleTagClick"
      />
    </GridCardListContainer>
  </div>
</template>
