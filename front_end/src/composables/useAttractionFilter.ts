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
    // TODO: API 연동 시 실제 구현으로 교체
    // fetchFilteredAttractions(newFilters);
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
