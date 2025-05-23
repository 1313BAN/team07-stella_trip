import type {
  MapInstance,
  MarkerInstance,
  InfoWindowInstance,
  PolylineInstance,
  MarkerOptions,
  InfoWindowOptions,
  PolylineOptions,
  MarkerInfo,
} from '@/types/kakao';

/**
 * 카카오맵 API가 로드되었는지 확인
 *
 * @returns 카카오맵 API 로드 여부
 */
export const checkKakaoMapsAPI = (): boolean => {
  return !!(window.kakao?.maps && window.kakao.maps.LatLng);
};

/**
 * 카카오맵 API 로드 상태를 확인하고 로드되지 않은 경우 에러 발생
 * @throws 카카오맵 API가 로드되지 않은 경우
 * @private 내부 사용 전용
 */
const ensureKakaoMapsAPI = (): void => {
  if (!checkKakaoMapsAPI()) {
    throw new Error('카카오맵 API가 로드되지 않았습니다.');
  }
};

/**
 * 카카오맵 마커를 생성
 *
 * @param position 마커 위치 좌표
 * @param position.lat 위도
 * @param position.lng 경도
 * @param map 마커를 표시할 지도 인스턴스
 * @param options 마커 생성 옵션 (이미지, 제목 등)
 * @returns 생성된 마커 인스턴스
 * @throws 카카오맵 API가 로드되지 않은 경우
 */
export const createKakaoMarker = (
  position: { lat: number; lng: number },
  map: MapInstance,
  options: Partial<MarkerOptions> = {}
): MarkerInstance => {
  ensureKakaoMapsAPI();

  const kakaoPosition = new window.kakao.maps.LatLng(position.lat, position.lng);

  const markerOptions: MarkerOptions = {
    position: kakaoPosition,
    map,
    ...options,
  };

  return new window.kakao.maps.Marker(markerOptions);
};

/**
 * 마커 정보창을 생성
 *
 * @param markerInfo 정보창에 표시할 마커 정보
 * @param markerInfo.name 장소 이름
 * @param markerInfo.order 방문 순서 (선택적)
 * @param markerInfo.date 방문 날짜 (선택적)
 * @param options 정보창 생성 옵션
 * @returns 생성된 정보창 인스턴스
 * @throws 카카오맵 API가 로드되지 않은 경우
 */
export const createInfoWindow = (
  markerInfo: MarkerInfo,
  options: Partial<InfoWindowOptions> = {}
): InfoWindowInstance => {
  ensureKakaoMapsAPI();

  const content = `
    <div style="padding:8px;font-size:12px;max-width:200px;word-break:keep-all;">
      <div style="font-weight:bold;margin-bottom:4px;color:#333;">${markerInfo.name}</div> 
      ${markerInfo.order ? `<div style="color:#666;">방문 순서: ${markerInfo.order}</div>` : ''}
      ${markerInfo.date ? `<div style="color:#666;">날짜: ${markerInfo.date}</div>` : ''}
    </div>
  `;

  const infoWindowOptions: InfoWindowOptions = {
    content,
    removable: true,
    ...options,
  };

  return new window.kakao.maps.InfoWindow(infoWindowOptions);
};

/**
 * 지도에 경로를 표시하는 폴리라인을 생성
 *
 * @param path 경로를 구성하는 좌표 배열
 * @param map 폴리라인을 표시할 지도 인스턴스
 * @param options 폴리라인 스타일 옵션 (색상, 두께 등)
 * @returns 생성된 폴리라인 인스턴스
 * @throws 카카오맵 API가 로드되지 않은 경우
 */
export const createPolyline = (
  path: { lat: number; lng: number }[],
  map: MapInstance,
  options: Partial<PolylineOptions> = {}
): PolylineInstance => {
  ensureKakaoMapsAPI();

  const kakaoPath = path.map(point => new window.kakao.maps.LatLng(point.lat, point.lng));

  const defaultOptions: Partial<PolylineOptions> = {
    strokeWeight: 4,
    strokeColor: '#5F3DC4',
    strokeOpacity: 0.7,
    strokeStyle: 'solid',
  };

  const polylineOptions: PolylineOptions = {
    path: kakaoPath,
    map,
    ...defaultOptions,
    ...options,
  };

  return new window.kakao.maps.Polyline(polylineOptions);
};

/**
 * 여러 좌표를 모두 포함하는 지도 경계를 계산
 *
 * @param points 경계 계산에 포함할 좌표 배열
 * @returns 계산된 경계 객체 또는 좌표가 없는 경우 null
 * @throws 카카오맵 API가 로드되지 않은 경우
 */
export const calculateBounds = (points: { lat: number; lng: number }[]) => {
  ensureKakaoMapsAPI();

  if (points.length === 0) {
    return null;
  }

  const firstPoint = new window.kakao.maps.LatLng(points[0].lat, points[0].lng);
  const bounds = new window.kakao.maps.LatLngBounds(firstPoint, firstPoint);

  points.slice(1).forEach(point => {
    bounds.extend(new window.kakao.maps.LatLng(point.lat, point.lng));
  });
  points.forEach(point => {
    bounds.extend(new window.kakao.maps.LatLng(point.lat, point.lng));
  });

  return bounds;
};

/**
 * 단일 마커를 관리하는 유틸리티 클래스
 */
export class SingleMarkerManager {
  private _marker: MarkerInstance | null = null;
  private _mapInstance: MapInstance;

  constructor(mapInstance: MapInstance) {
    this._mapInstance = mapInstance;
  }

  /**
   * 마커를 생성 (내부 사용)
   *
   * @param position 마커 위치 좌표
   * @returns 생성된 마커 인스턴스
   */
  private _createMarker(position: { lat: number; lng: number }): MarkerInstance {
    return createKakaoMarker(position, this._mapInstance);
  }

  /**
   * 마커를 업데이트하거나 새로 생성
   *
   * @param lat 위도
   * @param lng 경도
   * @param show 마커를 표시할지 여부 (기본값: true)
   */
  updateMarker(lat: number, lng: number, show: boolean = true): void {
    if (!this._mapInstance) return;

    const position = { lat, lng };

    // 마커가 이미 존재하면 위치만 업데이트
    if (this._marker) {
      if (show) {
        this._marker.setPosition(new window.kakao.maps.LatLng(lat, lng));
      } else {
        this._marker.setMap(null);
        this._marker = null;
      }
      return;
    }

    // show가 true이고 마커가 없으면 새로 생성
    if (show) {
      this._marker = this._createMarker(position);
    }
  }

  /**
   * 지정된 위치에 마커를 표시
   *
   * @param lat 위도
   * @param lng 경도
   */
  showMarker(lat: number, lng: number): void {
    this.updateMarker(lat, lng, true);

    if (this._marker && this._marker.getMap() !== this._mapInstance) {
      this._marker.setMap(this._mapInstance);
    }
  }

  /**
   * 마커를 숨김
   */
  hideMarker(): void {
    if (this._marker) {
      this._marker.setMap(null);
      this._marker = null;
    }
  }

  /**
   * 현재 마커 인스턴스를 반환
   *
   * @returns 마커 인스턴스 또는 null
   */
  getMarker(): MarkerInstance | null {
    return this._marker;
  }
}

/**
 * 다중 마커를 관리하는 유틸리티 클래스
 */
export class MultiMarkerManager {
  private _markers: MarkerInstance[] = [];
  private _infoWindows: InfoWindowInstance[] = [];
  private _mapInstance: MapInstance;

  constructor(mapInstance: MapInstance) {
    this._mapInstance = mapInstance;
  }

  /**
   * 마커 정보를 기반으로 마커와 정보창을 생성
   */
  private _createMarkerWithInfoWindow(markerInfo: MarkerInfo): void {
    if (!this._mapInstance) return;

    // 마커 생성 (기본 이미지 사용)
    const marker = createKakaoMarker(
      { lat: markerInfo.lat, lng: markerInfo.lng },
      this._mapInstance,
      {
        image: new window.kakao.maps.MarkerImage(
          'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png',
          new window.kakao.maps.Size(24, 35)
        ),
      }
    );

    // 정보창 생성
    const infoWindow = createInfoWindow(markerInfo);

    // 마커 클릭 이벤트
    window.kakao.maps.event.addListener(marker, 'click', () => {
      // 다른 정보창들 닫기
      this._infoWindows.forEach(iw => iw.close());
      // 현재 정보창 열기
      infoWindow.open(this._mapInstance, marker);
    });

    this._markers.push(marker);
    this._infoWindows.push(infoWindow);
  }

  /**
   * 다중 마커 추가
   */
  addMarkers(markerInfos: MarkerInfo[]): void {
    markerInfos.forEach(markerInfo => {
      if (Number.isFinite(markerInfo.lat) && Number.isFinite(markerInfo.lng)) {
        this._createMarkerWithInfoWindow(markerInfo);
      }
    });
  }

  /**
   * 모든 마커와 정보창 제거
   */
  clearAll(): void {
    // 마커 제거
    this._markers.forEach(marker => marker.setMap(null));
    this._markers = [];

    // 정보창 제거
    this._infoWindows.forEach(infoWindow => infoWindow.close());
    this._infoWindows = [];
  }

  /**
   * 마커 개수 반환
   */
  getMarkerCount(): number {
    return this._markers.length;
  }

  /**
   * 모든 마커 위치 반환
   */
  getMarkerPositions(): { lat: number; lng: number }[] {
    return this._markers.map(marker => {
      const position = marker.getPosition();
      return {
        lat: position.getLat(),
        lng: position.getLng(),
      };
    });
  }

  /**
   * 지도 경계에 맞게 줌 조정
   */
  fitMapBounds(): void {
    if (this._markers.length === 0) return;

    const positions = this.getMarkerPositions();
    const bounds = calculateBounds(positions);

    if (bounds) {
      this._mapInstance.setBounds(bounds);
    }
  }
}
