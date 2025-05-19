<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Map, Compass, User, LogIn, LogOut, UserPlus, Menu } from 'lucide-vue-next';

// Props for controlling auth state from parent
interface Props {
  isLoggedIn?: boolean;
  activeRoute?: string; // Add this prop for Storybook
}

const props = withDefaults(defineProps<Props>(), {
  isLoggedIn: false,
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

// Navigation items - separate regular and auth items
const menuItems = computed(() => {
  const navigationItems = [
    { name: '여행계획', icon: Map, route: '/trip-plans' },
    { name: '여행지', icon: Compass, route: '/attractions' },
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

// Emit logout event when logout is clicked
const emit = defineEmits<{
  logout: [];
}>();

const handleLogout = () => {
  emit('logout');
};
</script>

<template>
  <header
    class="fixed top-0 right-0 left-0 z-50 border-b border-white/20 bg-black/40 backdrop-blur-md"
  >
    <div class="mx-auto flex h-16 max-w-7xl items-center justify-between px-4">
      <!-- Logo -->
      <div class="text-2xl font-bold tracking-wider text-white">
        <span class="text-purple-400">Stella</span>
        Trip
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
            <span
              class="absolute bottom-0 left-0 h-0.5 w-0 bg-purple-400 transition-all duration-300 group-hover:w-full"
            ></span>
          </a>
        </template>
        <!-- User Account Dropdown -->
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              variant="ghost"
              size="icon"
              class="rounded-full text-gray-300 hover:bg-purple-800/20 hover:text-purple-200"
            >
              <User class="h-5 w-5" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            align="end"
            class="w-48 border-white/20 bg-black/95 backdrop-blur-md [&_[data-slot=dropdown-menu-item]]:hover:bg-purple-800/20 [&_[data-slot=dropdown-menu-item]]:focus:bg-purple-800/20 [&_[data-slot=dropdown-menu-item]]:focus:text-purple-200"
          >
            <div class="pointer-events-none bg-gray-800/40 px-2 py-1 text-xs text-gray-400">
              계정
            </div>
            <template v-if="props.isLoggedIn">
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
                  href="/login"
                  class="flex w-full items-center gap-3 px-3 py-3 text-sm font-medium tracking-widest uppercase"
                  :class="[
                    currentRoute === '/login'
                      ? 'text-purple-400'
                      : 'text-gray-300 hover:text-purple-200',
                  ]"
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
            class="rounded-full text-white hover:bg-purple-800/20 hover:text-purple-200"
          >
            <Menu class="h-6 w-6" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent
          align="end"
          class="w-56 border-white/20 bg-black/95 backdrop-blur-md [&_[data-slot=dropdown-menu-item]]:hover:bg-purple-800/20 [&_[data-slot=dropdown-menu-item]]:focus:bg-purple-800/20 [&_[data-slot=dropdown-menu-item]]:focus:text-purple-200"
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
          <template v-if="props.isLoggedIn">
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
                href="/login"
                class="flex w-full items-center gap-3 px-3 py-3 text-sm font-medium tracking-widest uppercase"
                :class="[
                  currentRoute === '/login'
                    ? 'text-purple-400'
                    : 'text-gray-300 hover:text-purple-200',
                ]"
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
</template>
