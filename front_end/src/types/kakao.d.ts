/* eslint-disable @typescript-eslint/no-explicit-any */
declare global {
  interface Window {
    kakao: {
      maps: {
        LatLngBounds: any;
        load: (callback: () => void) => void;
        LatLng: new (lat: number, lng: number) => any;
        Map: new (container: HTMLElement, options: any) => any;
        Marker: new (options: any) => any;
        MarkerImage: new (src: string, size: any, options?: any) => any;
        Size: new (width: number, height: number) => any;
        event: {
          addListener: (target: any, type: string, callback: (...args: any[]) => void) => void;
          removeListener: (target: any, type: string, callback: (...args: any[]) => void) => void;
        };
        InfoWindow: new (options: any) => any;
        services: {
          Geocoder: new () => any;
        };
      };
    };
  }
}

export {};
