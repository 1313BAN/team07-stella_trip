<template>
  <div class="min-h-screen bg-transparent text-white">
    <div class="container mx-auto px-4 py-8">
      <div class="flex gap-8">
        <!-- 사이드바 -->
        <div class="w-64 flex-shrink-0">
          <div class="sticky top-8">
            <!-- 프로필 섹션 -->
            <div
              class="mb-6 overflow-hidden rounded-2xl border border-white/20 bg-white/10 p-6 backdrop-blur-md"
            >
              <div class="flex flex-col items-center text-center">
                <div
                  class="mb-4 h-20 w-20 overflow-hidden rounded-full border-4 border-purple-400/50"
                >
                  <img
                    :src="userProfile.image || placeholder"
                    :alt="userProfile.name"
                    class="h-full w-full object-cover"
                  />
                </div>
                <h2 class="mb-1 text-xl font-bold text-white">{{ userProfile.name }}</h2>
                <p class="text-sm text-gray-300">{{ userProfile.email }}</p>
                <p class="text-sm text-gray-300">Followers : {{ userProfile.followerCount }}명</p>
                <p class="text-sm text-gray-300">Following : {{ userProfile.followingCount }}명</p>
              </div>
            </div>

            <!-- 메뉴 섹션 -->
            <div
              class="overflow-hidden rounded-2xl border border-white/20 bg-white/10 backdrop-blur-md"
            >
              <nav class="p-2">
                <button
                  v-for="item in menuItems"
                  :key="item.id"
                  :class="[
                    'flex w-full items-center gap-3 rounded-xl px-4 py-3 text-left transition-all duration-200',
                    activeTab === item.id
                      ? 'border border-purple-400/50 bg-purple-500/30 text-white'
                      : 'text-gray-300 hover:bg-white/10 hover:text-white',
                  ]"
                  @click="activeTab = item.id"
                >
                  <component :is="item.icon" class="h-5 w-5" />
                  <span class="font-medium">{{ item.label }}</span>
                </button>
              </nav>
            </div>
          </div>
        </div>

        <!-- 메인 콘텐츠 -->
        <div class="flex-1">
          <!-- 기본정보수정 탭 -->
          <div v-if="activeTab === 'profile'" class="space-y-6">
            <h1 class="mb-6 text-3xl font-bold text-white">기본정보수정</h1>

            <div
              class="overflow-hidden rounded-2xl border border-white/20 bg-white/10 p-8 backdrop-blur-md"
            >
              <form @submit.prevent="updateProfile" class="space-y-6">
                <div class="grid grid-cols-1 gap-6 md:grid-cols-2">
                  <div>
                    <label class="mb-2 block font-medium text-white">이름</label>
                    <input
                      v-model="editForm.name"
                      type="text"
                      class="w-full rounded-xl border border-white/20 bg-white/10 px-4 py-3 text-white placeholder-gray-400 transition-all focus:border-purple-400 focus:ring-2 focus:ring-purple-400/50 focus:outline-none"
                      placeholder="이름을 입력하세요"
                    />
                  </div>
                  <div class="col-span-1 md:col-span-2">
                    <label class="mb-2 block font-medium text-white">나의 한마디</label>
                    <textarea
                      v-model="editForm.description"
                      rows="3"
                      class="w-full rounded-xl border border-white/20 bg-white/10 px-4 py-3 text-white placeholder-gray-400 transition-all focus:border-purple-400 focus:ring-2 focus:ring-purple-400/50 focus:outline-none"
                      placeholder="자신을 소개하는 한마디를 입력하세요"
                    ></textarea>
                  </div>
                </div>

                <div class="flex justify-end">
                  <button
                    type="submit"
                    class="transform rounded-xl bg-gradient-to-r from-purple-500 to-indigo-500 px-8 py-3 font-semibold text-white transition-all duration-200 hover:scale-105 hover:from-purple-600 hover:to-indigo-600 focus:ring-2 focus:ring-purple-400/50 focus:outline-none"
                  >
                    저장하기
                  </button>
                </div>
              </form>
            </div>
            <div
              class="overflow-hidden rounded-2xl border border-white/20 bg-white/10 p-8 backdrop-blur-md"
            >
              <form @submit.prevent="changePassword" class="space-y-6">
                <div class="space-y-4">
                  <div>
                    <label class="mb-2 block font-medium text-white">현재 비밀번호</label>
                    <input
                      v-model="passwordForm.currentPassword"
                      type="password"
                      class="w-full rounded-xl border border-white/20 bg-white/10 px-4 py-3 text-white placeholder-gray-400 transition-all focus:border-purple-400 focus:ring-2 focus:ring-purple-400/50 focus:outline-none"
                      placeholder="현재 비밀번호를 입력하세요"
                    />
                  </div>
                  <div>
                    <label class="mb-2 block font-medium text-white">새 비밀번호</label>
                    <input
                      v-model="passwordForm.newPassword"
                      type="password"
                      class="w-full rounded-xl border border-white/20 bg-white/10 px-4 py-3 text-white placeholder-gray-400 transition-all focus:border-purple-400 focus:ring-2 focus:ring-purple-400/50 focus:outline-none"
                      placeholder="새 비밀번호를 입력하세요"
                    />
                  </div>
                  <div>
                    <label class="mb-2 block font-medium text-white">새 비밀번호 확인</label>
                    <input
                      v-model="passwordForm.confirmPassword"
                      type="password"
                      class="w-full rounded-xl border border-white/20 bg-white/10 px-4 py-3 text-white placeholder-gray-400 transition-all focus:border-purple-400 focus:ring-2 focus:ring-purple-400/50 focus:outline-none"
                      placeholder="새 비밀번호를 다시 입력하세요"
                    />
                  </div>
                </div>

                <div class="flex justify-end">
                  <button
                    type="submit"
                    class="transform rounded-xl bg-gradient-to-r from-purple-500 to-indigo-500 px-8 py-3 font-semibold text-white transition-all duration-200 hover:scale-105 hover:from-purple-600 hover:to-indigo-600 focus:ring-2 focus:ring-purple-400/50 focus:outline-none"
                  >
                    변경하기
                  </button>
                </div>
              </form>
            </div>
          </div>

          <!-- 여행지 탭 -->
          <div v-else-if="activeTab === 'likedPlan'" class="space-y-6">
            <FavoritePlan />
          </div>

          <div v-else-if="activeTab === 'likedAttraction'" class="space-y-6">
            <FavoriteAttraction />
          </div>

          <div v-else-if="activeTab === 'myCard'" class="space-y-6"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import placeholder from '@/assets/vue.svg';
import { ref, reactive, onMounted } from 'vue';
import { User, Heart, Star } from 'lucide-vue-next';
import type { UserInfo } from '@/services/api/domains/user/types';
import { toast } from 'vue-sonner';
import { getUserInfo, putBasicUserInfo, putPasswordChange } from '@/services/api/domains/user';
import FavoritePlan from '@/components/views/myPage/FavoritePlan.vue';
import FavoriteAttraction from '@/components/views/myPage/FavoriteAttraction.vue';

// 활성 탭
const activeTab = ref('profile');

// 메뉴 아이템
const menuItems = [
  { id: 'profile', label: '기본정보수정', icon: User },
  { id: 'likedPlan', label: '좋아요한 여행 계획획', icon: Heart },
  { id: 'likedAttraction', label: '좋아요한 여행지', icon: Heart },
  { id: 'myCard', label: '내가 만든 별자리 카드드', icon: Star },
];
const userProfile = ref<UserInfo>({} as UserInfo);

const fetchUserProfile = async () => {
  try {
    // 사용자 프로필 API 호출 (예시)
    userProfile.value = await getUserInfo();
    // 프로필 이미지가 없을 경우 기본 이미지 설정
  } catch {
    toast('프로필 로드 실패');
  }
};

// 프로필 수정 폼
const editForm = reactive({
  name: userProfile.value.name || '',
  description: userProfile.value.description || '',
});

onMounted(async () => {
  await fetchUserProfile();
  // 초기 프로필 데이터 설정
  editForm.name = userProfile.value.name || '';
  editForm.description = userProfile.value.description || '';
});

// 비밀번호 변경 폼
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: '',
});

// 프로필 업데이트
const updateProfile = () => {
  putBasicUserInfo({
    ...editForm,
    image: '', // 이미지 업로드 로직 추가 필요
  })
    .then(response => {
      toast.success('프로필이 업데이트 되었습니다.');
      // 프로필 업데이트 후 사용자 정보 다시 로드
      userProfile.value = response;
    })
    .catch(() => {
      toast.error('프로필 업데이트에 실패했습니다.');
    });
};

// 비밀번호 변경
const changePassword = () => {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    toast.error('새 비밀번호가 일치하지 않습니다.');
    return;
  }
  // API 호출 로직
  putPasswordChange(passwordForm.newPassword)
    .then(() => {
      toast.success('비밀번호가 변경되었습니다.');
      // 폼 초기화
      Object.assign(passwordForm, {
        currentPassword: '',
        newPassword: '',
        confirmPassword: '',
      });
    })
    .catch(() => {
      toast.error('비밀번호 변경에 실패했습니다.');
    });
};
</script>
