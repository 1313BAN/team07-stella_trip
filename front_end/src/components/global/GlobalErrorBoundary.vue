<script setup lang="ts">
import { AlertTriangle, Home, RotateCcw } from 'lucide-vue-next';
import { HeroBackground } from '@/components/background';

function goHome(): void {
  window.location.href = '/';
  window.location.reload();
}

function retry(): void {
  window.location.reload();
}
</script>

<template>
  <div class="relative min-h-screen overflow-hidden">
    <HeroBackground />

    <!-- Error Container -->
    <div class="relative z-10 flex min-h-screen flex-col items-center justify-center px-4">
      <!-- Error Icon Animation -->
      <div class="mb-8 text-center">
        <!-- Large Error Icon -->
        <div class="mb-6 flex justify-center">
          <AlertTriangle
            class="h-32 w-32 animate-pulse text-red-400/90 transition-all duration-1000"
          />
        </div>

        <!-- Floating warning icons -->
        <div class="relative">
          <AlertTriangle
            class="absolute -top-4 -left-16 h-6 w-6 animate-pulse text-orange-400 delay-[2500ms]"
          />
          <AlertTriangle
            class="absolute -top-8 -right-12 h-4 w-4 animate-pulse text-red-300 delay-1000"
          />
          <AlertTriangle
            class="absolute top-12 -left-20 h-5 w-5 animate-pulse text-yellow-400 delay-[2000ms]"
          />
          <AlertTriangle
            class="absolute top-8 -right-24 h-3 w-3 animate-pulse text-pink-400 delay-[1500ms]"
          />
        </div>
      </div>

      <!-- Content -->
      <div class="text-center transition-all delay-300 duration-1000">
        <h1 class="mb-4 text-4xl font-bold text-white md:text-5xl">앗, 문제가 발생했어요</h1>
        <p class="mb-2 text-lg text-red-200/80 md:text-xl">
          애플리케이션에서 예상치 못한 오류가 발생했습니다
        </p>
        <p class="mb-12 text-red-300/60">잠시 후 다시 시도해보거나 홈으로 돌아가세요</p>

        <!-- Action Buttons -->
        <div class="flex flex-col gap-4 sm:flex-row sm:justify-center">
          <button
            class="group flex items-center justify-center gap-2 rounded-xl bg-red-600 px-8 py-4 font-semibold text-white transition-all duration-300 hover:scale-105 hover:bg-red-500 focus:ring-4 focus:ring-red-600/20 focus:outline-none active:scale-95"
            @click="retry"
          >
            <RotateCcw class="h-5 w-5 transition-transform group-hover:rotate-180" />
            <span>다시 시도</span>
          </button>

          <button
            class="group flex items-center justify-center gap-2 rounded-xl border-2 border-red-400/50 px-8 py-4 font-semibold text-red-300 transition-all duration-300 hover:scale-105 hover:border-red-400 hover:bg-red-400/10 focus:ring-4 focus:ring-red-400/20 focus:outline-none active:scale-95"
            @click="goHome"
          >
            <Home class="h-5 w-5 transition-transform group-hover:scale-110" />
            <span>홈으로 돌아가기</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
