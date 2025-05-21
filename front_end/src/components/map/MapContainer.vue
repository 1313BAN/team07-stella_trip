<template>
  <div ref="mapElement" :style="containerStyle"></div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, shallowRef } from 'vue';
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

  return mapInstance.value;
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
});
</script>
