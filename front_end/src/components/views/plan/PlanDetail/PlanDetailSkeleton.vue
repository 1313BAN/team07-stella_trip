<template>
  <div class="flex flex-col divide-y divide-white/10">
    <!-- 별자리 표시 섹션 스켈레톤 -->
    <div class="relative h-40 w-full animate-pulse bg-indigo-950/30"></div>

    <!-- 기본 정보 섹션 스켈레톤 -->
    <div class="space-y-3 bg-slate-900/30 p-3">
      <!-- 태그 및 좋아요 -->
      <div class="flex justify-between">
        <div class="flex gap-2">
          <div class="h-6 w-16 animate-pulse rounded-md bg-purple-900/20"></div>
          <div class="h-6 w-16 animate-pulse rounded-md bg-purple-900/20"></div>
        </div>
        <div class="h-6 w-10 animate-pulse rounded-md bg-purple-900/20"></div>
      </div>

      <!-- 제목 -->
      <div class="h-7 w-3/4 animate-pulse rounded-md bg-white/10"></div>

      <!-- 날짜 -->
      <div class="flex items-center gap-2">
        <div class="h-4 w-4 animate-pulse rounded-full bg-purple-900/30"></div>
        <div class="h-5 w-1/2 animate-pulse rounded-md bg-white/10"></div>
      </div>

      <!-- 작성자 -->
      <div class="flex items-center gap-2">
        <div class="h-4 w-4 animate-pulse rounded-full bg-purple-900/30"></div>
        <div class="h-5 w-1/3 animate-pulse rounded-md bg-white/10"></div>
      </div>
    </div>

    <!-- 일정 섹션 스켈레톤 -->
    <div class="bg-slate-900/20 p-3">
      <!-- 섹션 제목 -->
      <div class="mb-3 h-5 w-16 animate-pulse rounded-md bg-white/10"></div>

      <!-- 간소화된 일정 스켈레톤 -->
      <div class="space-y-4">
        <!-- 첫 번째 날짜 -->
        <div class="rounded-lg border border-white/10 bg-white/5 p-3">
          <div class="mb-3 flex items-center border-b border-white/10 pb-2">
            <div class="h-5 w-1/4 animate-pulse rounded-md bg-white/10"></div>
          </div>

          <!-- 일정 아이템들 -->
          <div class="space-y-3">
            <div v-for="i in 3" :key="i" class="flex gap-3 rounded-lg p-2">
              <div class="h-8 w-8 animate-pulse rounded-full bg-purple-900/30"></div>
              <div class="flex-1">
                <div class="mb-2 h-5 w-1/2 animate-pulse rounded-md bg-white/10"></div>
                <div class="h-4 w-3/4 animate-pulse rounded-md bg-white/10"></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 두 번째 날짜 -->
        <div class="rounded-lg border border-white/10 bg-white/5 p-3">
          <div class="mb-3 flex items-center border-b border-white/10 pb-2">
            <div class="h-5 w-1/4 animate-pulse rounded-md bg-white/10"></div>
          </div>

          <!-- 일정 아이템들 -->
          <div class="space-y-3">
            <div v-for="i in 2" :key="i" class="flex gap-3 rounded-lg p-2">
              <div class="h-8 w-8 animate-pulse rounded-full bg-purple-900/30"></div>
              <div class="flex-1">
                <div class="mb-2 h-5 w-1/2 animate-pulse rounded-md bg-white/10"></div>
                <div class="h-4 w-3/4 animate-pulse rounded-md bg-white/10"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// No props or logic needed for skeleton
</script>
