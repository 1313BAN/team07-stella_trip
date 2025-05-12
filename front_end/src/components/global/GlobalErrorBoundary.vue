<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

function goHome(): void {
  router.push('/');
}

function retry(): void {
  window.location.reload();
}
</script>

<template>
  <div>
    <h2>애플리케이션에 문제가 발생했습니다</h2>
    <div>
      <button @click="retry">다시 시도</button>
      <button @click="goHome">홈으로</button>
    </div>
  </div>
</template>
