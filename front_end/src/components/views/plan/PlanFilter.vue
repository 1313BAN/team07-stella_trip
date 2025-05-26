<template>
  <div ref="containerRef">
    <!-- 검색 필드 -->
    <div class="relative mx-4">
      <div class="absolute inset-0 -z-10 rounded-md bg-slate-900/80 backdrop-blur-md"></div>
      <Input
        v-model="searchQuery"
        placeholder="여행 계획 검색"
        class="relative h-11 border-purple-400/50 bg-slate-800/70 pr-10 text-base text-white placeholder-purple-200/70 shadow-md backdrop-blur-md"
        @focus="isSearchFocused = true"
        @keyup.enter="handleSearch"
      />
      <Button
        variant="ghost"
        size="icon"
        class="absolute top-0 right-0 h-full text-purple-400 hover:text-purple-300"
        @click="handleSearch"
      >
        <Search class="h-5 w-5" />
      </Button>
    </div>

    <!-- 펼쳐지는 필터 영역 -->
    <div
      class="overflow-hidden px-4 transition-all duration-300"
      :class="isSearchFocused ? 'max-h-130 opacity-100' : 'max-h-0 opacity-0'"
    >
      <Card
        class="mt-2 space-y-4 border-purple-500/20 bg-slate-900/90 p-4 text-white shadow-md backdrop-blur-md"
      >
        <div class="space-y-4">
          <Label class="text-sm font-medium text-purple-200/80">여행 기간</Label>
          <DualSlider
            v-model="rangeValue"
            :min="0"
            :max="30"
            :step="1"
            :minStepsBetweenThumbs="1"
            class="slider-purple"
          />
          <div>
            여행 기간:
            {{ formatDurationLabel(rangeValue[0]) }}
            -
            {{ formatDurationLabel(rangeValue[1] - 1) }}
          </div>
        </div>

        <!-- 정렬 방식 선택 -->
        <div class="space-y-2">
          <Label class="text-sm font-medium text-purple-200/80">정렬 방식</Label>
          <div class="flex flex-wrap gap-2">
            <Badge
              v-for="(label, value) in sortOptions"
              :key="value"
              :variant="selectedSort === value ? 'default' : 'outline'"
              class="cursor-pointer border-purple-400/30 bg-slate-800 py-1.5 text-sm text-white transition-all hover:bg-slate-700"
              :class="{
                'border-purple-500/50 bg-purple-600 text-white hover:bg-purple-500':
                  selectedSort === value,
              }"
              @click="selectedSort = value"
            >
              {{ label }}
            </Badge>
          </div>
        </div>

        <!-- 작성자 이름 입력 -->
        <div class="space-y-2">
          <Label class="text-sm font-medium text-purple-200/80">작성자</Label>
          <Input
            v-model="userName"
            placeholder="작성자 이름"
            class="border-purple-400/50 bg-slate-800/70 text-white placeholder-purple-200/70"
          />
        </div>

        <!-- 필터 적용/초기화 버튼 -->
        <div class="flex items-center justify-between pt-2">
          <Button
            variant="outline"
            size="sm"
            class="border-purple-400/40 bg-slate-800 text-white hover:bg-purple-500/20 hover:text-white"
            @click="resetFilters"
          >
            초기화
          </Button>
          <Button
            size="sm"
            class="bg-purple-600 text-white hover:bg-purple-500"
            @click="handleSearch"
          >
            필터 적용
          </Button>
        </div>
      </Card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watch, type PropType } from 'vue';
import { Search } from 'lucide-vue-next';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Card } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Label } from '@/components/ui/label';
import { DualSlider } from '@/components/ui/slider';
import { type PlansParams } from '@/services/api/domains/plan';
import { type PlansSortOption } from '@/services/api/domains/plan/types';

const props = defineProps({
  filters: {
    type: Object as PropType<PlansParams>,
    default: () => ({}),
  },
  parentScrollContainer: {
    type: Object as PropType<HTMLElement | null>,
    default: null,
  },
  isScrollingDown: {
    type: Boolean,
    default: false,
  },
  scrollY: {
    type: Number,
    default: 0,
  },
});

// // 여행 기간 옵션 (일 수)
// const durationOptions = [0, 1, 2, 3, 7, 14];

// 정렬 옵션
const sortOptions = {
  recent: '최신순',
  like: '인기순',
};

const isSearchFocused = ref(false);
const containerRef = ref<HTMLElement | null>(null);
const searchQuery = ref(props.filters.search || '');
const userName = ref(props.filters.userName || '');
const selectedSort = ref<PlansSortOption>(props.filters.sort || 'recent');
const rangeValue = ref([props.filters.minDuration || 0, props.filters.maxDuration || 30]);
const emit = defineEmits<{
  (e: 'filter', filters: PlansParams): void;
  (e: 'clearFilter'): void;
}>();

// 스크롤 상태 변화 감지
watch([() => props.isScrollingDown, () => props.scrollY], ([newIsScrollingDown, newScrollY]) => {
  if (newIsScrollingDown && newScrollY > 30 && isSearchFocused.value) {
    isSearchFocused.value = false;
  }
});

// 외부 클릭 감지용 이벤트 핸들러
const handleClickOutside = (event: MouseEvent) => {
  if (
    containerRef.value &&
    !containerRef.value.contains(event.target as Node) &&
    isSearchFocused.value
  ) {
    //closePanel();
    if (isSearchFocused.value) {
      isSearchFocused.value = false;
    }
  }
};

onMounted(() => {
  document.addEventListener('mousedown', handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener('mousedown', handleClickOutside);
});

watch(props.filters, newFilters => {
  searchQuery.value = newFilters.search || '';
  userName.value = newFilters.userName || '';
  selectedSort.value = newFilters.sort || 'recent';
  rangeValue.value = [newFilters.minDuration || 0, newFilters.maxDuration || 30];
});

// 여행 기간 포맷
const formatDurationLabel = (days: number) => {
  if (days === 0) return '당일치기';
  return `${days}박 ${days + 1}일`;
};

// 필터 초기화
const resetFilters = () => {
  searchQuery.value = '';
  userName.value = '';
  selectedSort.value = 'recent';
  rangeValue.value = [0, 30];
  emit('clearFilter');
};

// 검색 핸들러
const handleSearch = () => {
  applyFilters();
};

const closeFilterPanel = () => {
  if (isSearchFocused.value) {
    isSearchFocused.value = false;
  }
};

// 부모 컴포넌트에 메소드 노출
defineExpose({
  closeFilterPanel,
});

// 필터 적용
const applyFilters = () => {
  const filters: PlansParams = {
    sort: selectedSort.value,
  };

  if (searchQuery.value) {
    filters.search = searchQuery.value;
  }

  if (userName.value) {
    filters.userName = userName.value;
  }

  filters.maxDuration = rangeValue.value[1];
  filters.minDuration = rangeValue.value[0];

  emit('filter', filters);
  isSearchFocused.value = false;
};
</script>
