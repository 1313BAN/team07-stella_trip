<script setup lang="ts">
import type { PlanDetail } from '@/services/api/domains/plan';
import { getMyStellaList } from '@/services/api/domains/stella';
import MyCard from '@/components/views/myPage/MyCard.vue';
import { onMounted, reactive } from 'vue';
import { toast } from 'vue-sonner';
import { StellaResponse, StellaAI } from '@/services/api/domains/stella/types';
import GridCardListContainer from '@/components/common/GridCardListContainer/GridCardListContainer.vue';
import { useRouter } from 'vue-router';
import { ROUTES } from '@/router/routes';

const router = useRouter();
const stellaList = reactive<{ stellaLink: string; planDetail: PlanDetail; stellaAI: StellaAI }[]>(
  []
);

const loadMyCard = async () => {
  try {
    const response = await getMyStellaList();
    response.stellaList.forEach((stella: StellaResponse) => {
      try {
        const planData = JSON.parse(stella.stellaData) as PlanDetail;
        const stellaAI = stella.stellaAI;

        stellaList.push({
          planDetail: planData,
          stellaAI: stellaAI,
          stellaLink: stella.stellaLink,
        });
      } catch {
        toast('카드 데이터 파싱 실패');
      }
    });
  } catch {
    toast('내 카드 로드 실패');
  }
};

onMounted(async () => {
  await loadMyCard();
});

const handleCardClick = (stella: {
  stellaLink: string;
  planDetail: PlanDetail;
  stellaAI: StellaAI;
}) => {
  router.push({
    name: ROUTES.SHARED.name,
    params: { link: stella.stellaLink },
  });
};
</script>

<template>
  <div class="mb-4">
    <template v-if="stellaList.length === 0">
      <div class="h-full w-full pt-16 text-center">
        <p class="text-lg text-gray-200">검색 결과가 없습니다.</p>
      </div>
    </template>
    <template v-else>
      <GridCardListContainer :showTitle="false" gap="3rem">
        <MyCard
          v-for="stella in stellaList"
          :key="stella.planDetail.planId"
          :subtitle="stella.stellaAI.cardName"
          :stella="stella.planDetail.stella"
          @click="handleCardClick(stella)"
        />
      </GridCardListContainer>
    </template>
  </div>
</template>
