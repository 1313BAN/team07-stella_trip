import { ref } from 'vue';
import type AttractionFilter from '@/components/views/attraction/AttractionFilter.vue';

export function useAttractionFilter() {
  const filterComponentRef = ref<InstanceType<typeof AttractionFilter> | null>(null);

  const handleFilterChange = (newFilters: {
    query: string;
    sido: string;
    gugun: string;
    contentTypes: number[];
  }) => {
    console.log('필터 적용:', newFilters);
    // 여기서 필터링된 데이터를 가져오는 API 호출 등을 수행할 수 있습니다.
    // 예: fetchFilteredAttractions(newFilters);
  };

  const closeFilterPanel = () => {
    if (filterComponentRef.value) {
      filterComponentRef.value.closeFilterPanel();
    }
  };

  return {
    filterComponentRef,
    handleFilterChange,
    closeFilterPanel,
  };
}
