/* eslint-disable @typescript-eslint/no-explicit-any */
import { shallowRef } from 'vue';

export interface MapOptions {
  center?: { lat: number; lng: number };
  level?: number;
  mapTypeId?: number;
  draggable?: boolean;
  zoomable?: boolean;
}

export function useMap() {
  const mapInstance = shallowRef<any>(null);

  const loadScript = async (): Promise<void> => {
    if ((window as any).kakao?.maps && (window as any).kakao.maps.LatLng) {
      return;
    }

    if ((window as any).kakao?.maps) {
      return new Promise(resolve => {
        (window as any).kakao.maps.load(() => resolve());
      });
    }

    const apiKey = import.meta.env.VITE_KAKAO_MAP_API_KEY;
    if (!apiKey) {
      throw new Error('카카오맵 API 키가 설정되지 않았습니다.');
    }

    return new Promise((resolve, reject) => {
      const script = document.createElement('script');
      script.async = true;
      script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${apiKey}&autoload=false`;

      script.onload = () => {
        (window as any).kakao.maps.load(() => resolve());
      };

      script.onerror = () => {
        reject(new Error('카카오맵 스크립트 로드 실패'));
      };

      document.head.appendChild(script);
    });
  };

  const initMap = async (
    container: HTMLElement | string,
    options: MapOptions = {}
  ): Promise<any> => {
    await loadScript();

    const el = typeof container === 'string' ? document.getElementById(container) : container;
    if (!el) {
      throw new Error('유효하지 않은 컨테이너입니다.');
    }

    const { center = { lat: 37.5665, lng: 126.978 }, level = 3, ...restOptions } = options;
    const mapOptions = {
      center: new (window as any).kakao.maps.LatLng(center.lat, center.lng),
      level,
      ...restOptions,
    };

    mapInstance.value = new (window as any).kakao.maps.Map(el, mapOptions);
    return mapInstance.value;
  };

  return {
    map: mapInstance,
    initMap,
  };
}
