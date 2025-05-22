import { ref } from 'vue';
import type AttractionFilter from '@/components/views/attraction/AttractionFilter.vue';

/**
 * 관광지 필터링 기능을 관리
 *
 * @returns
 * - `filterComponentRef`: 필터 컴포넌트 참조
 * - `handleFilterChange`: 필터 조건 변경 처리 함수
 * - `closeFilterPanel`: 필터 패널 닫기 함수
 */
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
