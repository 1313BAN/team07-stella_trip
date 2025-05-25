<template>
  <Card class="border-0 bg-slate-900/30 shadow-none">
    <CardContent class="space-y-3 p-3">
      <div class="flex items-center justify-between">
        <div class="flex flex-wrap items-center gap-2">
          <Tag
            v-for="tag in plan?.tags"
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
          <Heart v-if="plan?.isLiked" class="h-4 w-4 fill-purple-400 text-purple-400" />
          <Heart v-else class="h-4 w-4 text-gray-300" />
          <span class="text-sm font-medium">{{ plan?.likeCount || 0 }}</span>
        </Button>
      </div>

      <h2 class="text-xl font-bold text-white">{{ plan?.title }}</h2>

      <div class="flex items-start gap-2 text-sm text-gray-300">
        <Calendar class="mt-0.5 h-4 w-4 flex-shrink-0 text-purple-400" />
        <span>{{ formatDateRange(plan?.startDate, plan?.endDate) }}</span>
      </div>

      <div v-if="plan?.description" class="mt-2 text-sm text-gray-300">
        {{ plan.description }}
      </div>

      <div class="flex items-start gap-2 text-sm text-gray-300">
        <Users class="mt-0.5 h-4 w-4 flex-shrink-0 text-purple-400" />
        <div class="flex flex-wrap gap-1">
          <span
            v-for="writer in plan?.planWriters"
            :key="writer.userId"
            class="flex items-center justify-center rounded-full bg-purple-900/30 px-2 py-0.5"
          >
            {{ writer.name }}
          </span>
          <!-- 초대하기 버튼 -->
          <Button
            variant="ghost"
            size="sm"
            class="flex items-center gap-1.5 rounded-full border border-blue-500/30 px-3 py-1 text-blue-300 hover:bg-blue-900/30"
            @click="handleInvite"
          >
            <UserPlus class="h-3 w-3" />
            <span class="text-xs font-medium">초대하기</span>
          </Button>
        </div>
      </div>
    </CardContent>
  </Card>
</template>

<script setup lang="ts">
import { Heart, Calendar, Users, UserPlus } from 'lucide-vue-next';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import Tag from '@/components/common/Tag/Tag.vue';
import type { PlanDetail } from '@/services/api/domains/plan';

const props = defineProps<{
  plan?: PlanDetail | null;
}>();

const emit = defineEmits<{
  toggleLike: [planId: number];
  invite: [planId: number];
}>();

// 날짜 범위 표시 형식
const formatDateRange = (startDate?: string, endDate?: string) => {
  if (!startDate || !endDate) return '';

  const start = new Date(startDate);
  const end = new Date(endDate);

  const startStr = `${start.getFullYear()}년 ${start.getMonth() + 1}월 ${start.getDate()}일`;
  const endStr = `${end.getFullYear()}년 ${end.getMonth() + 1}월 ${end.getDate()}일`;

  return `${startStr} ~ ${endStr}`;
};

const handleClick = () => {
  if (props.plan?.planId) {
    emit('toggleLike', props.plan.planId);
  }
};

const handleInvite = () => {
  if (props.plan?.planId) {
    emit('invite', props.plan.planId);
  }
};
</script>
