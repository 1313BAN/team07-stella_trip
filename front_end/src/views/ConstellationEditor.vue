<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { usePlanStore } from '@/stores/plan';
import { getShareLink } from '@/services/api/domains/stella'; // getShareLink API 함수 import
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Eye, EyeOff, Trash2, X, Save, Info, Loader2 } from 'lucide-vue-next';

interface TravelSpot {
  id: number;
  name: string;
  x: number;
  y: number;
  visible: boolean;
  originalIndex: number; // 원본 인덱스 추가
}

interface Edge {
  id: number;
  from: number;
  to: number;
}

const route = useRoute();
const router = useRouter();
const planStore = usePlanStore();

const selectedNodes = ref<number[]>([]);
const selectedEdge = ref<number | null>(null);
const travelSpots = ref<TravelSpot[]>([]);
const edges = ref<Edge[]>([]);
const shareLink = ref<string>(''); // 공유 링크 저장용
const isSaving = ref<boolean>(false); // 저장 상태 추적

const planId = computed(() => Number(route.params.planId));

const convertPlanToTravelSpots = () => {
  if (!planStore.currentPlan?.stella?.nodes) return;

  const spots: TravelSpot[] = [];
  planStore.currentPlan.stella.nodes.forEach((node, index) => {
    spots.push({
      id: index,
      name: node.name,
      x: node.x,
      y: node.y,
      visible: true,
      originalIndex: index, // 원본 인덱스 저장
    });
  });

  travelSpots.value = spots;

  if (planStore.currentPlan.stella.edges) {
    edges.value = planStore.currentPlan.stella.edges.map((edge, index) => ({
      id: index,
      from: edge.from,
      to: edge.to,
    }));
  }
};

const convertToSvgCoord = (coord: number) => {
  return (coord / 500) * 80 + 10;
};

const svgSpots = computed(() =>
  travelSpots.value.map(spot => ({
    ...spot,
    svgX: convertToSvgCoord(spot.x),
    svgY: convertToSvgCoord(spot.y),
  }))
);

const visibleSpots = computed(() => svgSpots.value.filter(spot => spot.visible));

const validEdges = computed(() =>
  edges.value.filter(edge => {
    const fromSpot = travelSpots.value.find(s => s.id === edge.from);
    const toSpot = travelSpots.value.find(s => s.id === edge.to);
    return fromSpot?.visible && toSpot?.visible;
  })
);

const edgeList = computed(() =>
  validEdges.value.map(edge => ({
    ...edge,
    fromName: travelSpots.value.find(s => s.id === edge.from)?.name || '알 수 없음',
    toName: travelSpots.value.find(s => s.id === edge.to)?.name || '알 수 없음',
  }))
);

const checkLabelOverlap = (spot: any, allSpots: any[]) => {
  const threshold = 8;
  const overlapping = allSpots.filter(
    other =>
      other.id !== spot.id &&
      Math.abs(other.svgX - spot.svgX) < threshold &&
      Math.abs(other.svgY - spot.svgY) < threshold
  );
  return overlapping.length > 0;
};

const handleSpotClick = (event: MouseEvent, spotId: number) => {
  event.stopPropagation();
  selectedEdge.value = null;

  if (selectedNodes.value.includes(spotId)) {
    selectedNodes.value = selectedNodes.value.filter(id => id !== spotId);
  } else {
    selectedNodes.value.push(spotId);

    if (selectedNodes.value.length === 2) {
      addEdge(selectedNodes.value[0], selectedNodes.value[1]);
      // 연결선 생성 후 약간의 지연을 두고 선택 해제 (시각적 피드백을 위해)
      setTimeout(() => {
        selectedNodes.value = [];
      }, 200);
    }
  }
};

const handleEdgeClick = (event: MouseEvent | TouchEvent, edgeId: number) => {
  event.stopPropagation();
  selectedNodes.value = [];
  selectedEdge.value = selectedEdge.value === edgeId ? null : edgeId;
};

const handleCanvasClick = () => {
  selectedNodes.value = [];
  selectedEdge.value = null;
};

const addEdge = (fromId: number, toId: number) => {
  const exists = edges.value.some(
    edge => (edge.from === fromId && edge.to === toId) || (edge.from === toId && edge.to === fromId)
  );

  if (!exists && fromId !== toId) {
    const newId = Math.max(...edges.value.map(e => e.id), -1) + 1;
    edges.value.push({ id: newId, from: fromId, to: toId });
  }
};

const deleteEdge = (edgeId: number) => {
  edges.value = edges.value.filter(e => e.id !== edgeId);
  selectedEdge.value = null;
};

const deleteAllEdges = () => {
  edges.value = [];
  selectedEdge.value = null;
};

const toggleSpotVisibility = (spotId: number) => {
  const spot = travelSpots.value.find(s => s.id === spotId);
  if (spot) {
    spot.visible = !spot.visible;
  }
};

const toggleAllSpots = (visible: boolean) => {
  travelSpots.value.forEach(spot => (spot.visible = visible));
};

const saveConstellation = async () => {
  try {
    isSaving.value = true;

    if (!planStore.currentPlan) {
      console.error('현재 플랜 데이터가 없습니다.');
      return;
    }

    // visible이 true인 노드만 필터링하고 새로운 인덱스 매핑 생성
    const visibleNodes = travelSpots.value.filter(spot => spot.visible);

    // 원본 ID를 새로운 인덱스로 매핑하는 객체 생성
    const idMapping: { [oldId: number]: number } = {};
    visibleNodes.forEach((spot, newIndex) => {
      idMapping[spot.id] = newIndex;
    });

    console.log('ID 매핑:', idMapping);
    console.log('원본 간선:', edges.value);

    // 간선 데이터 변환: 원본 ID를 새로운 인덱스로 매핑
    const validEdgesForSave = edges.value
      .filter(edge => {
        // 양 끝 노드가 모두 visible인 간선만 포함
        const fromSpot = travelSpots.value.find(s => s.id === edge.from);
        const toSpot = travelSpots.value.find(s => s.id === edge.to);
        const isValid = fromSpot?.visible && toSpot?.visible;

        if (!isValid) {
          console.log(`간선 ${edge.id} 제외됨: from=${edge.from}, to=${edge.to}`);
        }

        return isValid;
      })
      .map(edge => {
        const mappedEdge = {
          from: idMapping[edge.from],
          to: idMapping[edge.to],
        };

        console.log(`간선 변환: ${edge.from}->${edge.to} => ${mappedEdge.from}->${mappedEdge.to}`);

        return mappedEdge;
      })
      .filter(
        edge =>
          // 매핑된 인덱스가 유효한지 확인
          edge.from !== undefined &&
          edge.to !== undefined &&
          edge.from >= 0 &&
          edge.to >= 0 &&
          edge.from < visibleNodes.length &&
          edge.to < visibleNodes.length
      );

    console.log('저장될 간선:', validEdgesForSave);

    // planStore.currentPlan을 복사하여 stella 데이터만 업데이트
    const updatedPlan = {
      ...planStore.currentPlan,
      stella: {
        nodes: visibleNodes.map(spot => ({
          name: spot.name,
          x: spot.x,
          y: spot.y,
        })),
        edges: validEdgesForSave,
      },
    };

    console.log('저장될 데이터:', updatedPlan.stella);

    // planStore의 currentPlan 업데이트
    planStore.currentPlan = updatedPlan;

    const stellaData = JSON.stringify(planStore.currentPlan);

    const response = await getShareLink({
      planId: planStore.currentPlan.planId,
      stellaData,
    });

    shareLink.value = `${window.location.origin}/shared/${response.stellaLink}`;

    console.log('별자리 데이터 저장 완료:', shareLink.value);

    // 공유링크 복사
    await copyToClipboard(shareLink.value);
  } catch (error) {
    console.error('공유 링크 생성 실패:', error);
  } finally {
    isSaving.value = false;
  }
};

const copyToClipboard = async (text: string) => {
  try {
    await navigator.clipboard.writeText(text);
    console.log('공유링크가 클립보드에 복사되었습니다:', text);
    // 성공 알림을 위해 잠시 버튼 텍스트 변경
    setTimeout(() => {
      router.back();
    }, 1500);
  } catch (error) {
    console.error('클립보드 복사 실패:', error);
    // 클립보드 복사 실패 시에도 뒤로가기
    router.back();
  }
};

// 정적 배경 별들 생성
const generateStaticStars = () => {
  const stars = [];
  for (let i = 0; i < 200; i++) {
    stars.push({
      id: i,
      left: Math.random() * 100,
      top: Math.random() * 100,
      size: Math.random() * 3 + 1,
      opacity: Math.random() * 0.8 + 0.2,
    });
  }
  return stars;
};

const backgroundStars = generateStaticStars();

onMounted(async () => {
  if (planStore.currentPlan && planStore.currentPlan.planId === planId.value) {
    convertPlanToTravelSpots();
  } else {
    console.warn('Plan data not found in store. Please load plan data first.');
    console.log('Available plan data:', planStore.currentPlan);
  }
});

watch(
  () => planStore.currentPlan,
  () => {
    if (planStore.currentPlan?.stella?.nodes) {
      convertPlanToTravelSpots();
    }
  },
  { deep: true }
);
</script>

<template>
  <TooltipProvider>
    <div
      class="flex h-[calc(100vh-4rem)] bg-gradient-to-br from-slate-950 via-blue-950 to-slate-900"
    >
      <!-- 컨트롤 패널 -->
      <div
        class="flex w-96 flex-col overflow-hidden border-r border-slate-800/50 bg-slate-950/90 backdrop-blur-sm"
      >
        <!-- 헤더 -->
        <div
          class="flex flex-shrink-0 items-center justify-between border-b border-slate-800/50 p-6"
        >
          <div class="flex items-center gap-3">
            <div
              class="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-blue-500 to-purple-600"
            >
              <svg class="h-4 w-4 text-white" fill="currentColor" viewBox="0 0 20 20">
                <path
                  d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"
                ></path>
              </svg>
            </div>
            <h1 class="text-xl font-semibold text-white">별자리 편집기</h1>
          </div>
          <Button
            variant="ghost"
            size="icon"
            @click="router.back()"
            class="text-slate-400 hover:text-white"
          >
            <X class="h-5 w-5" />
          </Button>
        </div>

        <!-- 스크롤 가능한 콘텐츠 영역 -->
        <div class="flex-1 overflow-y-auto">
          <!-- 여행지 섹션 -->
          <div class="px-6 py-6">
            <Card class="border-slate-800 bg-slate-900/50">
              <CardHeader class="pb-3">
                <div class="flex items-center justify-between">
                  <CardTitle class="flex items-center gap-2 text-lg text-white">
                    <div class="h-2 w-2 rounded-full bg-blue-500"></div>
                    여행지
                  </CardTitle>
                  <div class="flex gap-1">
                    <Button
                      size="sm"
                      variant="outline"
                      @click="toggleAllSpots(true)"
                      class="h-7 border-green-500/30 bg-green-500/10 px-2 text-xs text-green-400 hover:bg-green-500/20"
                    >
                      <Eye class="mr-1 h-3 w-3" />
                      모두 표시
                    </Button>
                    <Button
                      size="sm"
                      variant="outline"
                      @click="toggleAllSpots(false)"
                      class="h-7 border-red-500/30 bg-red-500/10 px-2 text-xs text-red-400 hover:bg-red-500/20"
                    >
                      <EyeOff class="mr-1 h-3 w-3" />
                      모두 숨김
                    </Button>
                  </div>
                </div>
              </CardHeader>
              <CardContent class="space-y-2 pt-0">
                <div
                  v-for="spot in travelSpots"
                  :key="spot.id"
                  class="flex cursor-pointer items-center gap-3 rounded-lg border p-3 transition-all duration-200"
                  :class="[
                    selectedNodes.includes(spot.id)
                      ? 'border-blue-500/50 bg-blue-500/20 ring-1 ring-blue-500/50'
                      : 'border-slate-700 bg-slate-800/50 hover:bg-slate-800/80',
                  ]"
                  @click="handleSpotClick($event, spot.id)"
                >
                  <Button
                    variant="ghost"
                    size="icon"
                    @click.stop="toggleSpotVisibility(spot.id)"
                    class="h-6 w-6 shrink-0"
                    :class="
                      spot.visible
                        ? 'text-green-400 hover:text-green-300'
                        : 'text-slate-500 hover:text-slate-400'
                    "
                  >
                    <Eye v-if="spot.visible" class="h-4 w-4" />
                    <EyeOff v-else class="h-4 w-4" />
                  </Button>
                  <div class="min-w-0 flex-1">
                    <div class="truncate font-medium text-white">{{ spot.name }}</div>
                    <div class="text-xs text-slate-400">
                      ({{ Math.round(spot.x) }}, {{ Math.round(spot.y) }})
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          <!-- 연결선 섹션 -->
          <div class="px-6 pb-4">
            <Card class="border-slate-800 bg-slate-900/50">
              <CardHeader class="pb-3">
                <div class="flex items-center justify-between">
                  <CardTitle class="flex items-center gap-2 text-lg text-white">
                    <div class="h-2 w-2 rounded-full bg-purple-500"></div>
                    연결선
                  </CardTitle>
                  <Button
                    v-if="edgeList.length > 0"
                    size="sm"
                    variant="outline"
                    @click="deleteAllEdges"
                    class="h-7 border-red-500/30 bg-red-500/10 px-2 text-xs text-red-400 hover:bg-red-500/20"
                  >
                    <Trash2 class="mr-1 h-3 w-3" />
                    모두 삭제
                  </Button>
                </div>
              </CardHeader>
              <CardContent class="space-y-2 pt-0">
                <div v-if="edgeList.length === 0" class="py-8 text-center text-slate-400">
                  <div
                    class="mx-auto mb-3 flex h-12 w-12 items-center justify-center rounded-full bg-slate-800"
                  >
                    <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"
                      ></path>
                    </svg>
                  </div>
                  <p class="text-sm">연결선이 없습니다</p>
                  <p class="mt-1 text-xs text-slate-500">여행지를 클릭하여 연결해보세요</p>
                </div>

                <div
                  v-for="edge in edgeList"
                  :key="edge.id"
                  class="group flex cursor-pointer items-center gap-3 rounded-lg border p-3 transition-all duration-200"
                  :class="[
                    selectedEdge === edge.id
                      ? 'border-orange-500/50 bg-orange-500/20 ring-1 ring-orange-500/50'
                      : 'border-slate-700 bg-slate-800/50 hover:bg-slate-800/80',
                  ]"
                  @click="selectedEdge = selectedEdge === edge.id ? null : edge.id"
                >
                  <div class="min-w-0 flex-1">
                    <div class="flex items-center gap-2 text-sm text-white">
                      <span class="truncate">{{ edge.fromName }}</span>
                      <svg
                        class="h-4 w-4 shrink-0 text-slate-400"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          stroke-width="2"
                          d="M9 5l7 7-7 7"
                        ></path>
                      </svg>
                      <span class="truncate">{{ edge.toName }}</span>
                    </div>
                  </div>
                  <Button
                    v-if="selectedEdge === edge.id"
                    variant="ghost"
                    size="icon"
                    @click.stop="deleteEdge(edge.id)"
                    class="h-6 w-6 shrink-0 text-red-400 hover:bg-red-500/20 hover:text-red-300"
                  >
                    <Trash2 class="h-4 w-4" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>

        <!-- 고정된 액션 버튼 -->
        <div class="flex-shrink-0 border-t border-slate-800/50 bg-slate-950/90 p-6">
          <Button @click="saveConstellation" :disabled="isSaving" class="h-11 w-full" size="lg">
            <Loader2 v-if="isSaving" class="mr-2 h-4 w-4 animate-spin" />
            <Save v-else class="mr-2 h-4 w-4" />
            {{ isSaving ? '링크 생성 중...' : '저장 및 공유' }}
          </Button>
        </div>
      </div>

      <!-- 별자리 캔버스 -->
      <div class="constellation-canvas relative flex-1 overflow-hidden">
        <!-- 배경 별들 -->
        <div class="absolute inset-0">
          <div
            v-for="star in backgroundStars"
            :key="`bg-star-${star.id}`"
            class="absolute rounded-full bg-white"
            :style="{
              left: `${star.left}%`,
              top: `${star.top}%`,
              width: `${star.size}px`,
              height: `${star.size}px`,
              opacity: star.opacity,
            }"
          />
        </div>

        <!-- 별자리 SVG -->
        <svg
          class="absolute inset-0 h-full w-full cursor-pointer"
          viewBox="0 0 100 100"
          @click="handleCanvasClick"
        >
          <defs>
            <filter id="glow">
              <feGaussianBlur stdDeviation="0.5" result="coloredBlur" />
              <feMerge>
                <feMergeNode in="coloredBlur" />
                <feMergeNode in="SourceGraphic" />
              </feMerge>
            </filter>
          </defs>

          <g class="edges">
            <line
              v-for="edge in validEdges"
              :key="`edge-click-${edge.id}`"
              :x1="svgSpots.find(s => s.id === edge.from)?.svgX"
              :y1="svgSpots.find(s => s.id === edge.from)?.svgY"
              :x2="svgSpots.find(s => s.id === edge.to)?.svgX"
              :y2="svgSpots.find(s => s.id === edge.to)?.svgY"
              stroke="transparent"
              stroke-width="2"
              class="cursor-pointer"
              @click="handleEdgeClick($event, edge.id)"
            />

            <line
              v-for="edge in validEdges"
              :key="`edge-visual-${edge.id}`"
              :x1="svgSpots.find(s => s.id === edge.from)?.svgX"
              :y1="svgSpots.find(s => s.id === edge.from)?.svgY"
              :x2="svgSpots.find(s => s.id === edge.to)?.svgX"
              :y2="svgSpots.find(s => s.id === edge.to)?.svgY"
              :stroke="selectedEdge === edge.id ? '#f97316' : 'rgba(147, 197, 253, 0.8)'"
              :stroke-width="selectedEdge === edge.id ? '0.5' : '0.3'"
              class="pointer-events-none transition-all duration-300"
              filter="url(#glow)"
            />
          </g>

          <g class="nodes">
            <g v-for="spot in visibleSpots" :key="`spot-${spot.id}`">
              <circle
                :cx="spot.svgX"
                :cy="spot.svgY"
                :r="selectedNodes.includes(spot.id) ? 3 : 2.5"
                :fill="
                  selectedNodes.includes(spot.id)
                    ? 'rgba(59, 130, 246, 0.4)'
                    : 'rgba(255, 255, 255, 0.3)'
                "
                style="
                  transition:
                    r 0.3s ease,
                    fill 0.3s ease;
                "
                filter="url(#glow)"
              />

              <circle
                :cx="spot.svgX"
                :cy="spot.svgY"
                :r="selectedNodes.includes(spot.id) ? 1.5 : 1"
                :fill="selectedNodes.includes(spot.id) ? '#3b82f6' : '#ffffff'"
                class="cursor-pointer hover:fill-yellow-300"
                style="
                  transition:
                    r 0.3s ease,
                    fill 0.3s ease;
                "
                @click="handleSpotClick($event, spot.id)"
                filter="url(#glow)"
              />

              <text
                v-if="!checkLabelOverlap(spot, visibleSpots)"
                :x="spot.svgX"
                :y="spot.svgY - 5"
                text-anchor="middle"
                class="pointer-events-none fill-white font-medium opacity-90"
                style="font-size: 1.6px; text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.8)"
              >
                {{ spot.name.length > 8 ? spot.name.substring(0, 8) + '...' : spot.name }}
              </text>
            </g>
          </g>
        </svg>

        <!-- 사용법 툴팁 -->
        <div class="absolute top-6 left-6">
          <Tooltip>
            <TooltipTrigger asChild>
              <Button
                variant="outline"
                size="sm"
                class="gap-2 border-slate-700 bg-slate-800/80 text-slate-300 backdrop-blur-sm hover:bg-slate-700"
              >
                <Info class="h-4 w-4" />
                사용법
              </Button>
            </TooltipTrigger>
            <TooltipContent side="right" class="max-w-xs">
              <div class="space-y-2 text-sm">
                <p>• 두 여행지를 클릭하여 연결선 생성</p>
                <p>• 연결선을 클릭하여 선택 후 삭제</p>
                <p>• 빈 공간을 클릭하여 선택 해제</p>
              </div>
            </TooltipContent>
          </Tooltip>
        </div>
      </div>
    </div>
  </TooltipProvider>
</template>
