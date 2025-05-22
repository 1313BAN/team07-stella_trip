import { ref, computed, watch, type ComputedRef, type Ref } from 'vue';
import type { Attraction } from '@/services/api/domains/attraction';
import type { Plan, PlanDetail } from '@/services/api/domains/plan';
import type {
  MarkerInfo,
  MarkerInstance,
  InfoWindowInstance,
  PolylineInstance,
} from '@/types/kakao';
import type MapContainer from '@/components/map/MapContainer.vue';
import {
  createKakaoMarker,
  createInfoWindow,
  createPolyline,
  calculateBounds,
  checkKakaoMapsAPI,
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
  singleMarker: Readonly<Ref<MarkerInstance | null>>;
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

// 카카오맵 객체들
const singleMarker = ref<MarkerInstance | null>(null);
const markersObjects = ref<MarkerInstance[]>([]);
const infoWindows = ref<InfoWindowInstance[]>([]);
const polylines = ref<PolylineInstance[]>([]);

/**
 * 단일 마커 표시
 */
const showSingleMarker = (lat: number, lng: number): void => {
  if (!mapRef.value?.map || !checkKakaoMapsAPI()) {
    throw new Error('지도가 초기화되지 않았거나 카카오맵 API를 사용할 수 없습니다.');
  }

  hideSingleMarker();

  try {
    singleMarker.value = createKakaoMarker({ lat, lng }, mapRef.value.map);
  } catch (error) {
    throw new Error(`단일 마커 생성 실패: ${error}`);
  }
};

/**
 * 단일 마커 숨김
 */
const hideSingleMarker = (): void => {
  if (singleMarker.value) {
    singleMarker.value.setMap(null);
    singleMarker.value = null;
  }
};

/**
 * 단일 마커 위치 업데이트
 */
const updateSingleMarker = (lat: number, lng: number, show: boolean = true): void => {
  if (!mapRef.value?.map) {
    throw new Error('지도가 초기화되지 않았습니다.');
  }

  if (singleMarker.value) {
    singleMarker.value.setPosition(new window.kakao.maps.LatLng(lat, lng));
    return;
  }

  if (show) {
    showSingleMarker(lat, lng);
  }
};

/**
 * 모든 마커 및 정보창 제거
 */
const clearMarkers = (): void => {
  mapRef.value?.hideMarker();
  hideSingleMarker();

  if (checkKakaoMapsAPI() && markersObjects.value.length > 0) {
    markersObjects.value.forEach(marker => marker.setMap(null));
    markersObjects.value = [];
  }

  infoWindows.value.forEach(infoWindow => infoWindow.close());
  infoWindows.value = [];
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
 * 관광지 선택 처리
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

    if (typeof mapRef.value.showMarker === 'function') {
      try {
        mapRef.value.showMarker(attraction.latitude, attraction.longitude);
      } catch {
        (mapRef.value.showMarker as () => void)();
      }
    }
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
 * 여행 계획 상세 정보 처리 및 마커 표시
 */
const selectPlanDetail = (planDetail: PlanDetail): void => {
  if (!planDetail) {
    throw new Error('유효하지 않은 여행 계획 상세 정보입니다.');
  }

  clearMarkers();

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

  if (markers.value.length > 0 && mapRef.value) {
    showAllMarkers();

    const bounds = calculateBounds(markers.value);
    if (bounds && mapRef.value.map) {
      mapRef.value.map.setBounds(bounds);
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

  mapRef.value.hideMarker();
  hideSingleMarker();
  clearMarkersAndInfoWindows();

  markers.value.forEach(markerInfo => {
    if (!markerInfo.lat || !markerInfo.lng) return;

    try {
      createMarkerWithInfoWindow(markerInfo);
    } catch (error) {
      throw new Error(`마커 생성 실패: ${error}`);
    }
  });

  return true;
};

/**
 * 마커 및 정보창 제거 (내부 헬퍼)
 */
const clearMarkersAndInfoWindows = (): void => {
  markersObjects.value.forEach(marker => marker.setMap(null));
  infoWindows.value.forEach(infoWindow => infoWindow.close());
  markersObjects.value = [];
  infoWindows.value = [];
};

/**
 * 단일 마커와 정보창 생성 (내부 헬퍼)
 */
const createMarkerWithInfoWindow = (markerInfo: MarkerInfo): void => {
  if (!mapRef.value?.map) {
    throw new Error('지도가 초기화되지 않았습니다.');
  }

  const marker = createKakaoMarker({ lat: markerInfo.lat, lng: markerInfo.lng }, mapRef.value.map, {
    image: new window.kakao.maps.MarkerImage(
      'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png',
      new window.kakao.maps.Size(24, 35)
    ),
  });

  const infoWindow = createInfoWindow(markerInfo);

  window.kakao.maps.event.addListener(marker, 'click', () => {
    infoWindows.value.forEach(iw => iw.close());
    infoWindow.open(mapRef.value!.map, marker);
  });

  markersObjects.value.push(marker);
  infoWindows.value.push(infoWindow);
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

// 지도 참조 변경 감지
watch(
  () => mapRef.value?.map,
  newMap => {
    if (newMap && markers.value.length > 0) {
      showAllMarkers();

      const bounds = calculateBounds(markers.value);
      if (bounds) {
        newMap.setBounds(bounds);
      }
    }
  }
);

/**
 * 지도 전역 상태 관리 컴포저블
 * 단일 마커, 다중 마커, 폴리라인 관리 및 비즈니스 로직 처리
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
