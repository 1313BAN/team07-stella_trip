<script setup lang="ts">
import { computed, reactive } from 'vue';
import { ROUTES } from '@/router/routes';
import { useRouter } from 'vue-router';
import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Map, Compass, User, LogIn, LogOut, UserPlus, Menu } from 'lucide-vue-next';
import { useAuthStore } from '@/stores/auth';
import { Toaster } from '@/components/ui/sonner';
import { logout } from '@/services/api/domains/auth/index';
import { toast } from 'vue-sonner';

import Modal from '@/components/Modal/Modal.vue';
import LoginForm from '@/components/Modal/content/LoginForm.vue';
import SignupForm from '@/components/Modal/content/SignupForm.vue';

// Props for controlling auth state from parent
interface Props {
  activeRoute?: string; // Add this prop for Storybook
}

interface ModalState {
  isOpen: boolean;
  state: 'login' | 'register' | '';
}

const props = withDefaults(defineProps<Props>(), {
  activeRoute: '/',
});

// Try to use router, but fallback if not available (for Storybook)
let router;
try {
  router = useRouter();
} catch {
  // Router not available (likely in Storybook)
  // Silent fallback for Storybook environments
  router = undefined;
}

const modalState = reactive<ModalState>({
  isOpen: false,
  state: '',
});

const isLoggedIn = computed(() => {
  const authStore = useAuthStore();
  return authStore.isAuthenticated;
});

// Navigation items - separate regular and auth items
const menuItems = computed(() => {
  const navigationItems = [
    { name: '여행계획', icon: Map, route: '/trip-plans' },
    { name: '여행지', icon: Compass, route: ROUTES.ATTRACTION.path },
  ];

  return navigationItems;
});

// Active link tracking - use router if available, otherwise use the activeRoute prop
const currentRoute = computed(() => {
  if (router) {
    return router.currentRoute.value.path;
  }
  return props.activeRoute;
});

const handleLogout = () => {
  const authStore = useAuthStore();
  logout({ refreshToken: authStore.getRefreshToken == null ? '' : authStore.getRefreshToken })
    .then(() => {
      toast('로그아웃 되었습니다.');
    })
    .catch(() => {
      // Handle error during logout
      toast('로그아웃중 오류가 발생했습니다.');
    });
  authStore.clearAuth();
};

const handleModalClose = () => {
  modalState.isOpen = false;
  modalState.state = '';
};

const handleModalOpen = (value: '' | 'login' | 'register') => {
  modalState.isOpen = true;
  modalState.state = value;
};
</script>

<template>
  <header
    class="relative top-0 right-0 left-0 z-20 max-h-16 border-b border-white/20 bg-black/40 backdrop-blur-md"
  >
    <div class="mx-auto flex h-16 max-w-7xl items-center justify-between px-4">
      <!-- Logo -->
      <div
        class="flex h-full items-center justify-center bg-gradient-to-r from-white via-purple-200 to-purple-300 bg-clip-text text-center text-3xl leading-tight font-black tracking-tight text-transparent md:text-3xl"
      >
        <span class="block">Stella Trip</span>
      </div>

      <!-- All Navigation Items - Right Aligned -->
      <div class="hidden md:flex md:items-center md:gap-12">
        <!-- Regular Navigation Items -->
        <template v-for="item in menuItems" :key="item.name">
          <a
            :href="item.route"
            class="group relative flex items-center gap-3 text-sm font-medium tracking-widest uppercase transition-all duration-300"
            :class="[
              currentRoute === item.route
                ? 'text-purple-400'
                : 'text-gray-300 hover:text-purple-200',
            ]"
          >
            <!-- <component :is="item.icon" class="h-5 w-5" /> -->
            <span>{{ item.name }}</span>
          </a>
        </template>
        <!-- User Account Dropdown -->
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              variant="ghost"
              size="icon"
              class="rounded-full text-gray-300 hover:bg-purple-500/20 hover:text-purple-200"
            >
              <User class="h-5 w-5" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            align="end"
            class="w-48 border-white/20 bg-black/90 backdrop-blur-md [&_[data-slot=dropdown-menu-item]]:hover:bg-purple-500/20 [&_[data-slot=dropdown-menu-item]]:focus:bg-purple-500/20 [&_[data-slot=dropdown-menu-item]]:focus:text-purple-200"
          >
            <div class="pointer-events-none px-2 py-1 text-xs text-gray-400">계정</div>
            <template v-if="isLoggedIn">
              <DropdownMenuItem asChild>
                <a
                  href="/mypage"
                  class="flex w-full items-center gap-3 px-3 py-3 text-sm font-medium tracking-widest uppercase"
                  :class="[
                    currentRoute === '/mypage'
                      ? 'text-purple-400'
                      : 'text-gray-300 hover:text-purple-200',
                  ]"
                >
                  <User class="h-5 w-5" />
                  <span>마이페이지</span>
                </a>
              </DropdownMenuItem>
              <DropdownMenuItem
                class="flex items-center gap-3 px-3 py-3 text-sm font-medium tracking-widest text-gray-300 uppercase hover:text-purple-200"
                @click="handleLogout"
              >
                <LogOut class="h-5 w-5" />
                <span>로그아웃</span>
              </DropdownMenuItem>
            </template>
            <template v-else>
              <DropdownMenuItem asChild>
                <a
                  class="flex w-full items-center gap-3 px-3 py-3 text-sm font-medium tracking-widest text-gray-300 uppercase hover:text-purple-200"
                  @click="handleModalOpen('register')"
                >
                  <UserPlus class="h-5 w-5" />
                  <span>회원가입</span>
                </a>
              </DropdownMenuItem>
              <DropdownMenuItem asChild>
                <a
                  class="flex w-full items-center gap-3 px-3 py-3 text-sm font-medium tracking-widest text-gray-300 uppercase hover:text-purple-200"
                  @click="handleModalOpen('login')"
                >
                  <LogIn class="h-5 w-5" />
                  <span>로그인</span>
                </a>
              </DropdownMenuItem>
            </template>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
      <!-- Mobile Navigation -->
      <DropdownMenu>
        <DropdownMenuTrigger asChild class="md:hidden">
          <Button
            variant="ghost"
            size="icon"
            class="rounded-full text-white hover:bg-purple-500/20 hover:text-purple-200"
          >
            <Menu class="h-6 w-6" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent
          align="end"
          class="w-56 border-white/20 bg-black/90 backdrop-blur-md [&_[data-slot=dropdown-menu-item]]:hover:bg-purple-500/20 [&_[data-slot=dropdown-menu-item]]:focus:bg-purple-500/20 [&_[data-slot=dropdown-menu-item]]:focus:text-purple-200"
        >
          <!-- Menu Items -->
          <template v-for="item in menuItems" :key="item.name">
            <DropdownMenuItem asChild>
              <a
                :href="item.route"
                class="flex w-full items-center gap-3 px-3 py-3 text-sm font-medium tracking-widest uppercase"
                :class="[
                  currentRoute === item.route
                    ? 'text-purple-400'
                    : 'text-gray-300 hover:text-purple-200',
                ]"
              >
                <!-- <component :is="item.icon" class="h-5 w-5" /> -->
                <span>{{ item.name }}</span>
              </a>
            </DropdownMenuItem>
          </template>
          <!-- Auth Items -->
          <div class="pointer-events-none bg-gray-800/40 px-2 py-1 text-xs text-gray-400">계정</div>
          <template v-if="isLoggedIn">
            <DropdownMenuItem asChild>
              <a
                href="/mypage"
                class="flex w-full items-center gap-3 px-3 py-3 text-sm font-medium tracking-widest uppercase"
                :class="[
                  currentRoute === '/mypage'
                    ? 'text-purple-400'
                    : 'text-gray-300 hover:text-purple-200',
                ]"
              >
                <User class="h-5 w-5" />
                <span>마이페이지</span>
              </a>
            </DropdownMenuItem>
            <DropdownMenuItem
              class="flex items-center gap-3 px-3 py-3 text-sm font-medium tracking-widest text-gray-300 uppercase hover:text-purple-200"
              @click="handleLogout"
            >
              <LogOut class="h-5 w-5" />
              <span>로그아웃</span>
            </DropdownMenuItem>
          </template>
          <template v-else>
            <DropdownMenuItem asChild>
              <a
                href="/register"
                class="flex w-full items-center gap-3 px-3 py-3 text-sm font-medium tracking-widest uppercase"
                :class="[
                  currentRoute === '/register'
                    ? 'text-purple-400'
                    : 'text-gray-300 hover:text-purple-200',
                ]"
              >
                <UserPlus class="h-5 w-5" />
                <span>회원가입</span>
              </a>
            </DropdownMenuItem>
            <DropdownMenuItem asChild>
              <a
                class="flex w-full items-center gap-3 px-3 py-3 text-sm font-medium tracking-widest uppercase"
                :class="[
                  currentRoute === '/login'
                    ? 'text-purple-400'
                    : 'text-gray-300 hover:text-purple-200',
                ]"
                @click.prevent="handleModalOpen('login')"
              >
                <LogIn class="h-5 w-5" />
                <span>로그인</span>
              </a>
            </DropdownMenuItem>
          </template>
        </DropdownMenuContent>
      </DropdownMenu>
    </div>
  </header>
  <Modal :modelValue="modalState.isOpen" maxWidth="36rem" @close="handleModalClose">
    <div>
      <LoginForm
        v-if="modalState.state === 'login'"
        @close="handleModalClose"
        @toSignup="handleModalOpen('register')"
      />
      <SignupForm
        v-if="modalState.state === 'register'"
        @close="handleModalClose"
        @toLogin="handleModalOpen('login')"
      />
    </div>
  </Modal>
  <Toaster
    :toastOptions="{
      style: { background: 'black', color: 'white' },
    }"
  />
</template>
