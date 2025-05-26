<template>
  <section class="relative min-h-screen">
    <div class="container mx-auto">
      <!-- 인기 태그 섹션 -->
      <AsyncContainer :loadingComponent="PopularTagsSkeleton" :errorComponent="PopularTagsError">
        <PopularTagsSection />
      </AsyncContainer>

      <!-- 인기 명소 섹션 -->
      <AsyncContainer
        v-for="Id in contentTypeList"
        :key="Id.value"
        :loadingComponent="AttractionsSkeleton"
        :errorComponent="AttractionsError"
        :title="`인기 ${contentTypeNameKR[Id.value as keyof typeof contentTypeNameKR]}`"
      >
        <AttractionsSection
          :title="`인기 ${contentTypeNameKR[Id.value as keyof typeof contentTypeNameKR]}`"
          :contentType="Number(Id.value)"
          @cardClick="handleAttractionCardClick"
          @likeClick="handleAttractionLikeClick"
          @tagClick="handleAttractionTagClick"
          @moreClick="() => handleMoreClick(12)"
        />
      </AsyncContainer>
    </div>
  </section>
</template>

<script setup lang="ts">
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';

import { type Attraction } from '@/services/api/domains/attraction/types';
import { PopularTagsError, PopularTagsSection, PopularTagsSkeleton } from './PopularTagsSection';
import { AttractionsError, AttractionsSection, AttractionsSkeleton } from './AttractionsSection';
import { contentTypeNameKR, contentTypeList } from '@/constants/constant';
import router from '@/router';
import { ROUTES } from '@/router/routes';
import { deleteAttractionLike, postAttractionLike } from '@/services/api/domains/attraction';
import { toast } from 'vue-sonner';

const handleAttractionCardClick = (attraction: Attraction) => {
  router.push({
    name: ROUTES.ATTRACTION.name,
    query: { keyword: attraction.name, selectedAttractionId: attraction.attractionId.toString() },
  });
};

const handleAttractionLikeClick = (attraction: Attraction) => {
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

const handleAttractionTagClick = (contentType: number) => {
  router.push({
    name: ROUTES.ATTRACTION.name,
    query: { contentTypeIds: [contentType.toString()] },
  });
};

const handleMoreClick = (contentType: number) => {
  console.log('더보기 클릭:', contentType);
};
</script>
