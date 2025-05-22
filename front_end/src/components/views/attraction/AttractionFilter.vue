<template>
  <div ref="containerRef">
    <!-- 검색 필드 -->
    <div class="relative mx-4">
      <div class="absolute inset-0 -z-10 rounded-md bg-slate-900/80 backdrop-blur-md"></div>
      <Input
        v-model="searchQuery"
        placeholder="관광지 검색"
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
      :class="isSearchFocused ? 'max-h-96 opacity-100' : 'max-h-0 opacity-0'"
    >
      <Card
        class="mt-2 space-y-4 border-purple-500/20 bg-slate-900/90 p-4 text-white shadow-md backdrop-blur-md"
      >
        <!-- 시도/구군 선택 -->
        <div class="grid grid-cols-2 gap-2">
          <div class="relative">
            <Select v-model="selectedSido" @update:modelValue="onSidoChange">
              <SelectTrigger class="border-purple-500/30 bg-slate-800/90 text-white">
                <SelectValue placeholder="시/도 선택" />
              </SelectTrigger>
              <SelectContent class="border-purple-500/20 bg-slate-900 text-white">
                <SelectItem value="all" class="focus:bg-purple-500/30">전체</SelectItem>
                <!-- 시도 목록은 API에서 가져옴 -->
              </SelectContent>
            </Select>
          </div>

          <div class="relative">
            <Select v-model="selectedGugun" :disabled="!selectedSido || selectedSido === 'all'">
              <SelectTrigger class="border-purple-500/30 bg-slate-800/90 text-white">
                <SelectValue placeholder="구/군 선택" />
              </SelectTrigger>
              <SelectContent class="border-purple-500/20 bg-slate-900 text-white">
                <SelectItem value="all" class="focus:bg-purple-500/30">전체</SelectItem>
                <!-- 구군 목록은 API에서 가져옴 -->
              </SelectContent>
            </Select>
          </div>
        </div>

        <!-- 컨텐츠 타입 선택 -->
        <div class="space-y-2">
          <Label class="text-sm font-medium text-purple-200/80">관광 유형</Label>
          <div class="flex flex-wrap gap-2">
            <Badge
              v-for="(name, id) in contentTypeOptions"
              :key="id"
              :variant="selectedContentTypes.includes(Number(id)) ? 'default' : 'outline'"
              class="cursor-pointer border-purple-400/30 bg-slate-800 py-1.5 text-sm text-white transition-all hover:bg-slate-700"
              :class="{
                'border-purple-500/50 bg-purple-600 text-white hover:bg-purple-500':
                  selectedContentTypes.includes(Number(id)),
              }"
              @click="toggleContentType(Number(id))"
            >
              {{ name }}
            </Badge>
          </div>
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
            @click="applyFilters"
          >
            필터 적용
          </Button>
        </div>
      </Card>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watch, type PropType, computed } from 'vue';
import { Search } from 'lucide-vue-next';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Card } from '@/components/ui/card';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Badge } from '@/components/ui/badge';
import { Label } from '@/components/ui/label';
import { AttractionContentType } from '@/constants/constant';

const props = defineProps({
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

const emit = defineEmits<{
  (
    e: 'filter',
    filters: {
      query: string;
      sido: string;
      gugun: string;
      contentTypes: number[];
    }
  ): void;
}>();

const contentTypeOptions = computed(() => AttractionContentType);

const isSearchFocused = ref(false);
const containerRef = ref<HTMLElement | null>(null);

const searchQuery = ref('');
const selectedSido = ref('');
const selectedGugun = ref('');
const selectedContentTypes = ref<number[]>([]);

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
    closeFilterPanel();
  }
};

// 필터 패널 닫기 메소드 (외부에서 호출 가능)
const closeFilterPanel = () => {
  if (isSearchFocused.value) {
    isSearchFocused.value = false;
  }
};

// 부모 컴포넌트에 메소드 노출
defineExpose({
  closeFilterPanel,
});

onMounted(() => {
  document.addEventListener('mousedown', handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener('mousedown', handleClickOutside);
});

// 시도 변경 핸들러
const onSidoChange = () => {
  selectedGugun.value = '';
  // 여기서 선택된 시도에 따른 구군 목록을 API에서 가져올 수 있음
};

// 컨텐츠 타입 토글 핸들러
const toggleContentType = (typeId: number) => {
  if (selectedContentTypes.value.includes(typeId)) {
    selectedContentTypes.value = selectedContentTypes.value.filter(id => id !== typeId);
  } else {
    selectedContentTypes.value.push(typeId);
  }
};

// 필터 초기화
const resetFilters = () => {
  searchQuery.value = '';
  selectedSido.value = '';
  selectedGugun.value = '';
  selectedContentTypes.value = [];
};

// 검색 핸들러
const handleSearch = () => {
  applyFilters();
};

// 필터 적용
const applyFilters = () => {
  emit('filter', {
    query: searchQuery.value,
    sido: selectedSido.value === 'all' ? '' : selectedSido.value,
    gugun: selectedGugun.value === 'all' ? '' : selectedGugun.value,
    contentTypes: selectedContentTypes.value,
  });

  isSearchFocused.value = false;
};
</script>
