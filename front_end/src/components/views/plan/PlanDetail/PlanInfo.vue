<template>
  <Card class="border-0 bg-slate-900/30 shadow-none">
    <CardContent class="space-y-3 p-3">
      <div class="flex items-center justify-between">
        <div class="flex flex-wrap gap-2">
          <Tag
            v-for="tag in tags"
            :key="tag.tagId"
            :label="tag.name || `태그 ${tag.tagId}`"
            class="rounded-md border-purple-400/50 bg-purple-900/20 text-purple-200"
          />
        </div>
        <Button
          variant="ghost"
          size="sm"
          class="flex items-center gap-1.5 text-purple-200 hover:bg-purple-900/30"
          @click="handleClick"
        >
          <Heart v-if="isLiked" class="h-4 w-4 fill-purple-400 text-purple-400" />
          <Heart v-else class="h-4 w-4 text-gray-300" />
          <span class="text-sm font-medium">{{ likeCount }}</span>
        </Button>
      </div>

      <h2 class="text-xl font-bold text-white">{{ title }}</h2>

      <div class="flex items-start gap-2 text-sm text-gray-300">
        <Calendar class="mt-0.5 h-4 w-4 flex-shrink-0 text-purple-400" />
        <span>{{ dateRange }}</span>
      </div>

      <div v-if="description" class="mt-2 text-sm text-gray-300">
        {{ description }}
      </div>

      <div class="flex items-start gap-2 text-sm text-gray-300">
        <Users class="mt-0.5 h-4 w-4 flex-shrink-0 text-purple-400" />
        <div class="flex flex-wrap gap-1">
          <span
            v-for="writer in writers"
            :key="writer.userId"
            class="inline-block rounded-full bg-purple-900/30 px-2 py-0.5"
          >
            {{ writer.name }}
          </span>
        </div>
      </div>
    </CardContent>
  </Card>
</template>

<script setup lang="ts">
import { Heart, Calendar, Users } from 'lucide-vue-next';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import Tag from '@/components/common/Tag/Tag.vue';

const props = defineProps<{
  title?: string;
  dateRange: string;
  description?: string;
  tags?: { tagId: number; name: string }[];
  writers?: { userId: number; name: string }[];
  likeCount: number;
  isLiked?: boolean;
}>();

const emit = defineEmits<{
  toggleLike: [planId: number];
}>();

const handleClick = () => {
  emit('toggleLike', props.planId);
};
</script>
