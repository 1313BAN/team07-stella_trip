<script setup lang="ts">
import { ref } from 'vue';
import { Button } from '@/components/ui/button';
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Mail, Lock, LogIn, UserPlus } from 'lucide-vue-next';
import { useForm } from 'vee-validate';
import { z } from 'zod';
import { toTypedSchema } from '@vee-validate/zod';
import { login } from '@/services/api/domains/auth/index';
import { useAuthStore } from '@/stores/auth';
import { User, AuthToken } from '@/services/api/types';

// 폼 스키마 정의
const formSchema = toTypedSchema(
  z.object({
    email: z
      .string()
      .min(1, { message: '이메일을 입력해주세요.' })
      .email({ message: '유효한 이메일 주소를 입력해주세요.' }),
    password: z.string().min(1, { message: '비밀번호를 입력해주세요.' }),
  })
);

const isLoading = ref(false);

// 폼 초기값과 유효성 검사
const form = useForm({
  validationSchema: formSchema,
  initialValues: {
    email: '',
    password: '',
  },
});

const emit = defineEmits<{
  close: [];
}>();

// 폼 제출 핸들러
const onSubmit = form.handleSubmit(async values => {
  isLoading.value = true;
  try {
    // API 호출 구현 예정
    // await new Promise(resolve => setTimeout(resolve, 800));
    login(values).then(res => {
      useAuthStore().setAuth(
        {
          id: res.id,
          email: res.email,
          username: res.name,
        } as User,
        {
          accessToken: res.accessToken,
          refreshToken: res.refreshToken,
        } as AuthToken
      );
      emit('close');
    });
  } catch (error) {
    // 로그인 오류 처리
  } finally {
    isLoading.value = false;
  }
});

// Register redirect
const handleRegister = () => {
  // 회원가입 페이지로 이동 로직
};
</script>

<template>
  <div class="relative z-10 space-y-4">
    <h2 class="mb-6 text-center text-2xl font-bold text-white">로그인</h2>

    <form class="space-y-4" @submit="onSubmit">
      <!-- Email Input -->
      <FormField v-slot="{ componentField, handleBlur, handleChange, value }" name="email">
        <FormItem>
          <FormLabel class="text-white">이메일</FormLabel>
          <div class="relative">
            <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
              <Mail class="h-5 w-5 text-purple-400" />
            </div>
            <FormControl>
              <input
                v-bind="componentField"
                :value="value"
                class="block w-full rounded-lg border border-purple-500/30 bg-indigo-900/50 p-2.5 pl-10 text-sm text-white focus:border-purple-500 focus:ring-purple-500"
                placeholder="name@stellatrip.com"
                type="email"
                @input="handleChange"
                @blur="handleBlur"
              />
            </FormControl>
          </div>
          <FormMessage class="text-red-400" />
        </FormItem>
      </FormField>

      <!-- Password Input -->
      <FormField v-slot="{ componentField, handleBlur, handleChange, value }" name="password">
        <FormItem>
          <FormLabel class="text-white">비밀번호</FormLabel>
          <div class="relative">
            <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
              <Lock class="h-5 w-5 text-purple-400" />
            </div>
            <FormControl>
              <input
                v-bind="componentField"
                :value="value"
                class="block w-full rounded-lg border border-purple-500/30 bg-indigo-900/50 p-2.5 pl-10 text-sm text-white focus:border-purple-500 focus:ring-purple-500"
                placeholder="••••••••"
                type="password"
                @input="handleChange"
                @blur="handleBlur"
              />
            </FormControl>
          </div>
          <FormMessage class="text-red-400" />
        </FormItem>
      </FormField>

      <!-- Login Button -->
      <Button
        type="submit"
        class="flex w-full items-center justify-center gap-2 bg-purple-600 hover:bg-purple-700"
        :disabled="isLoading"
      >
        <LogIn v-if="!isLoading" class="h-4 w-4" />
        <span
          v-if="isLoading"
          class="h-4 w-4 animate-spin rounded-full border-2 border-white border-t-transparent"
        ></span>
        {{ isLoading ? '로그인 중...' : '로그인' }}
      </Button>

      <!-- Register Link -->
      <div class="mt-4 text-center">
        <p class="text-sm text-gray-300">
          계정이 없으신가요?
          <a
            class="inline-flex cursor-pointer items-center gap-1 text-purple-400 hover:text-purple-300"
            @click="handleRegister"
          >
            회원가입
            <UserPlus class="h-3 w-3" />
          </a>
        </p>
      </div>
    </form>
  </div>
</template>

<style scoped>
.animate-spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

a {
  transition: all 0.2s ease;
}
</style>
