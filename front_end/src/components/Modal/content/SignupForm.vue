<script setup lang="ts">
import { ref } from 'vue';
import { Button } from '@/components/ui/button';
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Mail, Lock, User, UserPlus, ArrowLeft } from 'lucide-vue-next';
import { useForm } from 'vee-validate';
import { z } from 'zod';
import { toTypedSchema } from '@vee-validate/zod';
import { signup } from '@/services/api/domains/auth/index';
import { toast } from 'vue-sonner';

// 폼 스키마 정의
const formSchema = toTypedSchema(
  z
    .object({
      email: z
        .string()
        .min(1, { message: '이메일을 입력해주세요.' })
        .email({ message: '유효한 이메일 주소를 입력해주세요.' }),
      name: z.string().min(1, { message: '이름을 입력해주세요.' }),
      password: z.string().min(8, { message: '비밀번호는 8자 이상이어야 합니다.' }),
      confirmPassword: z.string().min(1, { message: '비밀번호를 다시 입력해주세요.' }),
    })
    .refine(data => data.password === data.confirmPassword, {
      message: '비밀번호가 일치하지 않습니다.',
      path: ['confirmPassword'],
    })
);

const isLoading = ref(false);

// 폼 초기값과 유효성 검사
const form = useForm({
  validationSchema: formSchema,
  initialValues: {
    email: '',
    name: '',
    password: '',
    confirmPassword: '',
  },
});

const emit = defineEmits<{
  (e: 'close'): [];
  (e: 'toLogin'): [];
}>();

// 폼 제출 핸들러
const onSubmit = form.handleSubmit(async values => {
  isLoading.value = true;
  try {
    // signup API 호출
    const { email, name, password } = values;
    const signupData = { email, name, password };

    await signup(signupData).then(
      () => {
        toast('회원가입이 완료되었습니다. 로그인해주세요.');
        emit('toLogin');
      },
      error => {
        toast(error.response?.data?.body?.message || '회원가입 중 오류가 발생했습니다.');
      }
    );
  } finally {
    isLoading.value = false;
  }
});

// 로그인 페이지로 이동
const handleLogin = () => {
  emit('toLogin');
};
</script>

<template>
  <div class="relative z-10 space-y-4">
    <h2 class="mb-6 text-center text-2xl font-bold text-white">회원가입</h2>

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

      <!-- Name Input -->
      <FormField v-slot="{ componentField, handleBlur, handleChange, value }" name="name">
        <FormItem>
          <FormLabel class="text-white">이름</FormLabel>
          <div class="relative">
            <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
              <User class="h-5 w-5 text-purple-400" />
            </div>
            <FormControl>
              <input
                v-bind="componentField"
                :value="value"
                class="block w-full rounded-lg border border-purple-500/30 bg-indigo-900/50 p-2.5 pl-10 text-sm text-white focus:border-purple-500 focus:ring-purple-500"
                placeholder="사용자 이름"
                type="text"
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
      <!-- Confirm Password Input -->
      <FormField
        v-slot="{ componentField, handleBlur, handleChange, value }"
        name="confirmPassword"
      >
        <FormItem>
          <FormLabel class="text-white">비밀번호 확인</FormLabel>
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

      <!-- Signup Button -->
      <Button
        type="submit"
        class="flex w-full items-center justify-center gap-2 bg-purple-600 hover:bg-purple-700"
        :disabled="isLoading"
      >
        <UserPlus v-if="!isLoading" class="h-4 w-4" />
        <span
          v-if="isLoading"
          class="h-4 w-4 animate-spin rounded-full border-2 border-white border-t-transparent"
        ></span>
        {{ isLoading ? '가입 중...' : '회원가입' }}
      </Button>

      <!-- Login Link -->
      <div class="mt-4 text-center">
        <p class="text-sm text-gray-300">
          이미 계정이 있으신가요?
          <a
            class="inline-flex cursor-pointer items-center gap-1 text-purple-400 hover:text-purple-300"
            @click="handleLogin"
          >
            로그인
            <ArrowLeft class="h-3 w-3" />
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
