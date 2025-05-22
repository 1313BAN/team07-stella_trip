<template>
  <Card
    class="cursor-pointer border-white/10 bg-white/5 shadow-none transition-all duration-200 hover:bg-white/10"
    @click="showRouteOnMap"
  >
    <CardHeader class="border-b border-white/10 p-3 pb-2">
      <h4 class="flex items-center text-lg font-semibold text-white">
        <Calendar class="mr-2 h-5 w-5 text-purple-400" />
        {{ date }}
      </h4>
    </CardHeader>
    <CardContent class="p-3">
      <div class="space-y-3">
        <AttractionItem
          v-for="attraction in sortedAttractions"
          :key="attraction.routeId"
          :order="attraction.order"
          :name="attraction.name"
          :visitTime="attraction.visitTime"
          :address="attraction.address"
          :memo="attraction.memo ?? ''"
        />
      </div>
    </CardContent>
  </Card>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { Calendar } from 'lucide-vue-next';
import { Card, CardHeader, CardContent } from '@/components/ui/card';
import AttractionItem from './AttractionItem.vue';
import type { RouteAttraction } from '@/services/api/domains/plan';

const props = defineProps<{
  date: string;
  attractions: RouteAttraction[];
}>();

const emit = defineEmits<{
  (e: 'showRoute', date: string, attractions: RouteAttraction[]): void;
}>();

const sortedAttractions = computed(() => {
  if (!props.attractions || !Array.isArray(props.attractions)) return [];
  return [...props.attractions].sort((a, b) => a.order - b.order);
});

const showRouteOnMap = () => {
  emit('showRoute', props.date, props.attractions);
};
</script>
