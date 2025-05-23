import { ref, type Ref } from 'vue';
import type { MapInstance } from '@/types/kakao';
import { checkKakaoMapsAPI } from '@/utils/mapUtils';

export interface MapOptions {
  center?: { lat: number; lng: number };
  level?: number;
  draggable?: boolean;
  zoomable?: boolean;
}

export interface UseMapReturn {
  map: Ref<MapInstance | null>;
  initMap: (container: HTMLElement | string, options?: MapOptions) => Promise<MapInstance>;
}

/**
 * 카카오맵 초기화를 위한 컴포저블
 *
 * @returns 지도 인스턴스와 초기화 함수
 */
export function useMap(): UseMapReturn {
  const map = ref<MapInstance | null>(null);

  /**
   * 카카오맵 스크립트를 동적으로 로드
   * 이미 로드된 경우 즉시 반환
   *
   * @throws 환경변수에 API 키가 없거나 스크립트 로드 실패 시
   */
  const loadScript = async (): Promise<void> => {
    if (checkKakaoMapsAPI()) return;

    if (window.kakao?.maps) {
      return new Promise(resolve => {
        window.kakao.maps.load(() => resolve());
      });
    }

    const apiKey = import.meta.env.VITE_KAKAO_MAP_API_KEY;
    if (!apiKey) {
      throw new Error('카카오맵 API 키가 설정되지 않았습니다.');
    }

    return new Promise((resolve, reject) => {
      const script = document.createElement('script');
      script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${apiKey}&autoload=false`;
      script.onload = () => window.kakao.maps.load(() => resolve());
      script.onerror = () => reject(new Error('카카오맵 스크립트 로드 실패'));
      document.head.appendChild(script);
    });
  };

  /**
   * 지정된 컨테이너에 카카오맵을 초기화
   *
   * @param container 지도를 표시할 HTML 요소 또는 요소 ID
   * @param options 지도 초기화 옵션
   * @param options.center 지도 중심 좌표 (기본값: 서울시청)
   * @param options.level 지도 확대/축소 레벨 (기본값: 3)
   * @param options.draggable 지도 드래그 가능 여부 (기본값: true)
   * @param options.zoomable 지도 확대/축소 가능 여부 (기본값: true)
   * @returns 생성된 지도 인스턴스
   * @throws 컨테이너를 찾을 수 없거나 지도 생성 실패 시
   */
  const initMap = async (
    container: HTMLElement | string,
    options: MapOptions = {}
  ): Promise<MapInstance> => {
    await loadScript();

    const element = typeof container === 'string' ? document.getElementById(container) : container;

    if (!element) throw new Error('컨테이너를 찾을 수 없습니다.');

    const { center = { lat: 37.5665, lng: 126.978 }, level = 3, ...rest } = options;

    const mapInstance = new window.kakao.maps.Map(element, {
      center: new window.kakao.maps.LatLng(center.lat, center.lng),
      level,
      ...rest,
    });

    map.value = mapInstance;
    return mapInstance;
  };

  return {
    map,
    initMap,
  };
}
