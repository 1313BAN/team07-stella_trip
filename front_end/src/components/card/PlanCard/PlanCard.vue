<script setup lang="ts">
import { ref } from 'vue';
import { Calendar } from 'lucide-vue-next';
import LikeButton from '@/components/common/LikeButton/LikeButton.vue';
import Constellation from '@/components/common/Constellation/Constellation.vue';
import TagComp from '@/components/common/Tag/Tag.vue';
import { calculatePeriod, formatLikeCount } from '@/utils/util';
import type { Plan, Tag } from '@/services/api/domains/plan/types';

interface Props {
  plan: Plan;
}

const props = defineProps<Props>();

const isHovered = ref(false);

const emit = defineEmits<{
  cardClick: [plan: Plan];
  likeClick: [plan: Plan];
  tagClick: [tag: Tag];
}>();

const backgroundStars = Array.from({ length: 30 }, () => ({
  x: Math.random() * 100,
  y: Math.random() * 100,
  r: Math.random() * 0.8 + 0.2,
  brightness: Math.random() * 0.4 + 0.2,
  duration: Math.random() * 2 + 2,
}));

const handleCardClick = () => {
  emit('cardClick', props.plan);
};

const handleLikeClick = () => {
  emit('likeClick', props.plan);
};

const handleTagClick = (index: number) => {
  const tag = props.plan.tags[index];
  if (tag) {
    emit('tagClick', tag);
  }
};
</script>

<template>
  <div
    class="group w-full max-w-72 min-w-56 cursor-pointer overflow-hidden rounded-2xl border border-white/20 bg-white/10 backdrop-blur-md transition-all duration-300 hover:scale-105 hover:border-purple-400/50 hover:bg-white/20 hover:shadow-xl hover:shadow-purple-500/25 focus:scale-105 focus:border-purple-400/50 focus:bg-white/20 focus:ring-2 focus:ring-purple-400/50 focus:outline-none"
    role="button"
    tabindex="0"
    :aria-label="`${plan.title} 여행 루트 상세보기`"
    @click="handleCardClick"
    @mouseenter="isHovered = true"
    @mouseleave="isHovered = false"
  >
    <!-- 별자리 SVG 섹션 -->
    <div class="relative h-48 overflow-hidden">
      <!-- 그라데이션 오버레이 -->
      <div
        class="absolute inset-0 bg-gradient-to-br from-slate-900 via-purple-950 to-indigo-950 opacity-70"
      />
      <div class="absolute inset-0 bg-gradient-to-t from-black/40 via-transparent to-transparent" />

      <!-- 별자리 SVG -->
      <div class="absolute inset-0 transition-transform duration-500 group-hover:scale-105">
        <Constellation
          v-if="props.plan.stella"
          :nodes="props.plan.stella.nodes"
          :edges="props.plan.stella.edges"
          :backgroundStars="backgroundStars"
          :isHovered="isHovered"
        />
        <!-- 별자리가 없을 때 기본 배경 -->
        <div v-else class="flex h-full w-full items-center justify-center text-purple-300/50">
          <Calendar class="h-16 w-16" />
        </div>
      </div>
    </div>

    <!-- 콘텐츠 섹션 -->
    <div class="flex flex-col gap-2.5 p-3">
      <!-- 제목 -->
      <div class="flex flex-col gap-1.5">
        <h3 class="line-clamp-1 text-xl font-bold text-white">
          {{ plan.title }}
        </h3>
        <div class="flex items-center gap-1.5 text-gray-300">
          <Calendar class="h-4 w-4 text-purple-400" />
          <span class="line-clamp-1 text-sm">
            {{ calculatePeriod(plan.startDate, plan.endDate) }}
          </span>
        </div>
      </div>

      <!-- 태그들과 좋아요 수 -->
      <div class="flex flex-col gap-1">
        <div class="flex flex-wrap gap-2">
          <TagComp
            v-for="(tag, index) in plan.tags.slice(0, 3)"
            :key="tag.tagId"
            :label="tag.name"
            @tagClick="handleTagClick(index)"
          />
        </div>
        <div class="flex items-center gap-2 text-purple-200">
          <LikeButton transparent :isLiked="plan.isLiked" size="sm" @likeClick="handleLikeClick" />
          <span class="text-sm">{{ formatLikeCount(plan.likeCount) }} 명이 좋아해요</span>
        </div>
      </div>
    </div>
  </div>
</template>
