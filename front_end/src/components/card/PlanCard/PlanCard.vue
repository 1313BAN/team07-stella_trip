<script setup lang="ts">
import { ref, computed } from 'vue';
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

// 기본 별자리 정의
const defaultConstellation = {
  stars: [
    { x: 30, y: 20, r: 2, brightness: 1, delay: 0 },
    { x: 50, y: 30, r: 1.5, brightness: 0.9, delay: 0.3 },
    { x: 70, y: 20, r: 2, brightness: 0.8, delay: 0.6 },
    { x: 50, y: 50, r: 2.5, brightness: 1, delay: 0.9 },
    { x: 30, y: 80, r: 1.5, brightness: 0.7, delay: 1.2 },
    { x: 50, y: 70, r: 2, brightness: 0.85, delay: 1.5 },
    { x: 70, y: 80, r: 1.5, brightness: 0.75, delay: 1.8 },
  ],
  connections: [
    { from: 0, to: 1 },
    { from: 1, to: 2 },
    { from: 1, to: 3 },
    { from: 3, to: 4 },
    { from: 3, to: 5 },
    { from: 3, to: 6 },
    { from: 4, to: 5 },
    { from: 5, to: 6 },
  ],
};

// computed 속성을 사용하여 constellation이 없을 경우 기본값 제공
const constellation = computed(() => props.plan.stella || defaultConstellation);
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
  // 이벤트 버블링 방지
  emit('likeClick', props.plan);
};

const handleTagClick = (index: number) => {
  // 이벤트 버블링 방지
  emit('tagClick', props.plan.tags[index]);
};
</script>

<template>
  <div
    class="group w-full max-w-72 min-w-56 cursor-pointer overflow-hidden rounded-2xl border border-white/20 bg-white/10 backdrop-blur-md transition-all duration-300 hover:scale-105 hover:border-purple-400/50 hover:bg-white/20 hover:shadow-xl hover:shadow-purple-500/25 focus:scale-105 focus:border-purple-400/50 focus:bg-white/20 focus:ring-2 focus:ring-purple-400/50 focus:outline-none"
    role="button"
    tabindex="0"
    :ariaLabel="`${plan.title} 여행 루트 상세보기`"
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
          :stars="constellation.stars"
          :connections="constellation.connections"
          :backgroundStars="backgroundStars"
          :isHovered="isHovered"
        />
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
          ></TagComp>
        </div>
        <div class="flex items-center gap-2 text-purple-200">
          <LikeButton transparent :isLiked="plan.isLiked" size="sm" @likeClick="handleLikeClick" />
          <span class="text-sm">{{ formatLikeCount(plan.likeCount) }} 명이 좋아해요</span>
        </div>
      </div>
    </div>
  </div>
</template>
