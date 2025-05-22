import { ref, computed, watch, type ComputedRef } from 'vue';
import type { Attraction } from '@/services/api/domains/attraction';
import type { Plan, PlanDetail } from '@/services/api/domains/plan';
import type MapContainer from '@/components/map/MapContainer.vue';

interface MarkerInfo {
  lat: number;
  lng: number;
  name: string;
  order?: number;
  date?: string;
}

// Kakao 맵 마커 객체 타입 정의
type KakaoMarker = {
  setMap: (map: any | null) => void;
  [key: string]: any;
};

interface UseMapStateReturn {
  mapRef: ReturnType<typeof ref<InstanceType<typeof MapContainer> | null>>;
  selectedAttraction: ReturnType<typeof ref<Attraction | null>>;
  selectedPlan: ReturnType<typeof ref<Plan | null>>;
  mapLevel: ReturnType<typeof ref<number>>;
  mapCenter: ComputedRef<{ lat: number; lng: number }>;
  markers: ReturnType<typeof ref<MarkerInfo[]>>;
  showRoute: (dateAttractions: MarkerInfo[]) => boolean;
  clearPolylines: () => void;
  selectAttraction: (attraction: Attraction) => void;
  selectPlan: (plan: Plan) => void;
  selectPlanDetail: (planDetail: PlanDetail) => void;
  clearMarkers: () => void;
  showAllMarkers: () => void;
}

// 싱글톤으로 작동
const mapRef = ref<InstanceType<typeof MapContainer> | null>(null);
const selectedAttraction = ref<Attraction | null>(null);
const selectedPlan = ref<Plan | null>(null);
const defaultCenter = { lat: 37.5665, lng: 126.978 };
const mapLevel = ref(7);
const markers = ref<MarkerInfo[]>([]);
const markersObjects = ref<KakaoMarker[]>([]);

// 마커 초기화 함수
const clearMarkers = () => {
  // 중앙 마커 숨기기
  mapRef.value?.hideMarker();

  // Kakao Maps API를 사용하여 추가한 마커 제거
  if (window.kakao?.maps && mapRef.value?.map && markersObjects.value.length > 0) {
    markersObjects.value.forEach(marker => marker.setMap(null));
    markersObjects.value = [];
  }

  // 마커 정보 배열 초기화
  markers.value = [];
};

// 관광지 선택 시 핸들러
const selectAttraction = (attraction: Attraction) => {
  if (!attraction) return;

  selectedAttraction.value = attraction;
  selectedPlan.value = null;
  clearMarkers();

  if (attraction.latitude !== undefined && attraction.longitude !== undefined && mapRef.value) {
    mapRef.value.panTo(attraction.latitude, attraction.longitude);
    mapRef.value.setLevel(3);
    mapRef.value.showMarker(attraction.latitude, attraction.longitude);
  }
};

// 여행 계획 선택 시 핸들러
const selectPlan = (plan: Plan) => {
  if (!plan) return;

  selectedPlan.value = plan;
  selectedAttraction.value = null;
};

// 여행 계획 상세 정보 처리 핸들러
const selectPlanDetail = (planDetail: PlanDetail) => {
  // 마커 초기화
  clearMarkers();

  // 모든 여행 일정에서 방문 장소 추출
  const allAttractions: MarkerInfo[] = [];

  if (planDetail.details) {
    Object.entries(planDetail.details).forEach(([date, attractions]) => {
      attractions.forEach(attraction => {
        // 좌표 검증 및 처리
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

  // 마커 정보 설정
  markers.value = allAttractions;

  if (markers.value.length > 0 && mapRef.value) {
    // 모든 마커를 표시
    showAllMarkers();

    // 카카오맵 bounds 설정
    if (window.kakao?.maps && mapRef.value.map) {
      // 모든 마커를 포함하는 경계 생성
      const bounds = new window.kakao.maps.LatLngBounds();

      // 모든 마커 좌표를 경계에 추가
      markers.value.forEach(marker => {
        bounds.extend(new window.kakao.maps.LatLng(marker.lat, marker.lng));
      });

      // 경계에 맞게 지도 조정 (모든 마커가 보이도록)
      mapRef.value.map.setBounds(bounds);

      // 경계 설정 후 약간의 지도 레벨 조정 (여백을 위해)
      const currentLevel = mapRef.value.map.getLevel();
      mapRef.value.map.setLevel(currentLevel);

      // 상태 변수 업데이트
      mapLevel.value = currentLevel;
    }
  }
};

// 모든 마커 표시 함수
const showAllMarkers = () => {
  if (!mapRef.value || !mapRef.value.map || markers.value.length === 0) return false;

  // 중앙 마커 숨기기
  mapRef.value.hideMarker();

  // 카카오맵 API 확인
  if (!window.kakao?.maps) return false;

  // 이전 마커 객체들 제거
  markersObjects.value.forEach(marker => marker.setMap(null));
  markersObjects.value = [];

  // 각 위치에 마커 생성 및 표시
  markers.value.forEach(markerInfo => {
    if (!markerInfo.lat || !markerInfo.lng) return;

    const position = new window.kakao.maps.LatLng(markerInfo.lat, markerInfo.lng);

    // 마커 생성 (색상이 있는 마커로 변경)
    const marker = new window.kakao.maps.Marker({
      position: position,
      map: mapRef.value?.map,
      // 날짜별로 다른 색상의 마커 사용
      image: new window.kakao.maps.MarkerImage(
        'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png',
        new window.kakao.maps.Size(24, 35)
      ),
    });

    // 정보창 (마커 클릭 시 표시되는 정보)
    const infoContent = `
          <div style="padding:8px;font-size:12px;max-width:200px;word-break:keep-all;">
            <div style="font-weight:bold;margin-bottom:4px;color:#333;">${markerInfo.name}</div> 
            ${markerInfo.order ? `<div style="color:#666;">방문 순서: ${markerInfo.order}</div>` : ''}
            ${markerInfo.date ? `<div style="color:#666;">날짜: ${markerInfo.date}</div>` : ''}
          </div>
        `;
    const infoWindow = new window.kakao.maps.InfoWindow({
      content: infoContent,
      removable: true,
    });

    // 마커 클릭 시 다른 정보창은 닫고 이 마커의 정보창만 표시
    window.kakao.maps.event.addListener(marker, 'click', () => {
      // 다른 정보창 닫기 로직 추가 가능
      infoWindow.open(mapRef.value?.map, marker);
    });

    // 마커 객체 저장
    markersObjects.value.push(marker);
  });

  return true;
};

// mapCenter 계산 속성 - 간소화됨
const mapCenter = computed(() => {
  if (
    selectedAttraction.value?.latitude !== undefined &&
    selectedAttraction.value?.longitude !== undefined
  ) {
    return {
      lat: selectedAttraction.value.latitude,
      lng: selectedAttraction.value.longitude,
    };
  }

  // 기본 중심점 반환
  return defaultCenter;
});

// 폴리라인 객체 관리를 위한 변수 추가
const polylines = ref<any[]>([]);

// 폴리라인 초기화 함수
const clearPolylines = () => {
  if (polylines.value.length > 0) {
    polylines.value.forEach(line => line.setMap(null));
    polylines.value = [];
  }
};

// 경로 표시 함수 추가
const showRoute = (dateAttractions: MarkerInfo[]) => {
  if (!mapRef.value?.map || !window.kakao?.maps) return false;

  // 기존 폴리라인 제거
  clearPolylines();

  // 해당 날짜의 마커들을 순서별로 정렬
  const sortedAttractions = [...dateAttractions].sort((a, b) => (a.order || 0) - (b.order || 0));

  if (sortedAttractions.length < 2) return false; // 경로를 그릴 포인트가 부족

  // 경로 좌표 배열 생성
  const linePath = sortedAttractions.map(
    attraction => new window.kakao.maps.LatLng(attraction.lat, attraction.lng)
  );

  // 폴리라인 생성
  const polyline = new window.kakao.maps.Polyline({
    path: linePath,
    strokeWeight: 4, // 선 두께
    strokeColor: '#5F3DC4', // 보라색 계열 (커스텀 가능)
    strokeOpacity: 0.7, // 투명도
    strokeStyle: 'solid', // 선 스타일
  });

  // 지도에 표시
  polyline.setMap(mapRef.value.map);

  // 폴리라인 객체 저장
  polylines.value.push(polyline);

  return true;
};

// 지도 참조 변경 감지 - 마커를 다시 그리고 bounds 설정
watch(
  () => mapRef.value?.map,
  newMap => {
    if (newMap && markers.value.length > 0) {
      // 마커 다시 그리기
      showAllMarkers();

      // bounds 설정으로 모든 마커 표시
      if (window.kakao?.maps) {
        const bounds = new window.kakao.maps.LatLngBounds();

        markers.value.forEach(marker => {
          bounds.extend(new window.kakao.maps.LatLng(marker.lat, marker.lng));
        });

        mapRef.value?.map.setBounds(bounds);
      }
    }
  }
);

// 싱글톤 컴포저블 함수 정의
export function useMapState(): UseMapStateReturn {
  // 이미 생성된 싱글톤 상태와 메서드 반환
  return {
    mapRef,
    selectedAttraction,
    selectedPlan,
    mapLevel,
    mapCenter,
    markers,
    showRoute,
    clearPolylines,
    selectAttraction,
    selectPlan,
    selectPlanDetail,
    clearMarkers,
    showAllMarkers,
  };
}
