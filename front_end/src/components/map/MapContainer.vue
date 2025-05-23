<template>
  <div ref="mapElement" :style="containerStyle"></div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount, watch, shallowRef } from 'vue';
import { useMap } from '@/composables/useMap';
import { useMapState } from '@/composables/useMapState';

interface Props {
  center?: { lat: number; lng: number };
  level?: number;
  width?: string;
  height?: string;
  showCenterMarker?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  center: () => ({ lat: 37.5665, lng: 126.978 }),
  level: 3,
  width: '100%',
  height: '100%',
  showCenterMarker: false,
});

const containerStyle = computed(() => ({
  width: props.width,
  height: props.height,
}));

const mapElement = ref<HTMLElement | null>(null);
const { initMap } = useMap();
const { showSingleMarker, hideSingleMarker, updateSingleMarker, singleMarker } = useMapState();

const mapInstance = shallowRef<any>(null);
const resizeObserver = shallowRef<ResizeObserver | null>(null);
const resizeTimer = shallowRef<number | null>(null);

/**
 * 지도 초기화
 */
const initializeMap = async () => {
  if (!mapElement.value) {
    throw new Error('지도 엘리먼트를 찾을 수 없습니다.');
  }

  mapInstance.value = await initMap(mapElement.value, {
    center: props.center,
    level: props.level,
  });

  if (props.showCenterMarker) {
    updateSingleMarker(props.center.lat, props.center.lng);
  }

  setupResizeObserver();
  return mapInstance.value;
};

/**
 * 지도 갱신 (디바운싱 적용)
 */
const refreshMap = (): void => {
  if (!mapInstance.value) return;

  // 이전 타이머 취소
  if (resizeTimer.value !== null) {
    window.clearTimeout(resizeTimer.value);
  }

  // 디바운싱 적용하여 지연 실행
  resizeTimer.value = window.setTimeout(() => {
    console.log('지도 리레이아웃 실행');
    mapInstance.value.relayout();

    // 중심점 재설정
    const position = new window.kakao.maps.LatLng(props.center.lat, props.center.lng);
    mapInstance.value.setCenter(position);

    // 마커 위치 업데이트
    if (props.showCenterMarker) {
      updateSingleMarker(props.center.lat, props.center.lng);
    }

    resizeTimer.value = null;
  }, 300);
};

/**
 * ResizeObserver 설정
 */
const setupResizeObserver = (): void => {
  if (!mapElement.value) return;

  // 기존 ResizeObserver 정리
  if (resizeObserver.value) {
    resizeObserver.value.disconnect();
  }

  // 새 ResizeObserver 생성 및 설정
  resizeObserver.value = new ResizeObserver(() => {
    refreshMap();
  });

  resizeObserver.value.observe(mapElement.value);
};

/**
 * 지도 중심점을 이동
 *
 * @param lat 위도
 * @param lng 경도
 */
const panTo = (lat: number, lng: number): void => {
  if (mapInstance.value) {
    const position = new window.kakao.maps.LatLng(lat, lng);
    mapInstance.value.panTo(position);
  }
};

/**
 * 지도 확대/축소 레벨 설정
 *
 * @param level 지도 레벨
 */
const setLevel = (level: number): void => {
  if (mapInstance.value) {
    mapInstance.value.setLevel(level);
  }
};

/**
 * 중심점에 마커 표시
 *
 * @param lat 위도 (선택적)
 * @param lng 경도 (선택적)
 */
const showMarker = (lat?: number, lng?: number): void => {
  const targetLat = lat ?? props.center.lat;
  const targetLng = lng ?? props.center.lng;
  showSingleMarker(targetLat, targetLng);
};

/**
 * 마커 숨김
 */
const hideMarker = (): void => {
  hideSingleMarker();
};

/**
 * 현재 마커 인스턴스 반환
 */
const getMarker = () => {
  return singleMarker.value;
};

// center prop 변경 감지
watch(
  () => props.center,
  newCenter => {
    if (mapInstance.value && newCenter) {
      const position = new window.kakao.maps.LatLng(newCenter.lat, newCenter.lng);
      mapInstance.value.setCenter(position);

      // 마커가 표시 중이면 위치 업데이트
      if (props.showCenterMarker) {
        updateSingleMarker(newCenter.lat, newCenter.lng);
      }
    }
  },
  { deep: true }
);

// showCenterMarker prop 변경 감지
watch(
  () => props.showCenterMarker,
  newShowMarker => {
    if (!mapInstance.value) return;

    if (newShowMarker) {
      showSingleMarker(props.center.lat, props.center.lng);
    } else {
      hideSingleMarker();
    }
  }
);

//TODO: getCurrentInstance 미사용으로 mapRef 주입 실패
onMounted(async () => {
  await initializeMap();
});

onBeforeUnmount(() => {
  // ResizeObserver 정리
  if (resizeObserver.value) {
    resizeObserver.value.disconnect();
    resizeObserver.value = null;
  }

  // 타이머 정리
  if (resizeTimer.value !== null) {
    window.clearTimeout(resizeTimer.value);
    resizeTimer.value = null;
  }
});

// 외부에서 사용할 수 있는 메서드들 노출
defineExpose({
  map: mapInstance,
  panTo,
  setLevel,
  showMarker,
  hideMarker,
  getMarker,
  refreshMap,
});
</script>
