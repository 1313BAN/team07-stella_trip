import { ref } from 'vue';
import PlanFilter from '@/components/views/plan/PlanFilter.vue';

type SortOption = 'RECENT' | 'POPULAR';

interface FilterParams {
  search?: string;
  userName?: string;
  duration?: number;
  sort?: SortOption;
}

/**
 * 여행 계획 필터링 기능을 관리하는 컴포저블
 *
 * @returns
 * - `filterComponentRef`: 필터 컴포넌트 참조
 * - `currentFilters`: 현재 적용된 필터 조건
 * - `handleFilterChange`: 필터 변경 처리 함수
 * - `closeFilterPanel`: 필터 패널 닫기 함수
 */
export function usePlanFilter() {
  const filterComponentRef = ref<InstanceType<typeof PlanFilter> | null>(null);
  const currentFilters = ref<FilterParams>({
    sort: 'RECENT',
  });

  const handleFilterChange = (filters: FilterParams) => {
    currentFilters.value = filters;
    // TODO: 여기서 필터링된 데이터를 가져오는 API 호출 등의 작업을 수행할 수 있습니다.
  };

  const closeFilterPanel = () => {
    if (filterComponentRef.value) {
      filterComponentRef.value.closeFilterPanel();
    }
  };

  return {
    filterComponentRef,
    currentFilters,
    handleFilterChange,
    closeFilterPanel,
  };
}
