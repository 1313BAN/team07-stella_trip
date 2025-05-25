<template>
  <div class="flex flex-col divide-y divide-white/10">
    <!-- 별자리 표시 섹션 -->
    <StellaHeader :stella="planDetail?.stella" :backgroundStars="backgroundStars" />

    <!-- 기본 정보 섹션 -->
    <PlanInfo class="px-2" :plan="planDetail" @toggleLike="toggleLike" @invite="handleInvite" />

    <!-- 일정 섹션 -->
    <div class="flex-1 bg-slate-900/20 p-3">
      <div class="mb-3 flex items-center justify-between">
        <h3 class="p-2 text-lg font-semibold text-purple-200">일정</h3>
        <div class="flex items-center gap-2">
          <!-- 일정 편집 버튼 -->
          <Button
            v-if="isModifyPage"
            variant="outline"
            size="sm"
            class="border-blue-500/50 bg-blue-900/30 text-blue-300 hover:bg-blue-800/50 hover:text-blue-200"
            :disabled="isProcessingLock"
            @click="handleToggleScheduleEdit"
          >
            <LoaderCircle v-if="isProcessingLock" class="mr-2 h-4 w-4 animate-spin" />
            {{ planStore.isModifying ? '편집 완료' : '일정 편집' }}
          </Button>

          <!-- 경로 표시 중 배지 -->
          <div v-if="selectedDate" class="flex items-center">
            <Badge variant="outline" class="border-purple-400/30 bg-purple-900/30 text-purple-200">
              {{ selectedDate }} 경로 표시 중
            </Badge>
            <Button
              variant="ghost"
              size="sm"
              class="ml-2 text-purple-200 hover:bg-purple-900/30"
              @click="resetRoute"
            >
              <X class="h-4 w-4" />
            </Button>
          </div>
        </div>
      </div>

      <!-- 일별 일정 목록 -->
      <div v-if="planDetail?.details" class="space-y-4">
        <DailySchedule
          v-for="(attractions, date) in planDetail.details"
          :key="date"
          :date="date"
          :attractions="attractions"
          @showRoute="showRouteOnMap"
          @addAttraction="handleAddAttraction"
          @deleteAttraction="handleDeleteAttraction"
        />
      </div>

      <!-- 일정이 없을 경우 -->
      <EmptySchedule v-else />
    </div>

    <!-- 여행에서 나가기 버튼 -->
    <div class="border-t border-red-500/20 bg-slate-900/20 p-3">
      <Button
        variant="ghost"
        size="sm"
        class="w-full border border-red-500/30 text-red-300 hover:bg-red-900/20 hover:text-red-200"
        @click="handleLeavePlan"
      >
        <LogOut class="mr-2 h-4 w-4" />
        여행에서 나가기
      </Button>
    </div>
  </div>

  <!-- 초대하기 모달 -->
  <InviteModal
    :isOpen="isInviteModalOpen"
    :planId="planDetail?.planId"
    :planTitle="planDetail?.title"
    @update:open="isInviteModalOpen = $event"
  />

  <!-- 여행 나가기 확인 모달 -->
  <ConfirmModal
    :isOpen="isLeaveConfirmOpen"
    title="여행에서 나가시겠습니까?"
    :description="`${planDetail?.title} 여행에서 나가면 더 이상 이 여행 계획에 접근할 수 없습니다.`"
    confirmText="나가기"
    cancelText="취소"
    variant="destructive"
    @confirm="confirmLeavePlan"
    @cancel="isLeaveConfirmOpen = false"
  />
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue';
import { toast } from 'vue-sonner';
import { X, LoaderCircle, LogOut } from 'lucide-vue-next';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import StellaHeader from './StellaHeader.vue';
import PlanInfo from './PlanInfo.vue';
import DailySchedule from '@/components/card/DailyScheduleCard/DailyScheduleCard.vue';
import EmptySchedule from './EmptySchedule.vue';
import InviteModal from './InviteModal.vue';
import ConfirmModal from './ConfirmModal.vue'; // 확인 모달 컴포넌트 (생성 필요)
import {
  getPlanDetail,
  postPlanLike,
  deletePlanLike,
  checkLock,
  getLock,
  returnLock,
  updateRoute,
  leaveMyPlan,
  type PlanDetail,
  type RouteAttraction,
  type UpdateRouteRequest,
} from '@/services/api/domains/plan';
import { useMapState } from '@/composables/useMapState';
import { usePlanStore } from '@/stores/plan';
import type { MarkerInfo } from '@/types/kakao';
import { useAuthStore } from '@/stores/auth';
import { ROUTES } from '@/router/routes';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

/**
 * 현재 라우트가 편집 화면인지 확인
 */
const isModifyPage = computed(() => {
  return route.name === ROUTES.PLAN_MODIFY.name;
});

const props = defineProps<{
  planId: number;
}>();

// Emits
const emit = defineEmits<{
  (e: 'moveToAttractionSearch', date: string): void;
}>();

const { selectPlanDetail, showRoute, clearPolylines, clearMarkers } = useMapState();
const planStore = usePlanStore();

// Store에서 현재 플랜 데이터 가져오기 (reactive)
const planDetail = computed(() => planStore.currentPlan);
const selectedDate = ref<string | null>(null);
const isProcessingLock = ref<boolean>(false);

// 모달 상태
const isInviteModalOpen = ref<boolean>(false);
const isLeaveConfirmOpen = ref<boolean>(false);

/**
 * 여행지 추가 버튼 클릭 핸들러
 * @param date - 선택된 날짜
 */
const handleAddAttraction = (date: string) => {
  emit('moveToAttractionSearch', date);
};

// 로그인 여부 확인 (예시: 로컬스토리지 토큰 기반)
const isLoggedIn = computed(() => {
  const authStore = useAuthStore();
  return authStore.isAuthenticated;
});

// 좋아요 토글
const toggleLike = () => {
  if (isLoggedIn.value === false) {
    toast('로그인이 필요한 기능입니다.');
    return;
  }

  if (planStore.currentPlan) {
    if (planStore.currentPlan?.planId) {
      if (!planStore.currentPlan.isLiked) {
        postPlanLike(planStore.currentPlan.planId)
          .then(() => {
            toast('좋아요가 추가되었습니다.');
          })
          .catch(() => {
            toast('좋아요 처리 중 오류 발생:');
          })
          .finally(() => {
            if (planStore.currentPlan) {
              planStore.currentPlan.isLiked = true;
              planStore.currentPlan.likeCount += 1;
            }
          });
      } else {
        deletePlanLike(planStore.currentPlan.planId)
          .then(() => {
            toast('좋아요가 취소되었습니다.');
          })
          .catch(() => {
            toast('이미 좋아요를 누른 계획입니다.');
          })
          .finally(() => {
            if (planStore.currentPlan) {
              planStore.currentPlan.isLiked = false;
              planStore.currentPlan.likeCount -= 1;
            }
          });
      }
    }
  }
};
/**
 * 여행지 삭제 핸들러
 * @param date - 대상 날짜
 * @param routeId - 삭제할 여행지 ID
 */
const handleDeleteAttraction = (date: string, routeId: number) => {
  planStore.removeAttractionFromDate(date, routeId);

  // 현재 선택된 날짜의 경로가 표시 중이라면 업데이트
  if (selectedDate.value === date && planDetail.value?.details?.[date]) {
    const updatedAttractions = planDetail.value.details[date];
    if (updatedAttractions.length > 0) {
      showRouteOnMap(date, updatedAttractions);
    } else {
      resetRoute();
    }
  }
};

/**
 * 초대하기 버튼 클릭 핸들러
 * @param planId - 플랜 ID
 */
const handleInvite = (planId: number) => {
  isInviteModalOpen.value = true;
};

/**
 * 여행에서 나가기 버튼 클릭 핸들러
 */
const handleLeavePlan = () => {
  isLeaveConfirmOpen.value = true;
};

/**
 * 여행 나가기 확인
 */
const confirmLeavePlan = async () => {
  if (!planDetail.value) return;

  try {
    // API 호출 - 여행에서 나가기 (변경된 함수명 사용)
    await leaveMyPlan(planDetail.value.planId);

    toast.success('여행에서 나갔습니다', {
      description: '해당 여행 계획에 더 이상 접근할 수 없습니다.',
      duration: 4000,
    });

    // 메인 페이지로 이동
    router.push({ name: ROUTES.HOME.name });
  } catch (error) {
    console.error('여행 나가기 실패:', error);
    toast.error('여행 나가기에 실패했습니다', {
      description: '잠시 후 다시 시도해주세요.',
      duration: 4000,
    });
  } finally {
    isLeaveConfirmOpen.value = false;
  }
};

/**
 * 일정 편집 모드 토글
 */
const handleToggleScheduleEdit = async () => {
  if (!planDetail.value) return;
  console.log(planStore.isModifying);

  try {
    isProcessingLock.value = true;

    if (planStore.isModifying) {
      // 편집 완료 - 변경사항 저장 후 락 반환
      await saveChangesAndReleaseLock();
    } else {
      // 편집 모드 진입 - 락 확인 및 획득
      await acquireLockAndEnterEditMode();
    }
  } catch (error) {
    console.error('편집 모드 토글 실패:', error);
  } finally {
    isProcessingLock.value = false;
  }
};

/**
 * 락을 획득하고 편집 모드에 진입합니다.
 */
const acquireLockAndEnterEditMode = async () => {
  if (!planDetail.value) return;

  // 1. 락 상태 확인
  const lockStatus = await checkLock(planDetail.value.planId);

  if (lockStatus.lockStatus) {
    // 다른 사용자가 락을 보유 중
    toast.error('다른 사용자가 수정 중입니다', {
      description: '잠시 후 다시 시도해주세요.',
      duration: 4000,
    });
    return;
  }

  // 2. 락 획득
  await getLock(planDetail.value.planId);
  planStore.setLock(true);

  // 3. 편집 모드 진입
  planStore.setModifying(true);

  toast.success('편집 모드에 진입했습니다', {
    description: '여행지 순서를 변경하거나 삭제할 수 있습니다.',
    duration: 3000,
  });
};

/**
 * 변경사항을 저장하고 락을 반환합니다.
 */
const saveChangesAndReleaseLock = async () => {
  if (!planDetail.value) return;

  let saveSuccess = false;

  try {
    // 1. 변경사항 확인 (수정된 날짜 또는 삭제된 항목이 있는지)
    const hasModifications = planStore.hasModifications();
    const modifiedRoutes = planStore.getModifiedRoutes();

    console.log('수정사항 확인:', {
      hasModifications,
      modifiedRoutesCount: modifiedRoutes.length,
      modifiedDates: Array.from(planStore.modifiedDates),
      deletedRoutes: Array.from(planStore.deletedRoutes.keys()),
      routes: modifiedRoutes,
    });

    if (hasModifications && modifiedRoutes.length > 0) {
      const updateRequest: UpdateRouteRequest = {
        routes: modifiedRoutes,
      };

      console.log('서버로 전송할 데이터:', updateRequest);

      // 변경사항 저장
      const updatedPlan = await updateRoute(planDetail.value.planId, updateRequest);
      planStore.setPlanDetail(updatedPlan);
      saveSuccess = true;

      // 성공 메시지 세분화
      const deletedCount = modifiedRoutes.filter(r => r.deleted).length;
      const modifiedCount = modifiedRoutes.filter(r => !r.deleted).length;

      let description = '';
      if (deletedCount > 0 && modifiedCount > 0) {
        description = `경로 수정, 삭제`;
      } else if (deletedCount > 0) {
        description = `${deletedCount}개 경로가 삭제되었습니다`;
      } else {
        description = `경로가 수정되었습니다`;
      }

      toast.success('변경사항이 저장되었습니다', {
        description,
        duration: 4000,
      });
    } else {
      // 변경사항이 없어도 성공으로 처리
      saveSuccess = true;
      toast.info('변경사항이 없어 저장하지 않았습니다', {
        duration: 3000,
      });
    }
  } catch (error) {
    console.error('변경사항 저장 실패:', error);
    toast.error('변경사항 저장에 실패했습니다', {
      description: '잠시 후 다시 시도해주세요.',
      duration: 4000,
    });
  } finally {
    // 2. 에러 발생 여부와 관계없이 항상 실행되는 정리 작업
    try {
      // 락 반환
      await returnLock(planDetail.value.planId);
      planStore.setLock(false);

      // 편집 모드 종료 (이때 modifiedDates와 deletedRoutes도 초기화됨)
      planStore.setModifying(false);

      // 에러가 발생했거나 저장이 실패한 경우 데이터 재로드
      if (!saveSuccess) {
        console.log('에러 발생으로 인한 데이터 재로드 시작');
        await initializeData(); // 최신 데이터로 갱신
        toast.info('최신 데이터로 갱신되었습니다', {
          duration: 3000,
        });
      }
    } catch (lockError) {
      console.error('락 반환 또는 데이터 갱신 실패:', lockError);

      // 락 반환에 실패해도 클라이언트 상태는 정리
      planStore.setLock(false);
      planStore.setModifying(false);

      // 데이터 재로드 시도
      try {
        await initializeData();
        toast.warning('편집 모드를 종료했지만 일부 오류가 발생했습니다', {
          description: '데이터는 최신 상태로 갱신되었습니다.',
          duration: 4000,
        });
      } catch (reloadError) {
        console.error('데이터 재로드 실패:', reloadError);
        toast.error('편집 모드 종료 중 오류가 발생했습니다', {
          description: '페이지를 새로고침해주세요.',
          duration: 5000,
        });
      }
    }
  }
};

// 배경 별 생성
const backgroundStars = Array.from({ length: 30 }, () => ({
  x: Math.random() * 100,
  y: Math.random() * 100,
  r: Math.random() * 0.8 + 0.2,
  brightness: Math.random() * 0.4 + 0.2,
  duration: Math.random() * 2 + 2,
}));

/**
 * RouteAttraction을 MarkerInfo로 변환
 * @param attractions - 여행지 배열
 * @param date - 날짜
 * @returns MarkerInfo 배열
 */
const convertToMarkerInfos = (attractions: RouteAttraction[], date: string): MarkerInfo[] => {
  return attractions
    .map(attr => ({
      lat: parseFloat(String(attr.latitude)),
      lng: parseFloat(String(attr.longitude)),
      name: attr.name,
      order: attr.order,
      date: date,
    }))
    .filter(info => !isNaN(info.lat) && !isNaN(info.lng));
};

/**
 * 일별 경로를 지도에 표시
 * @param date - 선택된 날짜
 * @param attractions - 해당 날짜의 여행지 배열
 */
const showRouteOnMap = (date: string, attractions: RouteAttraction[]) => {
  selectedDate.value = date;

  if (!planDetail.value) return;

  const dailyPlanDetail = {
    ...planDetail.value,
    details: {
      [date]: attractions,
    },
  } as PlanDetail;

  // 다중 마커 표시
  selectPlanDetail(dailyPlanDetail);

  // 폴리라인 표시
  const markerInfos = convertToMarkerInfos(attractions, date);
  if (markerInfos.length >= 2) {
    showRoute(markerInfos);
  }
};

/**
 * 경로 표시 초기화
 */
const resetRoute = () => {
  selectedDate.value = null;

  clearPolylines();

  if (planDetail.value) {
    selectPlanDetail(planDetail.value as PlanDetail);
  }
};

/**
 * 플랜 데이터 초기화
 */
const initializeData = async () => {
  try {
    // API에서 플랜 데이터 가져오기
    const data = await getPlanDetail(props.planId);

    planStore.setPlanDetail(data);

    // 지도에 표시
    if (data) {
      selectPlanDetail(data);
    }
  } catch (error) {
    console.error('플랜 로드 실패:', error);
    planStore.setPlanDetail(null);
  }
};

// planDetail의 변경사항을 지도에 반영 (순서 변경 시 자동 업데이트)
watch(
  () => planDetail.value,
  newPlan => {
    if (newPlan && selectedDate.value && newPlan.details?.[selectedDate.value]) {
      // 현재 선택된 날짜의 경로가 있다면 지도 업데이트
      const attractions = newPlan.details[selectedDate.value];
      showRouteOnMap(selectedDate.value, attractions);
    } else if (newPlan) {
      // 전체 플랜 표시
      selectPlanDetail(newPlan as PlanDetail);
    }
  },
  { deep: true }
);

// planId 변경 감지
watch(
  () => props.planId,
  async (newId, oldId) => {
    if (newId !== oldId) {
      selectedDate.value = null; // 선택된 날짜 초기화

      // 기존 락이 있다면 반환
      if (planStore.hasLock && oldId) {
        try {
          await returnLock(oldId);
        } catch (error) {
          console.error('기존 락 반환 실패:', error);
        }
      }

      planStore.clearPlan();
      await initializeData();
    }
  }
);

// 컴포넌트 마운트 시 초기 데이터 로드
onMounted(async () => {
  await initializeData();
});

// 컴포넌트 언마운트 시 정리
onUnmounted(async () => {
  clearPolylines();
  clearMarkers();

  // 락이 있다면 반환
  if (planStore.hasLock && planDetail.value) {
    try {
      await returnLock(planDetail.value.planId);
      planStore.setLock(false);
    } catch (error) {
      console.error('락 반환 실패:', error);
    }
  }
});
</script>
