<template>
  <section class="relative min-h-screen">
    <div class="container mx-auto">
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
        title="유럽"
        moreLink="/categories/europe"
        @moreClick="() => handleMoreClick('유럽')"
      >
        <PlanCard
          v-for="plan in europePlans"
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

      <RowCardListContainer
        title="서울 명소"
        moreLink="/attractions/seoul"
        @moreClick="() => handleMoreClick('서울 명소')"
      >
        <AttractionCard
          v-for="attraction in seoulAttractions"
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
import type { Plan, Attraction } from '@/types/type';

// 힐링 여행 계획 더미 데이터
const healingPlans = ref<Plan[]>([
  {
    planId: 1,
    title: '강원도 숲속 힐링 여행',
    description: '강원도 깊은 숲속에서 몸과 마음을 치유하는 5일간의 여행',
    startDate: '2025-06-10',
    endDate: '2025-06-14',
    tags: ['힐링', '자연', '숲', '강원도'],
    planWriters: [{ userId: 1, name: '김힐링' }],
    likeCount: 328,
    isLiked: false,
    constellation: {
      stars: [
        { x: 30, y: 20, r: 0.8, brightness: 0.7 },
        { x: 50, y: 30, r: 0.6, brightness: 0.5 },
        { x: 70, y: 20, r: 0.9, brightness: 0.8 },
        { x: 40, y: 50, r: 0.7, brightness: 0.6 },
        { x: 20, y: 40, r: 0.5, brightness: 0.4 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 4 },
        { from: 4, to: 0 },
      ],
    },
  },
  {
    planId: 2,
    title: '제주 바다 명상 여행',
    description: '제주 해안가에서 명상과 함께하는 힐링 휴양',
    startDate: '2025-07-05',
    endDate: '2025-07-08',
    tags: ['힐링', '명상', '제주', '바다'],
    planWriters: [{ userId: 2, name: '명상가이드' }],
    likeCount: 245,
    isLiked: true,
    constellation: {
      stars: [
        { x: 30, y: 30, r: 0.8, brightness: 0.7 },
        { x: 70, y: 30, r: 0.6, brightness: 0.5 },
        { x: 50, y: 50, r: 0.9, brightness: 0.8 },
        { x: 30, y: 70, r: 0.7, brightness: 0.6 },
        { x: 70, y: 70, r: 0.5, brightness: 0.4 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 4 },
        { from: 4, to: 0 },
      ],
    },
  },
  {
    planId: 3,
    title: '전남 템플스테이 체험',
    description: '전남 산사에서 템플스테이를 통해 마음의 평화 찾기',
    startDate: '2025-05-20',
    endDate: '2025-05-23',
    tags: ['힐링', '템플스테이', '명상', '전남'],
    planWriters: [{ userId: 3, name: '절에사는사람' }],
    likeCount: 187,
    isLiked: false,
    constellation: {
      stars: [
        { x: 20, y: 20, r: 0.8, brightness: 0.7 },
        { x: 60, y: 20, r: 0.6, brightness: 0.5 },
        { x: 80, y: 50, r: 0.9, brightness: 0.8 },
        { x: 40, y: 60, r: 0.7, brightness: 0.6 },
        { x: 20, y: 50, r: 0.5, brightness: 0.4 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 4 },
        { from: 4, to: 0 },
      ],
    },
  },
  {
    planId: 4,
    title: '울릉도 청정 자연 여행',
    description: '울릉도와 독도의 맑은 공기를 마시며 힐링하는 여행',
    startDate: '2025-08-12',
    endDate: '2025-08-16',
    tags: ['힐링', '울릉도', '독도', '자연'],
    planWriters: [{ userId: 4, name: '독도사랑' }],
    likeCount: 203,
    isLiked: false,
    constellation: {
      stars: [
        { x: 40, y: 20, r: 0.8, brightness: 0.7 },
        { x: 20, y: 40, r: 0.6, brightness: 0.5 },
        { x: 40, y: 60, r: 0.9, brightness: 0.8 },
        { x: 60, y: 40, r: 0.7, brightness: 0.6 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 0 },
      ],
    },
  },
]);

// 유럽 여행 계획 더미 데이터
const europePlans = ref<Plan[]>([
  {
    planId: 11,
    title: '동유럽 8일 완전정복',
    description: '체코, 오스트리아, 헝가리를 아우르는 동유럽 문화 탐방',
    startDate: '2025-09-10',
    endDate: '2025-09-17',
    tags: ['유럽', '동유럽', '문화', '역사'],
    planWriters: [{ userId: 8, name: '유럽탐험가' }],
    likeCount: 356,
    isLiked: false,
    constellation: {
      stars: [
        { x: 20, y: 20, r: 0.8, brightness: 0.7 },
        { x: 40, y: 40, r: 0.6, brightness: 0.5 },
        { x: 60, y: 20, r: 0.9, brightness: 0.8 },
        { x: 80, y: 40, r: 0.7, brightness: 0.6 },
        { x: 50, y: 60, r: 0.5, brightness: 0.4 },
        { x: 20, y: 60, r: 0.6, brightness: 0.5 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 4 },
        { from: 4, to: 5 },
        { from: 5, to: 0 },
      ],
    },
  },
  {
    planId: 12,
    title: '남부 이탈리아 로드트립',
    description: '나폴리부터 시칠리아까지 남부 이탈리아를 자동차로 여행',
    startDate: '2025-05-25',
    endDate: '2025-06-05',
    tags: ['유럽', '이탈리아', '로드트립', '미식'],
    planWriters: [{ userId: 9, name: '피자러버' }],
    likeCount: 284,
    isLiked: true,
    constellation: {
      stars: [
        { x: 30, y: 20, r: 0.8, brightness: 0.7 },
        { x: 20, y: 40, r: 0.6, brightness: 0.5 },
        { x: 40, y: 60, r: 0.9, brightness: 0.8 },
        { x: 60, y: 60, r: 0.7, brightness: 0.6 },
        { x: 80, y: 40, r: 0.5, brightness: 0.4 },
        { x: 70, y: 20, r: 0.6, brightness: 0.5 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 4 },
        { from: 4, to: 5 },
        { from: 5, to: 0 },
      ],
    },
  },
  {
    planId: 13,
    title: '북유럽 오로라 여행',
    description: '핀란드, 노르웨이에서 오로라를 감상하는 겨울 여행',
    startDate: '2026-01-15',
    endDate: '2026-01-22',
    tags: ['유럽', '북유럽', '오로라', '겨울'],
    planWriters: [{ userId: 10, name: '오로라헌터' }],
    likeCount: 412,
    isLiked: false,
    constellation: {
      stars: [
        { x: 30, y: 30, r: 0.8, brightness: 0.7 },
        { x: 50, y: 20, r: 0.6, brightness: 0.5 },
        { x: 70, y: 30, r: 0.9, brightness: 0.8 },
        { x: 60, y: 50, r: 0.7, brightness: 0.6 },
        { x: 40, y: 60, r: 0.5, brightness: 0.4 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 4 },
        { from: 4, to: 0 },
      ],
    },
  },
  {
    planId: 14,
    title: '스페인 바르셀로나 건축 투어',
    description: '가우디의 작품을 중심으로 한 바르셀로나 건축 탐방',
    startDate: '2025-08-05',
    endDate: '2025-08-10',
    tags: ['유럽', '스페인', '건축', '문화'],
    planWriters: [{ userId: 11, name: '건축덕후' }],
    likeCount: 276,
    isLiked: true,
    constellation: {
      stars: [
        { x: 40, y: 20, r: 0.8, brightness: 0.7 },
        { x: 20, y: 40, r: 0.6, brightness: 0.5 },
        { x: 40, y: 60, r: 0.9, brightness: 0.8 },
        { x: 60, y: 40, r: 0.7, brightness: 0.6 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 0 },
      ],
    },
  },
  {
    planId: 15,
    title: '동유럽 8일 완전정복',
    description: '체코, 오스트리아, 헝가리를 아우르는 동유럽 문화 탐방',
    startDate: '2025-09-10',
    endDate: '2025-09-17',
    tags: ['유럽', '동유럽', '문화', '역사'],
    planWriters: [{ userId: 8, name: '유럽탐험가' }],
    likeCount: 356,
    isLiked: false,
    constellation: {
      stars: [
        { x: 20, y: 20, r: 0.8, brightness: 0.7 },
        { x: 40, y: 40, r: 0.6, brightness: 0.5 },
        { x: 60, y: 20, r: 0.9, brightness: 0.8 },
        { x: 80, y: 40, r: 0.7, brightness: 0.6 },
        { x: 50, y: 60, r: 0.5, brightness: 0.4 },
        { x: 20, y: 60, r: 0.6, brightness: 0.5 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 4 },
        { from: 4, to: 5 },
        { from: 5, to: 0 },
      ],
    },
  },
  {
    planId: 16,
    title: '남부 이탈리아 로드트립',
    description: '나폴리부터 시칠리아까지 남부 이탈리아를 자동차로 여행',
    startDate: '2025-05-25',
    endDate: '2025-06-05',
    tags: ['유럽', '이탈리아', '로드트립', '미식'],
    planWriters: [{ userId: 9, name: '피자러버' }],
    likeCount: 284,
    isLiked: true,
    constellation: {
      stars: [
        { x: 30, y: 20, r: 0.8, brightness: 0.7 },
        { x: 20, y: 40, r: 0.6, brightness: 0.5 },
        { x: 40, y: 60, r: 0.9, brightness: 0.8 },
        { x: 60, y: 60, r: 0.7, brightness: 0.6 },
        { x: 80, y: 40, r: 0.5, brightness: 0.4 },
        { x: 70, y: 20, r: 0.6, brightness: 0.5 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 4 },
        { from: 4, to: 5 },
        { from: 5, to: 0 },
      ],
    },
  },
  {
    planId: 17,
    title: '북유럽 오로라 여행',
    description: '핀란드, 노르웨이에서 오로라를 감상하는 겨울 여행',
    startDate: '2026-01-15',
    endDate: '2026-01-22',
    tags: ['유럽', '북유럽', '오로라', '겨울'],
    planWriters: [{ userId: 10, name: '오로라헌터' }],
    likeCount: 412,
    isLiked: false,
    constellation: {
      stars: [
        { x: 30, y: 30, r: 0.8, brightness: 0.7 },
        { x: 50, y: 20, r: 0.6, brightness: 0.5 },
        { x: 70, y: 30, r: 0.9, brightness: 0.8 },
        { x: 60, y: 50, r: 0.7, brightness: 0.6 },
        { x: 40, y: 60, r: 0.5, brightness: 0.4 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 4 },
        { from: 4, to: 0 },
      ],
    },
  },
  {
    planId: 18,
    title: '스페인 바르셀로나 건축 투어',
    description: '가우디의 작품을 중심으로 한 바르셀로나 건축 탐방',
    startDate: '2025-08-05',
    endDate: '2025-08-10',
    tags: ['유럽', '스페인', '건축', '문화'],
    planWriters: [{ userId: 11, name: '건축덕후' }],
    likeCount: 276,
    isLiked: true,
    constellation: {
      stars: [
        { x: 40, y: 20, r: 0.8, brightness: 0.7 },
        { x: 20, y: 40, r: 0.6, brightness: 0.5 },
        { x: 40, y: 60, r: 0.9, brightness: 0.8 },
        { x: 60, y: 40, r: 0.7, brightness: 0.6 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 0 },
      ],
    },
  },
]);

// 인기 관광지 더미 데이터
const popularAttractions = ref<Attraction[]>([
  {
    attractionId: 1,
    name: '경복궁',
    image: 'https://example.com/gyeongbokgung.jpg',
    address: '서울특별시 종로구 사직로 161',
    contentType: '역사유적',
    rating: 4.7,
    latitude: 37.579617,
    longitude: 126.977041,
    isLiked: true,
    likeCount: 542,
  },
  {
    attractionId: 2,
    name: '해운대 해수욕장',
    image: 'https://example.com/haeundae.jpg',
    address: '부산광역시 해운대구 해운대해변로 264',
    contentType: '자연명소',
    rating: 4.8,
    latitude: 35.158795,
    longitude: 129.160606,
    isLiked: false,
    likeCount: 487,
  },
  {
    attractionId: 3,
    name: '제주 성산일출봉',
    image: 'https://example.com/seongsan.jpg',
    address: '제주특별자치도 서귀포시 성산읍 성산리',
    contentType: '자연명소',
    rating: 4.9,
    latitude: 33.458031,
    longitude: 126.94252,
    isLiked: true,
    likeCount: 623,
  },
  {
    attractionId: 4,
    name: '남산서울타워',
    image: 'https://example.com/seoultower.jpg',
    address: '서울특별시 용산구 남산공원길 105',
    contentType: '랜드마크',
    rating: 4.5,
    latitude: 37.551348,
    longitude: 126.988266,
    isLiked: false,
    likeCount: 478,
  },
  {
    attractionId: 5,
    name: '전주 한옥마을',
    image: 'https://example.com/hanok.jpg',
    address: '전라북도 전주시 완산구 기린대로 99',
    contentType: '문화명소',
    rating: 4.6,
    latitude: 35.815361,
    longitude: 127.149882,
    isLiked: false,
    likeCount: 392,
  },
  {
    attractionId: 1,
    name: '경복궁',
    image: 'https://example.com/gyeongbokgung.jpg',
    address: '서울특별시 종로구 사직로 161',
    contentType: '역사유적',
    rating: 4.7,
    latitude: 37.579617,
    longitude: 126.977041,
    isLiked: true,
    likeCount: 542,
  },
  {
    attractionId: 2,
    name: '해운대 해수욕장',
    image: 'https://example.com/haeundae.jpg',
    address: '부산광역시 해운대구 해운대해변로 264',
    contentType: '자연명소',
    rating: 4.8,
    latitude: 35.158795,
    longitude: 129.160606,
    isLiked: false,
    likeCount: 487,
  },
  {
    attractionId: 3,
    name: '제주 성산일출봉',
    image: 'https://example.com/seongsan.jpg',
    address: '제주특별자치도 서귀포시 성산읍 성산리',
    contentType: '자연명소',
    rating: 4.9,
    latitude: 33.458031,
    longitude: 126.94252,
    isLiked: true,
    likeCount: 623,
  },
  {
    attractionId: 4,
    name: '남산서울타워',
    image: 'https://example.com/seoultower.jpg',
    address: '서울특별시 용산구 남산공원길 105',
    contentType: '랜드마크',
    rating: 4.5,
    latitude: 37.551348,
    longitude: 126.988266,
    isLiked: false,
    likeCount: 478,
  },
  {
    attractionId: 5,
    name: '전주 한옥마을',
    image: 'https://example.com/hanok.jpg',
    address: '전라북도 전주시 완산구 기린대로 99',
    contentType: '문화명소',
    rating: 4.6,
    latitude: 35.815361,
    longitude: 127.149882,
    isLiked: false,
    likeCount: 392,
  },
]);

// 서울 명소 더미 데이터
const seoulAttractions = ref<Attraction[]>([
  {
    attractionId: 6,
    name: '광화문광장',
    image: 'https://example.com/gwanghwamun.jpg',
    address: '서울특별시 종로구 세종로',
    contentType: '랜드마크',
    rating: 4.3,
    latitude: 37.575268,
    longitude: 126.976896,
    isLiked: true,
    likeCount: 312,
  },
  {
    attractionId: 7,
    name: '명동',
    image: 'https://example.com/myeongdong.jpg',
    address: '서울특별시 중구 명동길',
    contentType: '쇼핑',
    rating: 4.2,
    latitude: 37.564432,
    longitude: 126.981119,
    isLiked: false,
    likeCount: 367,
  },
  {
    attractionId: 8,
    name: '북촌 한옥마을',
    image: 'https://example.com/bukchon.jpg',
    address: '서울특별시 종로구 계동길 37',
    contentType: '문화명소',
    rating: 4.6,
    latitude: 37.58273,
    longitude: 126.983771,
    isLiked: true,
    likeCount: 419,
  },
  {
    attractionId: 9,
    name: '동대문디자인플라자',
    image: 'https://example.com/ddp.jpg',
    address: '서울특별시 중구 을지로 281',
    contentType: '문화시설',
    rating: 4.4,
    latitude: 37.566809,
    longitude: 127.009391,
    isLiked: false,
    likeCount: 342,
  },
  {
    attractionId: 10,
    name: '홍대 거리',
    image: 'https://example.com/hongdae.jpg',
    address: '서울특별시 마포구 와우산로 29',
    contentType: '문화거리',
    rating: 4.5,
    latitude: 37.55653,
    longitude: 126.923952,
    isLiked: true,
    likeCount: 387,
  },
]);

// 이벤트 핸들러
const handlePlanCardClick = (plan: Plan) => {
  console.log('여행 계획 카드 클릭:', plan.title);
  // 여행 계획 상세 페이지로 이동
  // ex: router.push(`/plans/${plan.planId}`);
};

const handlePlanLikeClick = (plan: Plan) => {
  console.log('여행 계획 좋아요 클릭:', plan.title);
  // 좋아요 상태 변경 로직
  // ex: 좋아요 API 호출 후 상태 업데이트
};

const handlePlanTagClick = (tag: string) => {
  console.log('여행 계획 태그 클릭:', tag);
  // 태그로 필터링 로직
  // ex: router.push(`/tags/${encodeURIComponent(tag)}`);
};

const handleAttractionCardClick = (attraction: Attraction) => {
  console.log('관광지 카드 클릭:', attraction.name);
  // 관광지 상세 페이지로 이동
  // ex: router.push(`/attractions/${attraction.attractionId}`);
};

const handleAttractionLikeClick = (attraction: Attraction) => {
  console.log('관광지 좋아요 클릭:', attraction.name);
  // 좋아요 상태 변경 로직
  // ex: 좋아요 API 호출 후 상태 업데이트
};

const handleAttractionTagClick = (contentType: string) => {
  console.log('관광지 태그(컨텐츠 타입) 클릭:', contentType);
  // 콘텐츠 타입으로 필터링 로직
  // ex: router.push(`/attractions/types/${encodeURIComponent(contentType)}`);
};

const handleMoreClick = (category: string) => {
  console.log('더보기 클릭:', category);
  // 해당 카테고리 전체 목록 페이지로 이동
  // 카테고리에 따라 다른 경로로 라우팅
};
</script>
