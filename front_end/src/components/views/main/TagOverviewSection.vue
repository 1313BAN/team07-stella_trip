<template>
  <section class="relative min-h-screen">
    <div class="container mx-auto">
      <GridCardListContainer title="인기 여행 태그">
        <TagCard
          v-for="tag in popularTags"
          :key="tag.id"
          :label="tag.label"
          :labelCount="tag.count"
        ></TagCard>
      </GridCardListContainer>
      <RowCardListContainer
        title="힐링"
        moreLink="/categories/healing"
        @moreClick="() => handleMoreClick('힐링')"
      >
        <PlanCard
          v-for="plan in healingPlans"
          :key="plan.planId"
          :plan="plan"
          @cardClick="handlePlanCardClick"
          @likeClick="handlePlanLikeClick"
          @tagClick="handlePlanTagClick"
        />
      </RowCardListContainer>

      <RowCardListContainer
        title="인기 관광지"
        moreLink="/attractions/popular"
        @moreClick="() => handleMoreClick('인기 관광지')"
      >
        <AttractionCard
          v-for="attraction in popularAttractions"
          :key="attraction.attractionId"
          :attraction="attraction"
          :showRating="true"
          @cardClick="handleAttractionCardClick"
          @likeClick="handleAttractionLikeClick"
          @tagClick="handleAttractionTagClick"
        />
      </RowCardListContainer>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import RowCardListContainer from '@/components/common/RowCardListContainer/RowCardListContainer.vue';
import PlanCard from '@/components/PlanCard/PlanCard.vue';
import AttractionCard from '@/components/AttractionCard/AttractionCard.vue';
import GridCardListContainer from '@/components/GridCardListContainer/GridCardListContainer.vue';
import TagCard from '@/components/TagCard/TagCard.vue';
import { getPlans, getPopularTags, type Plan } from '@/services/api/domains/plan';
import { getAttractions, type Attraction } from '@/services/api/domains/attraction';

// 데이터를 담을 ref 생성
const healingPlans = ref<Plan[]>([]);
const popularAttractions = ref<Attraction[]>([]);
const popularTags = ref<Tag[]>([]);

// 부모 컴포넌트에서 <Suspense>로 처리할 비동기 함수
const setupData = async () => {
  const [plansResponse, attractionsResponse, tagsResponse] = await Promise.all([
    getPlans({
      page: 1,
      size: 10,
      sort: 'RECENT',
    }),
    getAttractions({
      page: 1,
      size: 10,
    }),
    getPopularTags(),
  ]);

  healingPlans.value = plansResponse.content;
  popularAttractions.value = attractionsResponse.content;
  popularTags.value = tagsResponse;
};

setupData();

const handlePlanCardClick = (plan: Plan) => {
  console.log('여행 계획 카드 클릭:', plan.title);
};

const handlePlanLikeClick = (plan: Plan) => {
  console.log('여행 계획 좋아요 클릭:', plan.title);
};

const handlePlanTagClick = (tag: string) => {
  console.log('여행 계획 태그 클릭:', tag);
};

const handleAttractionCardClick = (attraction: Attraction) => {
  console.log('관광지 카드 클릭:', attraction.name);
};

const handleAttractionLikeClick = (attraction: Attraction) => {
  console.log('관광지 좋아요 클릭:', attraction.name);
};

const handleAttractionTagClick = (contentType: string) => {
  console.log('관광지 태그(컨텐츠 타입) 클릭:', contentType);
};

const handleMoreClick = (category: string) => {
  console.log('더보기 클릭:', category);
};
</script>
