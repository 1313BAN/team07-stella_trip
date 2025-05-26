<script setup lang="ts">
import AttractionCard from '@/components/card/AttractionCard/AttractionCard.vue';
import PlanCard from '@/components/card/PlanCard/PlanCard.vue';
import type { Attraction } from '@/services/api/domains/attraction/types';
import { ContentTypeId } from '@/constants/constant';
import GridCardListContainer from '@/components/common/GridCardListContainer/GridCardListContainer.vue';
import { onMounted, ref } from 'vue';
import type { Plan } from '@/services/api/domains/plan/types';
import { getLikedAttractions, getLikedPlans } from '@/services/api/domains/user';
import router from '@/router';
import { ROUTES } from '@/router/routes';
import type { Tag } from '@/services/api/domains/plan/types';
import { toast } from 'vue-sonner';
import { deleteAttractionLike, postAttractionLike } from '@/services/api/domains/attraction';
import { useAuthStore } from '@/stores/auth';

const attractions = ref<Attraction[]>([]);
const authStore = useAuthStore();

onMounted(async () => {
  // 초기화 로직: 예시로 빈 배열을 할당
  attractions.value = (await getLikedAttractions()).content;
});

const handleAttractionCardClick = (attraction: Attraction) => {
  router.push({
    path: ROUTES.ATTRACTION.path,
    query: {
      keyword: attraction.name,
      selectedAttractionId: attraction.attractionId.toString(),
    },
  });
};

const handleAttractionLikeClick = async (attraction: Attraction) => {
  if (!authStore.isAuthenticated) {
    toast('로그인이 필요합니다.');
    return;
  }

  if (attraction.isLiked) {
    attraction.isLiked = false;
    attraction.likeCount = attraction.likeCount == 0 ? 0 : attraction.likeCount - 1;
    deleteAttractionLike(attraction.attractionId)
      .then(() => {
        toast('관광지 좋아요 취소 성공');
      })
      .catch(() => {
        toast('관광지 좋아요 취소 실패');
        // 원래 상태로 롤백
        attraction.isLiked = !attraction.isLiked;
        attraction.likeCount = attraction.isLiked
          ? attraction.likeCount + 1
          : attraction.likeCount - 1;
      });
  } else {
    attraction.isLiked = true;
    attraction.likeCount += 1;
    // API 호출 및 상태 업데이트
    postAttractionLike(attraction.attractionId)
      .then(() => {
        toast('관광지 좋아요 상태 업데이트 성공');
      })
      .catch(() => {
        toast('관광지 좋아요 상태 업데이트 실패:');
        // 원래 상태로 롤백
        attraction.isLiked = !attraction.isLiked;
        attraction.likeCount = attraction.isLiked
          ? attraction.likeCount + 1
          : attraction.likeCount - 1;
      });
  }
};

const handleAttractionTagClick = (contentType: ContentTypeId) => {
  router.push({
    path: ROUTES.ATTRACTION.path,
    query: {
      contentTypeIds: [contentType],
    },
  });
};
</script>

<template>
  <div class="mb-4">
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
          @cardClick="handleAttractionCardClick(attraction)"
          @likeClick="handleAttractionLikeClick(attraction)"
          @tagClick="handleAttractionTagClick(attraction.contentType)"
        />
      </GridCardListContainer>
    </template>
  </div>
</template>
