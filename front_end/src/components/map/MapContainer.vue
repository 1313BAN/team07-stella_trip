<template>
  <div ref="mapElement" :style="containerStyle"></div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount, watch, shallowRef } from 'vue';
import { useMap } from '@/composables/useMap';
import { useMapMarker } from '@/composables/useMapMarker';

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
const mapInstance = shallowRef<any>(null);
const resizeObserver = shallowRef<ResizeObserver | null>(null);

// 지연 실행 함수(디바운스)를 위한 타이머
const resizeTimer = shallowRef<number | null>(null);

const { updateMarker, showMarker, hideMarker, getMarker } = useMapMarker(mapInstance);

const initializeMap = async () => {
  if (!mapElement.value) {
    throw new Error('지도 엘리먼트를 찾을 수 없습니다.');
  }

  mapInstance.value = await initMap(mapElement.value, {
    center: props.center,
    level: props.level,
  });

  if (props.showCenterMarker) {
    updateMarker(props.center.lat, props.center.lng);
  }

  // ResizeObserver 설정
  setupResizeObserver();

  return mapInstance.value;
};

// 지도 갱신 함수
const refreshMap = () => {
  if (mapInstance.value) {
    // 이전 타이머가 있으면 취소
    if (resizeTimer.value !== null) {
      window.clearTimeout(resizeTimer.value);
    }

    // TODO: 디바운싱 유틸 분리
    resizeTimer.value = window.setTimeout(() => {
      console.log('지도 리레이아웃 실행');
      mapInstance.value.relayout();

      // 중심점 재설정
      const position = new window.kakao.maps.LatLng(props.center.lat, props.center.lng);
      mapInstance.value.setCenter(position);

      // 마커 위치 업데이트
      if (props.showCenterMarker) {
        updateMarker(props.center.lat, props.center.lng);
      }

      resizeTimer.value = null;
    }, 300);
  }
};

// ResizeObserver 설정 함수
const setupResizeObserver = () => {
  if (!mapElement.value) return;

  // 이전 ResizeObserver가 있으면 제거
  if (resizeObserver.value) {
    resizeObserver.value.disconnect();
  }

  // 새 ResizeObserver 생성
  resizeObserver.value = new ResizeObserver(() => {
    refreshMap();
  });

  // 지도 엘리먼트 관찰 시작
  resizeObserver.value.observe(mapElement.value);
};

// center prop 변경 감지 및 지도 위치 업데이트
watch(
  () => props.center,
  newCenter => {
    if (mapInstance.value && newCenter) {
      const position = new window.kakao.maps.LatLng(newCenter.lat, newCenter.lng);
      mapInstance.value.setCenter(position);

      // 센터가 변경되면 마커 위치도 업데이트
      if (props.showCenterMarker) {
        updateMarker(newCenter.lat, newCenter.lng);
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
      showMarker(props.center.lat, props.center.lng);
    } else {
      hideMarker();
    }
  }
);

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

// 지도 메서드 노출
defineExpose({
  map: mapInstance,
  panTo: (lat: number, lng: number) => {
    if (mapInstance.value) {
      const position = new window.kakao.maps.LatLng(lat, lng);
      mapInstance.value.panTo(position);
    }
  },
  setLevel: (level: number) => {
    if (mapInstance.value) {
      mapInstance.value.setLevel(level);
    }
  },
  showMarker: () => showMarker(props.center.lat, props.center.lng),
  hideMarker,
  getMarker,
  refreshMap, // 외부에서 강제로 갱신할 수 있도록 노출
});
</script>
