/* eslint-disable @typescript-eslint/no-explicit-any */

interface KakaoLatLng {
  getLat(): number;
  getLng(): number;
}

interface KakaoSize {
  width: number;
  height: number;
}

interface KakaoLatLngBounds {
  extend(latlng: KakaoLatLng): void;
  isEmpty(): boolean;
  getSouthWest(): KakaoLatLng;
  getNorthEast(): KakaoLatLng;
}

interface KakaoMap {
  setCenter(latlng: KakaoLatLng): void;
  getCenter(): KakaoLatLng;
  setLevel(level: number, options?: { animate?: boolean; anchor?: KakaoLatLng }): void;
  getLevel(): number;
  panTo(latlng: KakaoLatLng): void;
  setBounds(bounds: KakaoLatLngBounds): void;
  getBounds(): KakaoLatLngBounds;
  addControl(control: any, position: any): void;
  removeControl(control: any): void;
  relayout(): void;
}

interface KakaoMarkerImage {
  getSrc(): string;
  getSize(): KakaoSize;
}

interface KakaoMarkerOptions {
  position: KakaoLatLng;
  map?: KakaoMap | null;
  image?: KakaoMarkerImage;
  title?: string;
  draggable?: boolean;
  clickable?: boolean;
  zIndex?: number;
  opacity?: number;
  range?: number;
}

interface KakaoMarker {
  setMap(map: KakaoMap | null): void;
  getMap(): KakaoMap | null;
  setPosition(position: KakaoLatLng): void;
  getPosition(): KakaoLatLng;
  setImage(image: KakaoMarkerImage): void;
  getImage(): KakaoMarkerImage;
  setTitle(title: string): void;
  getTitle(): string;
  setDraggable(draggable: boolean): void;
  getDraggable(): boolean;
  setClickable(clickable: boolean): void;
  getClickable(): boolean;
  setZIndex(zIndex: number): void;
  getZIndex(): number;
  setOpacity(opacity: number): void;
  getOpacity(): number;
  setRange(range: number): void;
  getRange(): number;
}

interface KakaoInfoWindowOptions {
  content?: string | HTMLElement;
  position?: KakaoLatLng;
  map?: KakaoMap;
  removable?: boolean;
  zIndex?: number;
  altitude?: number;
}

interface KakaoInfoWindow {
  open(map: KakaoMap, marker?: KakaoMarker): void;
  close(): void;
  getMap(): KakaoMap | null;
  setPosition(position: KakaoLatLng): void;
  getPosition(): KakaoLatLng;
  setContent(content: string | HTMLElement): void;
  getContent(): string | HTMLElement;
  setZIndex(zIndex: number): void;
  getZIndex(): number;
}

interface KakaoPolylineOptions {
  path: KakaoLatLng[];
  map?: KakaoMap;
  endArrow?: boolean;
  strokeWeight?: number;
  strokeColor?: string;
  strokeOpacity?: number;
  strokeStyle?:
    | 'solid'
    | 'shortdash'
    | 'shortdot'
    | 'shortdashdot'
    | 'dot'
    | 'dash'
    | 'longdash'
    | 'dashdot'
    | 'longdashdot';
  zIndex?: number;
}

interface KakaoPolyline {
  setMap(map: KakaoMap | null): void;
  getMap(): KakaoMap | null;
  setPath(path: KakaoLatLng[]): void;
  getPath(): KakaoLatLng[];
  getLength(): number;
  setOptions(options: Partial<KakaoPolylineOptions>): void;
  setZIndex(zIndex: number): void;
  getZIndex(): number;
}

interface KakaoMapOptions {
  center: KakaoLatLng;
  level: number;
  mapTypeId?: number;
  draggable?: boolean;
  zoomable?: boolean;
  scrollwheel?: boolean;
  disableDoubleClick?: boolean;
  disableDoubleClickZoom?: boolean;
  projectionId?: string;
  tileAnimation?: boolean;
  keyboardShortcuts?: boolean;
}

// 이벤트 관련 타입
interface KakaoMapEvent {
  addListener(
    target: KakaoMap | KakaoMarker | KakaoInfoWindow | KakaoPolyline,
    type: string,
    callback: (event?: any) => void
  ): void;
  removeListener(
    target: KakaoMap | KakaoMarker | KakaoInfoWindow | KakaoPolyline,
    type: string,
    callback: (event?: any) => void
  ): void;
}

// 서비스 관련 타입 (간단히)
interface KakaoGeocoder {
  addressSearch(address: string, callback: (result: any[], status: any) => void): void;
  coord2Address(lng: number, lat: number, callback: (result: any[], status: any) => void): void;
}

// 글로벌 인터페이스 확장
declare global {
  interface Window {
    kakao: {
      maps: {
        // 생성자들
        LatLngBounds: new () => KakaoLatLngBounds;
        LatLng: new (lat: number, lng: number) => KakaoLatLng;
        Size: new (width: number, height: number) => KakaoSize;
        Map: new (container: HTMLElement, options: KakaoMapOptions) => KakaoMap;
        Marker: new (options: KakaoMarkerOptions) => KakaoMarker;
        MarkerImage: new (
          src: string,
          size: KakaoSize,
          options?: {
            alt?: string;
            coords?: string;
            offset?: KakaoSize;
            shape?: string;
            spriteOrigin?: KakaoSize;
            spriteSize?: KakaoSize;
          }
        ) => KakaoMarkerImage;
        InfoWindow: new (options: KakaoInfoWindowOptions) => KakaoInfoWindow;
        Polyline: new (options: KakaoPolylineOptions) => KakaoPolyline;

        // 유틸리티
        load(callback: () => void): void;
        event: KakaoMapEvent;

        // 서비스
        services: {
          Geocoder: new () => KakaoGeocoder;
          Places: new () => any; // 필요시 확장
          Status: {
            OK: string;
            ZERO_RESULT: string;
            ERROR: string;
          };
        };

        // 상수들
        MapTypeId: {
          ROADMAP: number;
          SKYVIEW: number;
          HYBRID: number;
        };

        ControlPosition: {
          TOP: number;
          TOPLEFT: number;
          TOPRIGHT: number;
          LEFT: number;
          RIGHT: number;
          BOTTOMLEFT: number;
          BOTTOM: number;
          BOTTOMRIGHT: number;
        };
      };
    };
  }
}

// 애플리케이션에서 사용할 편의 타입들
export type MapInstance = KakaoMap;
export type MarkerInstance = KakaoMarker;
export type InfoWindowInstance = KakaoInfoWindow;
export type PolylineInstance = KakaoPolyline;
export type LatLngInstance = KakaoLatLng;
export type BoundsInstance = KakaoLatLngBounds;

// 옵션 타입들 export
export type MapOptions = Partial<KakaoMapOptions>;
export type MarkerOptions = KakaoMarkerOptions;
export type InfoWindowOptions = KakaoInfoWindowOptions;
export type PolylineOptions = KakaoPolylineOptions;

// 편의 함수 타입들
export type EventCallback<T = any> = (event: T) => void;
export type MarkerClickCallback = EventCallback<{ target: KakaoMarker }>;
export type MapClickCallback = EventCallback<{ latLng: KakaoLatLng }>;

export interface MarkerInfo {
  lat: number;
  lng: number;
  name: string;
  order?: number;
  date?: string;
}
