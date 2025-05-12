<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

function goHome(): void {
  router.push('/');
}
</script>
<template>
  <div>
    <h2>404 페이지</h2>
    <div>
      <button @click="goHome">홈으로</button>
    </div>
  </div>
</template>
