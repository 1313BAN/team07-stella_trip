import { ref, computed } from 'vue';
import type { Attraction } from '@/services/api/domains/attraction';
import type MapContainer from '@/components/map/MapContainer.vue';

export function useMapState() {
  const mapRef = ref<InstanceType<typeof MapContainer> | null>(null);
  const selectedAttraction = ref<Attraction | null>(null);
  const defaultCenter = { lat: 37.5665, lng: 126.978 };
  const mapLevel = ref(7);

  const mapCenter = computed(() => {
    if (
      selectedAttraction.value &&
      selectedAttraction.value.latitude &&
      selectedAttraction.value.longitude
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

    if (attraction.latitude && attraction.longitude && mapRef.value) {
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
