<script setup lang="ts">
import { MapPin } from 'lucide-vue-next';
import LikeButton from '@/components/common/LikeButton/LikeButton.vue';
import StarRating from '@/components/common/StarRating/StarRating.vue';
import Tag from '@/components/common/Tag/Tag.vue';
import ImageWithFallback from '@/components/common/ImageWithFallback/ImageWithFallback.vue';
import { formatLikeCount } from '@/utils/util';
import type { Attraction } from '@/services/api/domains/attraction';

interface Props {
  attraction: Attraction;
  showRating?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  showRating: true,
});

const emit = defineEmits<{
  cardClick: [attraction: Attraction];
  likeClick: [attraction: Attraction];
  tagClick: [contentType: number];
}>();

const handleCardClick = () => {
  emit('cardClick', props.attraction);
};

const handlelikeClick = () => {
  emit('likeClick', props.attraction);
};

const handleTagClick = () => {
  emit('tagClick', props.attraction.contentType);
};

//TODO: attraction.contentType.toString() 변환 함수 구현
</script>

<template>
  <div
    class="group w-full max-w-72 min-w-56 cursor-pointer overflow-hidden rounded-2xl border border-white/20 bg-white/10 backdrop-blur-md transition-all duration-300 hover:scale-105 hover:border-purple-400/50 hover:bg-white/20 hover:shadow-xl hover:shadow-purple-500/25 focus:scale-105 focus:border-purple-400/50 focus:bg-white/20 focus:ring-2 focus:ring-purple-400/50 focus:outline-none"
    tabindex="0"
    role="button"
    @click="handleCardClick"
  >
    <!-- 이미지 섹션 -->
    <div class="relative h-48 overflow-hidden">
      <ImageWithFallback
        :src="attraction.image"
        :alt="attraction.name"
        class="group-hover:scale-105"
      />

      <!-- 그라데이션 오버레이 -->
      <div class="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent" />

      <!-- 평점 -->
      <StarRating v-if="showRating" :rating="attraction.rating" class="absolute bottom-3 left-3" />
    </div>

    <!-- 콘텐츠 섹션 -->
    <div class="flex flex-col gap-2.5 p-3">
      <!-- 제목과 위치 -->
      <div class="flex flex-col gap-1.5">
        <h3 class="line-clamp-1 text-xl font-bold text-white">
          {{ attraction.name }}
        </h3>
        <div class="flex items-center gap-1.5 text-gray-300">
          <MapPin class="h-4 w-4 text-purple-400" />
          <span class="line-clamp-1 text-sm">{{ attraction.address }}</span>
        </div>
      </div>

      <!-- 컨텐츠 타입과 좋아요 수 -->
      <div class="flex flex-col gap-1">
        <Tag :label="attraction.contentType.toString()" @click.stop="handleTagClick" />
        <div class="flex items-center gap-2 text-purple-200">
          <LikeButton
            transparent
            :isLiked="attraction.isLiked"
            size="sm"
            @click.stop="handlelikeClick"
          />
          <span class="text-sm">{{ formatLikeCount(attraction.likeCount) }} 명이 좋아해요</span>
        </div>
      </div>
    </div>
  </div>
</template>
