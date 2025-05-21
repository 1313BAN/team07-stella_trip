/* eslint-disable @typescript-eslint/no-explicit-any */
import { type Ref, shallowRef } from 'vue';

interface UseMapMarkerReturn {
  marker: Ref<any>;
  updateMarker: (lat: number, lng: number, show?: boolean) => void;
  showMarker: (lat: number, lng: number) => void;
  hideMarker: () => void;
  getMarker: () => any;
}

export function useMapMarker(mapInstance: Ref<any>): UseMapMarkerReturn {
  const marker = shallowRef<any>(null);

  // 마커 생성 함수
  const createMarker = (position: any) => {
    if (!mapInstance.value) return null;

    const markerOptions: any = {
      position,
      map: mapInstance.value,
    };

    return new window.kakao.maps.Marker(markerOptions);
  };

  // 마커 업데이트 또는 생성 함수
  const updateMarker = (lat: number, lng: number, show: boolean = true) => {
    if (!mapInstance.value) return;

    const position = new window.kakao.maps.LatLng(lat, lng);

    // 마커가 이미 존재하면 위치만 업데이트
    if (marker.value) {
      marker.value.setPosition(position);
      return;
    }

    // show가 true이고 마커가 없으면 새로 생성
    if (show) {
      marker.value = createMarker(position);
    }
  };

  // 마커 표시 함수
  const showMarker = (lat: number, lng: number): void => {
    updateMarker(lat, lng, true);

    if (marker.value && marker.value.getMap() !== mapInstance.value) {
      marker.value.setMap(mapInstance.value);
    }
  };

  // 마커 숨김 함수
  const hideMarker = () => {
    if (marker.value) {
      marker.value.setMap(null);
      marker.value = null;
    }
  };

  return {
    marker,
    updateMarker,
    showMarker,
    hideMarker,
    getMarker: () => marker.value,
  };
}
