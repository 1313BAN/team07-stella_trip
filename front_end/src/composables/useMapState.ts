import { ref, computed, watch, type ComputedRef } from 'vue';
import type { Attraction } from '@/services/api/domains/attraction/types';
import type { Plan, PlanDetail } from '@/services/api/domains/plan';
import type { MarkerInfo, MarkerInstance, PolylineInstance } from '@/types/kakao';
import type MapContainer from '@/components/map/MapContainer.vue';
import {
  createPolyline,
  checkKakaoMapsAPI,
  SingleMarkerManager,
  MultiMarkerManager,
} from '@/utils/mapUtils';

interface UseMapStateReturn {
  // 지도 관련
  mapRef: ReturnType<typeof ref<InstanceType<typeof MapContainer> | null>>;
  mapLevel: ReturnType<typeof ref<number>>;
  mapCenter: ComputedRef<{ lat: number; lng: number }>;

  // 선택된 상태
  selectedAttraction: ReturnType<typeof ref<Attraction | null>>;
  selectedPlan: ReturnType<typeof ref<Plan | null>>;

  // 마커 관련
  singleMarker: ComputedRef<MarkerInstance | null>;
  markers: ReturnType<typeof ref<MarkerInfo[]>>;

  // 단일 마커 관리
  showSingleMarker: (lat: number, lng: number) => void;
  hideSingleMarker: () => void;
  updateSingleMarker: (lat: number, lng: number, show?: boolean) => void;

  // 다중 마커 관리
  showAllMarkers: () => boolean;
  clearMarkers: () => void;

  // 경로 관리
  showRoute: (dateAttractions: MarkerInfo[]) => boolean;
  clearPolylines: () => void;

  // 비즈니스 로직
  selectAttraction: (attraction: Attraction) => void;
  selectPlan: (plan: Plan) => void;
  selectPlanDetail: (planDetail: PlanDetail) => void;
}

// 싱글톤 상태 관리
const mapRef = ref<InstanceType<typeof MapContainer> | null>(null);
const selectedAttraction = ref<Attraction | null>(null);
const selectedPlan = ref<Plan | null>(null);
const defaultCenter = { lat: 37.5665, lng: 126.978 };
const mapLevel = ref(7);
const markers = ref<MarkerInfo[]>([]);

// 마커 관리자들
let singleMarkerManager: SingleMarkerManager | null = null;
let multiMarkerManager: MultiMarkerManager | null = null;
const polylines = ref<PolylineInstance[]>([]);

// 매니저들 초기화
const initializeManagers = () => {
  if (mapRef.value?.map) {
    if (!singleMarkerManager) {
      singleMarkerManager = new SingleMarkerManager(mapRef.value.map);
    }
    if (!multiMarkerManager) {
      multiMarkerManager = new MultiMarkerManager(mapRef.value.map);
    }
  }
};

/**
 * 단일 마커 표시
 */
const showSingleMarker = (lat: number, lng: number): void => {
  if (!mapRef.value?.map || !checkKakaoMapsAPI()) {
    throw new Error('지도가 초기화되지 않았거나 카카오맵 API를 사용할 수 없습니다.');
  }

  initializeManagers();

  // 다중 마커는 숨기고 단일 마커만 표시
  multiMarkerManager?.clearAll();
  mapRef.value?.hideMarker();

  singleMarkerManager?.showMarker(lat, lng);
};

/**
 * 단일 마커 숨김
 */
const hideSingleMarker = (): void => {
  singleMarkerManager?.hideMarker();
};

/**
 * 단일 마커 위치 업데이트
 */
const updateSingleMarker = (lat: number, lng: number, show: boolean = true): void => {
  if (!mapRef.value?.map) {
    throw new Error('지도가 초기화되지 않았습니다.');
  }

  initializeManagers();
  singleMarkerManager?.updateMarker(lat, lng, show);
};

/**
 * 모든 마커 및 정보창 제거
 */
const clearMarkers = (): void => {
  mapRef.value?.hideMarker();
  singleMarkerManager?.hideMarker();
  multiMarkerManager?.clearAll();
  markers.value = [];
};

/**
 * 모든 폴리라인 제거
 */
const clearPolylines = (): void => {
  polylines.value.forEach(line => line.setMap(null));
  polylines.value = [];
};

/**
 * 관광지 선택 처리 - 단일 마커 사용
 */
const selectAttraction = (attraction: Attraction): void => {
  if (!attraction) {
    throw new Error('유효하지 않은 관광지 정보입니다.');
  }

  selectedAttraction.value = attraction;
  selectedPlan.value = null;
  clearMarkers();

  if (attraction.latitude !== undefined && attraction.longitude !== undefined && mapRef.value) {
    mapRef.value.panTo(attraction.latitude, attraction.longitude);
    mapRef.value.setLevel(3);
    showSingleMarker(attraction.latitude, attraction.longitude);
  }
};

/**
 * 여행 계획 선택 처리
 */
const selectPlan = (plan: Plan): void => {
  if (!plan) {
    throw new Error('유효하지 않은 여행 계획 정보입니다.');
  }

  selectedPlan.value = plan;
  selectedAttraction.value = null;
};

/**
 * 여행 계획 상세 정보 처리 및 다중 마커 표시
 */
const selectPlanDetail = (planDetail: PlanDetail): void => {
  if (!planDetail) {
    throw new Error('유효하지 않은 여행 계획 상세 정보입니다.');
  }

  clearMarkers();
  initializeManagers();

  const allAttractions: MarkerInfo[] = [];

  if (planDetail.details) {
    Object.entries(planDetail.details).forEach(([date, attractions]) => {
      attractions.forEach(attraction => {
        const lat = parseFloat(String(attraction.latitude));
        const lng = parseFloat(String(attraction.longitude));

        if (!isNaN(lat) && !isNaN(lng)) {
          allAttractions.push({
            lat,
            lng,
            name: attraction.name,
            order: attraction.order,
            date: date,
          });
        }
      });
    });
  }

  markers.value = allAttractions;

  if (markers.value.length > 0 && multiMarkerManager) {
    multiMarkerManager.addMarkers(markers.value);
    multiMarkerManager.fitMapBounds();

    if (mapRef.value?.map) {
      mapLevel.value = mapRef.value.map.getLevel();
    }
  }
};

/**
 * 다중 마커 표시
 */
const showAllMarkers = (): boolean => {
  if (!mapRef.value?.map || markers.value.length === 0 || !checkKakaoMapsAPI()) {
    return false;
  }

  initializeManagers();

  // 단일 마커는 숨기고 다중 마커만 표시
  mapRef.value.hideMarker();
  singleMarkerManager?.hideMarker();

  multiMarkerManager?.clearAll();
  multiMarkerManager?.addMarkers(markers.value);
  multiMarkerManager?.fitMapBounds();

  return true;
};

/**
 * 경로를 폴리라인으로 표시
 */
const showRoute = (dateAttractions: MarkerInfo[]): boolean => {
  if (!mapRef.value?.map || !checkKakaoMapsAPI()) {
    return false;
  }

  clearPolylines();

  const sortedAttractions = [...dateAttractions].sort((a, b) => (a.order || 0) - (b.order || 0));

  if (sortedAttractions.length < 2) {
    return false;
  }

  try {
    const polyline = createPolyline(sortedAttractions, mapRef.value.map);
    polylines.value.push(polyline);
    return true;
  } catch (error) {
    throw new Error(`폴리라인 생성 실패: ${error}`);
  }
};

/**
 * 지도 중심점 계산
 */
const mapCenter = computed((): { lat: number; lng: number } => {
  if (
    selectedAttraction.value?.latitude !== undefined &&
    selectedAttraction.value?.longitude !== undefined
  ) {
    return {
      lat: selectedAttraction.value.latitude,
      lng: selectedAttraction.value.longitude,
    };
  }

  return defaultCenter;
});

/**
 * 현재 단일 마커 인스턴스 반환
 */
const singleMarker = computed(() => {
  return singleMarkerManager?.getMarker() || null;
});

// 지도 참조 변경 감지
watch(
  () => mapRef.value?.map,
  newMap => {
    if (newMap) {
      // 매니저들 재초기화
      singleMarkerManager = new SingleMarkerManager(newMap);
      multiMarkerManager = new MultiMarkerManager(newMap);

      // 기존 마커들이 있다면 다시 표시
      if (markers.value.length > 0) {
        showAllMarkers();
      }
    }
  }
);

/**
 * 지도 전역 상태 관리 컴포저블
 * SingleMarkerManager: 개별 관광지 선택
 * MultiMarkerManager: 여행 계획 다중 마커 관리
 */
export function useMapState(): UseMapStateReturn {
  return {
    // 지도 관련
    mapRef,
    mapLevel,
    mapCenter,

    // 선택된 상태
    selectedAttraction,
    selectedPlan,

    // 마커 관련
    singleMarker,
    markers,

    // 단일 마커 관리
    showSingleMarker,
    hideSingleMarker,
    updateSingleMarker,

    // 다중 마커 관리
    showAllMarkers,
    clearMarkers,

    // 경로 관리
    showRoute,
    clearPolylines,

    // 비즈니스 로직
    selectAttraction,
    selectPlan,
    selectPlanDetail,
  };
}
