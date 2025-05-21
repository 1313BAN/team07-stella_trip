import { ref, computed, type ComputedRef } from 'vue';
import type { Attraction } from '@/services/api/domains/attraction';
import type MapContainer from '@/components/map/MapContainer.vue';

interface UseMapStateReturn {
  mapRef: ReturnType<typeof ref<InstanceType<typeof MapContainer> | null>>;
  selectedAttraction: ReturnType<typeof ref<Attraction | null>>;
  mapLevel: ReturnType<typeof ref<number>>;
  mapCenter: ComputedRef<{ lat: number; lng: number }>;
  selectAttraction: (attraction: Attraction) => void;
}

export function useMapState(): UseMapStateReturn {
  const mapRef = ref<InstanceType<typeof MapContainer> | null>(null);
  const selectedAttraction = ref<Attraction | null>(null);
  const defaultCenter = { lat: 37.5665, lng: 126.978 };
  const mapLevel = ref(7);

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
    return defaultCenter;
  });

  const selectAttraction = (attraction: Attraction) => {
    selectedAttraction.value = attraction;

    if (attraction.latitude !== undefined && attraction.longitude !== undefined && mapRef.value) {
      mapRef.value.panTo(attraction.latitude, attraction.longitude);
      mapRef.value.setLevel(3);
    }
  };

  return {
    mapRef,
    selectedAttraction,
    mapLevel,
    mapCenter,
    selectAttraction,
  };
}
