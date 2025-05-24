<template>
  <Card
    class="cursor-pointer border-white/10 bg-white/5 shadow-none transition-all duration-200 hover:bg-white/10"
    @click="showRouteOnMap"
  >
    <CardHeader class="border-b border-white/10 px-6">
      <h4 class="flex items-center gap-3 text-lg font-semibold text-white">
        <Calendar class="h-5 w-5 text-purple-400" />
        {{ date }}
      </h4>
    </CardHeader>
    <CardContent class="px-3">
      <!-- Vue Draggable 컨테이너 -->
      <draggable
        v-model="localAttractions"
        :itemKey="`${props.date}`"
        :disabled="!planStore.isModifying"
        class="flex flex-col gap-2"
        :animation="150"
        ghostClass="sortable-ghost"
        chosenClass="sortable-chosen"
        dragClass="sortable-drag"
        @end="handleOrderChange"
        @start="handleDragStart"
      >
        <template #item="{ element: attraction }">
          <div
            :class="[
              'transition-transform duration-150 ease-in-out',
              planStore.isModifying ? 'cursor-move hover:bg-white/10' : 'cursor-default',
            ]"
          >
            <AttractionItem
              :key="`${props.date}-${attraction.routeId}`"
              :order="attraction.order"
              :name="attraction.name"
              :visitTime="attraction.visitTime"
              :address="attraction.address"
              :memo="attraction.memo ?? ''"
              :data-id="attraction.routeId"
              @delete="handleDeleteAttraction(attraction.routeId)"
            />
          </div>
        </template>
      </draggable>

      <button
        v-if="isAttractionSearchPage"
        class="mt-4 flex w-full items-center justify-center gap-2 rounded-lg border-2 border-dashed border-white/20 py-2 text-sm text-white transition-colors hover:cursor-pointer hover:border-purple-400/50 hover:bg-white/10"
        @click.stop="handleAddAttraction"
      >
        <Plus class="h-4 w-4 text-white" />
        여행지 추가하기
      </button>
    </CardContent>
  </Card>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue';
import { useRoute } from 'vue-router';
import { Calendar, Plus } from 'lucide-vue-next';
import { Card, CardHeader, CardContent } from '@/components/ui/card';
import AttractionItem from '@/components/common/AttractionItem/AttractionItem.vue';
import type { RouteAttraction } from '@/services/api/domains/plan';
import { usePlanStore } from '@/stores/plan';
import { ROUTES } from '@/router/routes';
import draggable from 'vuedraggable';

const props = defineProps<{
  date: string;
  attractions: RouteAttraction[];
}>();

const emit = defineEmits<{
  (e: 'showRoute', date: string, attractions: RouteAttraction[]): void;
  (e: 'addAttraction', date: string): void;
  (e: 'deleteAttraction', date: string, routeId: number): void;
}>();

const route = useRoute();
const planStore = usePlanStore();
const isDragging = ref(false);

/**
 * 현재 라우트가 여행지 검색 페이지인지 확인
 */
const isAttractionSearchPage = computed(() => {
  return route.name === ROUTES.PLAN_MODIFY.name;
});

// 로컬 상태로 관리하는 attractions 배열
const localAttractions = ref<RouteAttraction[]>([]);

/**
 * planStore에서 데이터가 변경될 때 로컬 상태 업데이트
 */
const updateLocalAttractions = () => {
  const storeAttractions = planStore.currentPlan?.details?.[props.date];
  if (storeAttractions && Array.isArray(storeAttractions)) {
    localAttractions.value = [...storeAttractions].sort((a, b) => a.order - b.order);
  } else if (props.attractions && Array.isArray(props.attractions)) {
    localAttractions.value = [...props.attractions].sort((a, b) => a.order - b.order);
  } else {
    localAttractions.value = [];
  }
};

// 초기 데이터 설정 및 store 변경 감지
watch(
  () => [planStore.currentPlan?.details?.[props.date], props.attractions],
  () => {
    if (!isDragging.value) {
      updateLocalAttractions();
    }
  },
  { immediate: true, deep: true }
);

/**
 * 지도에서 해당 날짜의 경로를 표시합니다.
 */
const showRouteOnMap = () => {
  const currentAttractions = planStore.currentPlan?.details?.[props.date] || props.attractions;
  emit('showRoute', props.date, currentAttractions);
};

/**
 * 새로운 여행지 추가 이벤트를 emit합니다.
 */
const handleAddAttraction = () => {
  console.log(props.date);
  emit('addAttraction', props.date);
};

/**
 * 여행지 삭제 이벤트를 emit합니다.
 * @param routeId - 삭제할 여행지 ID
 */
const handleDeleteAttraction = (routeId: number) => {
  emit('deleteAttraction', props.date, routeId);
};

/**
 * 드래그 시작 시 플래그를 설정합니다.
 */
const handleDragStart = () => {
  isDragging.value = true;
};

/**
 * 드래그로 인한 순서 변경을 처리합니다.
 */
const handleOrderChange = () => {
  isDragging.value = false;

  // 현재 localAttractions의 순서를 기반으로 order 재설정
  const reorderedAttractions = localAttractions.value.map((attraction, index) => ({
    ...attraction,
    order: index + 1,
  }));

  // planStore 업데이트
  planStore.reorderAttractions(props.date, reorderedAttractions);

  // 변경된 순서로 경로 다시 표시
  emit('showRoute', props.date, reorderedAttractions);
};
</script>

<style scoped>
.sortable-ghost {
  opacity: 0.5;
  background-color: rgba(147, 51, 234, 0.2);
}

.sortable-chosen {
  border: 2px solid rgba(147, 51, 234, 0.5);
  background-color: rgba(147, 51, 234, 0.2);
}

.sortable-drag {
  transform: scale(1.05);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.3);
  background-color: rgba(255, 255, 255, 0.1);
}
</style>
