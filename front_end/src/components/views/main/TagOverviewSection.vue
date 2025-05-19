<template>
  <section class="relative min-h-screen">
    <div class="container mx-auto">
      <!-- 인기 태그 섹션 -->
      <AsyncContainer :loadingComponent="PopularTagsSkeleton" :errorComponent="PopularTagsError">
        <PopularTagsSection />
      </AsyncContainer>

      <!-- 인기 명소 섹션 -->
      <AsyncContainer
        :loadingComponent="AttractionsSkeleton"
        :errorComponent="AttractionsError"
        title="인기 명소"
      >
        <AttractionsSection
          title="인기 명소"
          @cardClick="handleAttractionCardClick"
          @likeClick="handleAttractionLikeClick"
          @tagClick="handleAttractionTagClick"
          @moreClick="() => handleMoreClick(12)"
        />
      </AsyncContainer>

      <!-- 인기 문화 시설 섹션 -->
      <AsyncContainer
        :loadingComponent="AttractionsSkeleton"
        :errorComponent="AttractionsError"
        title="인기 문화 시설"
      >
        <AttractionsSection
          title="인기 문화 시설"
          :apiParams="{ contentTypeId: 14 }"
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
import AsyncContainer from '@/components/common/AsyncContainer/AsyncContainer.vue';

import { type Attraction } from '@/services/api/domains/attraction';
import { PopularTagsError, PopularTagsSection, PopularTagsSkeleton } from './PopularTagsSection';
import { AttractionsError, AttractionsSection, AttractionsSkeleton } from './AttractionsSection';

const handleAttractionCardClick = (attraction: Attraction) => {
  console.log('관광지 카드 클릭:', attraction.name);
};

const handleAttractionLikeClick = (attraction: Attraction) => {
  console.log('관광지 좋아요 클릭:', attraction.name);
};

const handleAttractionTagClick = (contentType: number) => {
  console.log('관광지 태그(컨텐츠 타입) 클릭:', contentType);
};

const handleMoreClick = (contentType: number) => {
  console.log('더보기 클릭:', contentType);
};
</script>
