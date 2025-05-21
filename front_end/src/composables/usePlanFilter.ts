import { ref } from 'vue';

type SortOption = 'RECENT' | 'POPULAR';

interface FilterParams {
  search?: string;
  userName?: string;
  duration?: number;
  sort?: SortOption;
}

export function usePlanFilter() {
  const filterComponentRef = ref(null);
  const currentFilters = ref<FilterParams>({
    sort: 'RECENT',
  });

  const handleFilterChange = (filters: FilterParams) => {
    currentFilters.value = filters;
    // TODO: 여기서 필터링된 데이터를 가져오는 API 호출 등의 작업을 수행할 수 있습니다.
  };

  const closeFilterPanel = () => {
    if (filterComponentRef.value && 'closeFilterPanel' in filterComponentRef.value) {
      (filterComponentRef.value as any).closeFilterPanel();
    }
  };

  return {
    filterComponentRef,
    currentFilters,
    handleFilterChange,
    closeFilterPanel,
  };
}
