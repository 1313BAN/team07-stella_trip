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
import { Home, Map, Compass, User, LogIn, LogOut, UserPlus, Menu } from 'lucide-vue-next';

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

// Navigation items
const navItems = computed(() => {
  const commonItems = [
    { name: '홈', icon: Home, route: '/' },
    { name: '여행계획', icon: Map, route: '/trip-plans' },
    { name: '여행지', icon: Compass, route: '/attractions' },
  ];

  if (props.isLoggedIn) {
    return [...commonItems, { name: '마이페이지', icon: User, route: '/mypage' }];
  } else {
    return [
      ...commonItems,
      { name: '로그인', icon: LogIn, route: '/login' },
      { name: '회원가입', icon: UserPlus, route: '/register' },
    ];
  }
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
    class="fixed top-0 right-0 left-0 z-50 border-b border-white/20 bg-black/60 backdrop-blur-md"
  >
    <div class="mx-auto flex h-16 max-w-7xl items-center justify-between px-4">
      <!-- Logo -->
      <div class="text-2xl font-bold text-white">
        <span class="text-purple-400">Stella</span>
        Trip
      </div>

      <!-- Desktop Navigation -->
      <nav class="hidden md:flex md:gap-6">
        <template v-for="item in navItems" :key="item.name">
          <a
            :href="item.route"
            class="group flex items-center gap-2 transition-all duration-300"
            :class="[
              currentRoute === item.route
                ? 'text-purple-400'
                : 'text-gray-300 hover:text-purple-300',
            ]"
          >
            <component :is="item.icon" class="h-5 w-5" />
            <span>{{ item.name }}</span>
          </a>
        </template>

        <!-- Logout button (only visible when logged in) -->
        <button
          v-if="props.isLoggedIn"
          class="group flex items-center gap-2 text-gray-300 transition-all duration-300 hover:text-purple-300"
          @click="handleLogout"
        >
          <LogOut class="h-5 w-5" />
          <span>로그아웃</span>
        </button>
      </nav>

      <!-- Mobile Navigation -->
      <DropdownMenu>
        <DropdownMenuTrigger asChild class="md:hidden">
          <Button variant="ghost" size="icon" class="text-white">
            <Menu class="h-6 w-6" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end" class="w-56 border-white/20 bg-black/95 backdrop-blur-md">
          <template v-for="item in navItems" :key="item.name">
            <DropdownMenuItem asChild>
              <a
                :href="item.route"
                class="flex w-full items-center gap-2 px-2 py-2"
                :class="[
                  currentRoute === item.route
                    ? 'text-purple-400'
                    : 'text-gray-300 hover:text-purple-300',
                ]"
              >
                <component :is="item.icon" class="h-5 w-5" />
                <span>{{ item.name }}</span>
              </a>
            </DropdownMenuItem>
          </template>

          <!-- Logout in mobile dropdown (only when logged in) -->
          <DropdownMenuItem
            v-if="props.isLoggedIn"
            class="text-gray-300 hover:text-purple-300"
            @click="handleLogout"
          >
            <LogOut class="mr-2 h-5 w-5" />
            <span>로그아웃</span>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </div>
  </header>
</template>
