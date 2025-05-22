<template>
  <div class="h-full overflow-y-auto bg-slate-950/90 text-white backdrop-blur-md">
    <!-- 헤더 영역 -->
    <div
      class="sticky top-0 z-10 flex items-center justify-between border-b border-white/20 bg-slate-900/80 p-3 backdrop-blur-md"
    >
      <div class="h-7 w-3/4 animate-pulse rounded bg-slate-700"></div>
      <div class="h-8 w-8 animate-pulse rounded-full bg-slate-700"></div>
    </div>

    <!-- 메인 이미지 스켈레톤 -->
    <div class="relative h-40 w-full animate-pulse bg-slate-800"></div>

    <!-- 기본 정보 섹션 스켈레톤 -->
    <div class="space-y-3 bg-slate-900/30 p-3">
      <div class="flex items-center gap-2">
        <div class="h-6 w-20 animate-pulse rounded-md bg-slate-700"></div>
        <div class="h-6 w-16 animate-pulse rounded-md bg-slate-700"></div>
      </div>

      <div class="h-7 w-4/5 animate-pulse rounded bg-slate-700"></div>

      <div class="flex items-start gap-2">
        <div class="h-4 w-4 animate-pulse rounded bg-slate-700"></div>
        <div class="h-4 w-full animate-pulse rounded bg-slate-700"></div>
      </div>
    </div>

    <!-- 리뷰 섹션 스켈레톤 -->
    <div class="flex-1 bg-slate-900/20 p-3">
      <div class="mb-3 flex items-center justify-between">
        <div class="h-6 w-24 animate-pulse rounded bg-slate-700"></div>
        <div class="h-8 w-24 animate-pulse rounded bg-slate-700"></div>
      </div>

      <!-- 리뷰 아이템 스켈레톤 -->
      <div class="space-y-4">
        <div v-for="i in 2" :key="i" class="rounded-lg border border-white/10 bg-white/5 p-3">
          <div class="mb-2 flex items-center justify-between">
            <div class="flex items-center gap-2">
              <div class="h-8 w-8 animate-pulse rounded-full bg-slate-700"></div>
              <div>
                <div class="h-5 w-24 animate-pulse rounded bg-slate-700"></div>
                <div class="mt-1 h-3 w-32 animate-pulse rounded bg-slate-700"></div>
              </div>
            </div>
            <div class="h-4 w-20 animate-pulse rounded bg-slate-700"></div>
          </div>
          <div class="h-5 w-3/4 animate-pulse rounded bg-slate-700"></div>
          <div class="mt-2 h-16 w-full animate-pulse rounded bg-slate-700"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts"></script>
