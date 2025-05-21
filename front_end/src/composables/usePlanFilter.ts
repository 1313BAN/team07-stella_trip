import { ref } from 'vue';
import PlanFilter from '@/components/views/plan/PlanFilter.vue';

type SortOption = 'RECENT' | 'POPULAR';

interface FilterParams {
  search?: string;
  userName?: string;
  duration?: number;
  sort?: SortOption;
}

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
