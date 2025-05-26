<template>
  <div class="h-full overflow-y-auto">
    <div class="flex items-center justify-between border-b border-white/20 bg-slate-900/80 p-2">
      <div class="flex items-center">
        <Button
          variant="ghost"
          size="icon"
          class="mr-2 h-8 w-8 text-gray-300 hover:bg-white/10 hover:text-white"
          @click="goBack"
        >
          <ChevronLeft class="h-5 w-5" />
        </Button>
        <h3 class="text-lg font-semibold text-white">여행 계획 상세</h3>
      </div>

      <div v-if="isModifyPage" class="flex items-center gap-2">
        <!-- 공유 버튼 -->
        <Button
          variant="outline"
          size="sm"
          class="border-blue-500/50 bg-blue-900/30 text-blue-300 hover:bg-blue-800/50 hover:text-blue-200"
          :disabled="isGeneratingShare"
          @click="handleSharePlan"
        >
          <Share2 class="mr-1 h-4 w-4" />
          {{ isGeneratingShare ? '생성 중...' : '공유하기' }}
        </Button>

        <Button
          variant="outline"
          size="sm"
          class="border-purple-500/50 bg-purple-900/30 text-purple-300 hover:bg-purple-800/50 hover:text-purple-200"
          @click="handleTogglePlanInfoEdit"
        >
          계획 정보 수정
        </Button>
      </div>
    </div>

    <AsyncContainer :loadingComponent="PlanDetailSkeleton" :errorComponent="PlanDetailError">
      <PlanDetailContent :planId="planId" @moveToAttractionSearch="handleMoveToAttractionSearch" />
    </AsyncContainer>

    <!-- 여행 계획 정보 수정 모달 -->
    <PlanFormModal
      :isOpen="isPlanInfoEditOpen"
      :isEditMode="true"
      :initialData="planStore.currentPlan"
      :isSubmitting="isSubmittingEdit"
      @update:open="isPlanInfoEditOpen = $event"
      @submit="handlePlanInfoSubmit"
    />

    <!-- 별자리 커스텀 선택 다이얼로그 -->
    <Dialog :open="isConstellationChoiceOpen" @update:open="isConstellationChoiceOpen = $event">
      <DialogContent
        class="border-slate-700/50 bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 shadow-2xl sm:max-w-lg"
      >
        <DialogHeader class="pb-6 text-center">
          <div
            class="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-gradient-to-br from-blue-500/20 to-purple-500/20 ring-1 ring-blue-500/30"
          >
            <svg
              class="h-8 w-8 text-blue-400"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"
              />
            </svg>
          </div>
          <DialogTitle class="mb-2 text-2xl font-bold text-white">별자리 설정</DialogTitle>
          <DialogDescription class="text-base leading-relaxed text-gray-300">
            공유할 여행 계획을 나만의 별자리로 꾸며보세요.
            <br />
            특별한 추억을 더욱 아름답게 표현할 수 있습니다.
          </DialogDescription>
        </DialogHeader>

        <div class="space-y-4 py-6">
          <!-- 커스텀 옵션 -->
          <div
            class="group relative cursor-pointer overflow-hidden rounded-xl border border-blue-500/30 bg-gradient-to-r from-blue-900/40 via-blue-800/30 to-purple-900/40 p-6 transition-all duration-300 hover:border-blue-400/50 hover:shadow-lg hover:shadow-blue-500/25"
            @click="handleCustomConstellation"
          >
            <div
              class="absolute inset-0 bg-gradient-to-r from-blue-600/10 to-purple-600/10 opacity-0 transition-opacity duration-300 group-hover:opacity-100"
            ></div>
            <div class="relative flex items-center">
              <div
                class="flex h-12 w-12 items-center justify-center rounded-full bg-blue-500/20 ring-1 ring-blue-400/30 transition-colors group-hover:bg-blue-500/30"
              >
                <svg
                  class="h-6 w-6 text-blue-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M11 4a2 2 0 114 0v1a1 1 0 001 1h3a1 1 0 011 1v3a1 1 0 01-1 1h-1a2 2 0 100 4h1a1 1 0 011 1v3a1 1 0 01-1 1h-3a1 1 0 01-1-1v-1a2 2 0 10-4 0v1a1 1 0 01-1 1H7a1 1 0 01-1-1v-3a1 1 0 011-1h1a2 2 0 100-4H7a1 1 0 01-1-1V7a1 1 0 011-1h3a1 1 0 001-1V4z"
                  />
                </svg>
              </div>
              <div class="ml-4 flex-1">
                <h3
                  class="text-lg font-semibold text-white transition-colors group-hover:text-blue-100"
                >
                  별자리 커스텀하기
                </h3>
                <p class="text-sm text-blue-200/80 transition-colors group-hover:text-blue-100/90">
                  여행지를 별로 연결하여 나만의 별자리 만들기
                </p>
              </div>
              <div class="ml-4">
                <div
                  class="rounded-full bg-blue-500/20 px-3 py-1 text-xs font-medium text-blue-300 transition-colors group-hover:bg-blue-500/30"
                >
                  편집 모드
                </div>
              </div>
            </div>
          </div>

          <!-- 기본 옵션 -->
          <div
            class="group relative cursor-pointer overflow-hidden rounded-xl border border-slate-600/50 bg-gradient-to-r from-slate-800/50 to-slate-700/50 p-6 transition-all duration-300 hover:border-slate-500/70 hover:shadow-lg hover:shadow-slate-500/20"
            @click="handleDefaultConstellation"
          >
            <div
              class="absolute inset-0 bg-gradient-to-r from-slate-600/10 to-slate-500/10 opacity-0 transition-opacity duration-300 group-hover:opacity-100"
            ></div>
            <div class="relative flex items-center">
              <div
                class="flex h-12 w-12 items-center justify-center rounded-full bg-slate-500/20 ring-1 ring-slate-400/30 transition-colors group-hover:bg-slate-500/30"
              >
                <svg
                  class="h-6 w-6 text-slate-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M13 10V3L4 14h7v7l9-11h-7z"
                  />
                </svg>
              </div>
              <div class="ml-4 flex-1">
                <h3
                  class="text-lg font-semibold text-white transition-colors group-hover:text-slate-100"
                >
                  기본 별자리 사용
                </h3>
                <p
                  class="text-sm text-slate-300/80 transition-colors group-hover:text-slate-200/90"
                >
                  여행지를 기반으로 자동 생성된 별자리 사용
                </p>
              </div>
              <div class="ml-4">
                <div
                  class="rounded-full bg-slate-500/20 px-3 py-1 text-xs font-medium text-slate-300 transition-colors group-hover:bg-slate-500/30"
                >
                  바로 공유
                </div>
              </div>
            </div>
          </div>
        </div>

        <DialogFooter class="border-t border-slate-700/50 pt-6">
          <DialogClose asChild>
            <Button
              type="button"
              variant="secondary"
              class="border-slate-600/50 bg-slate-700/50 text-slate-200 hover:bg-slate-600/50"
            >
              취소
            </Button>
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>

    <!-- 공유 링크 모달 -->
    <ShareLinkModal
      :isOpen="isShareModalOpen"
      :shareLink="shareLink"
      @update:open="isShareModalOpen = $event"
      @copySuccess="handleCopySuccess"
      @copyError="handleCopyError"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { toast } from 'vue-sonner';
import { ChevronLeft, Share2 } from 'lucide-vue-next';
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';
import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogClose,
} from '@/components/ui/dialog';
import {
  PlanDetailError,
  PlanDetailSkeleton,
  PlanDetailContent,
} from '@/components/views/plan/PlanDetail';
import PlanFormModal from '@/components/views/plan/PlanFormModal.vue';
import ShareLinkModal from '@/components/views/myPlan/ShareLinkModal.vue';
import { usePlanStore } from '@/stores/plan';
import {
  updatePlanInfo,
  updatePlanSchedule,
  checkLock,
  type CreatePlanRequest,
  type UpdatePlanInfoRequest,
  type UpdatePlanScheduleRequest,
} from '@/services/api/domains/plan';
import { getShareLink } from '@/services/api/domains/stella';
import { ROUTES } from '@/router/routes';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();

const route = useRoute();
/**
 * 현재 라우트가 편집 화면인지 확인
 */
const isModifyPage = computed(() => {
  return route.name === ROUTES.PLAN_MODIFY.name;
});

// Vue Router
const router = useRouter();
const planStore = usePlanStore();

// planId 계산 속성
const planId = computed(() => Number(route.params.planId));

// 상태 관리
const isPlanInfoEditOpen = ref(false);
const isSubmittingEdit = ref(false);
const isGeneratingShare = ref(false);
const isShareModalOpen = ref(false);
const isConstellationChoiceOpen = ref(false); // 별자리 선택 다이얼로그 상태
const shareLink = ref('');

const emit = defineEmits<{
  (e: 'moveToAttractionSearch', date: string): void;
}>();

/**
 * 여행지 추가 버튼 클릭 핸들러
 * @param date - 선택된 날짜
 */
const handleMoveToAttractionSearch = (date: string) => {
  emit('moveToAttractionSearch', date);
};

/**
 * 여행계획 정보 수정 모달 열기
 */
const handleTogglePlanInfoEdit = () => {
  isPlanInfoEditOpen.value = true;
};

/**
 * 공유 버튼 클릭 핸들러 - 별자리 커스텀 선택 다이얼로그 열기
 */
const handleSharePlan = () => {
  if (!planStore.currentPlan || isGeneratingShare.value) return;
  isConstellationChoiceOpen.value = true;
};

/**
 * 별자리 커스텀하기 선택 - 편집 모드로 이동
 */
const handleCustomConstellation = () => {
  if (!planStore.currentPlan) return;

  isConstellationChoiceOpen.value = false;

  router.push({
    name: ROUTES.STELLA.name,
    params: {
      planId: planStore.currentPlan.planId.toString(),
    },
  });
};

/**
 * 기본 별자리 사용 - 바로 공유 링크 생성
 */
const handleDefaultConstellation = async () => {
  if (!planStore.currentPlan || isGeneratingShare.value) return;

  try {
    isConstellationChoiceOpen.value = false;
    isGeneratingShare.value = true;

    // currentPlan을 JSON 문자열로 변환
    const stellaData = JSON.stringify(planStore.currentPlan);

    const response = await getShareLink({
      planId: planStore.currentPlan.planId,
      stellaData,
    });

    shareLink.value = `${window.location.origin}/shared/${response.stellaLink}`;
    isShareModalOpen.value = true;

    toast.success('공유 링크가 생성되었습니다!');
  } catch (error) {
    console.error('공유 링크 생성 실패:', error);
    toast.error('공유 링크 생성에 실패했습니다', {
      description: '잠시 후 다시 시도해주세요.',
      duration: 4000,
    });
  } finally {
    isGeneratingShare.value = false;
  }
};

/**
 * 공유 링크 복사 성공 핸들러
 */
const handleCopySuccess = () => {
  // 추가적인 성공 처리 로직이 필요하면 여기에 작성
};

/**
 * 공유 링크 복사 에러 핸들러
 */
const handleCopyError = (error: Error) => {
  console.error('링크 복사 에러:', error);
  // 추가적인 에러 처리 로직이 필요하면 여기에 작성
};

/**
 * 여행계획 정보 수정 제출 핸들러
 * @param formData - 수정된 폼 데이터
 */
const handlePlanInfoSubmit = async (formData: CreatePlanRequest) => {
  if (isSubmittingEdit.value || !planStore.currentPlan) return;

  try {
    isSubmittingEdit.value = true;

    const originalPlan = planStore.currentPlan;

    // 일정 정보가 변경되었는지 확인
    const isScheduleChanged =
      formData.startDate !== originalPlan.startDate || formData.endDate !== originalPlan.endDate;

    if (isScheduleChanged) {
      // 일정 변경 시 락 확인
      const lockResponse = await checkLock(originalPlan.planId);

      if (lockResponse.lockStatus && lockResponse.userId !== authStore.user?.id) {
        toast.error('다른 사용자가 수정 중입니다', {
          description: '잠시 후 다시 시도해주세요.',
          duration: 4000,
        });
        return;
      }

      // 일정 정보 업데이트 (락 확인 필요)
      const scheduleData: UpdatePlanScheduleRequest = {
        startDate: formData.startDate,
        endDate: formData.endDate,
      };

      await updatePlanSchedule(originalPlan.planId, scheduleData);
    }

    // 기본 정보 업데이트 (락 확인 불필요)
    const infoData: UpdatePlanInfoRequest = {
      title: formData.title,
      description: formData.description,
      tags: formData.tags,
      isPublic: formData.isPublic,
    };

    const updatedPlanResponse = await updatePlanInfo(originalPlan.planId, infoData);

    planStore.setPlanDetail(updatedPlanResponse);

    toast.success('여행 계획이 성공적으로 수정되었습니다!', {
      description: `${formData.title} 계획이 업데이트되었습니다.`,
      duration: 4000,
    });

    isPlanInfoEditOpen.value = false;
  } catch (error) {
    console.error('계획 수정 실패:', error);

    toast.error('계획 수정에 실패했습니다', {
      description: '잠시 후 다시 시도해주세요.',
      duration: 4000,
    });
  } finally {
    isSubmittingEdit.value = false;
  }
};

/**
 * 뒤로 가기
 */
const goBack = () => {
  router.back();
};
</script>
