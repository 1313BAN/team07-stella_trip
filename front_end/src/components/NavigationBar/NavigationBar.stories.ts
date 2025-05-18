import type { Meta, StoryObj } from '@storybook/vue3';
import { fn } from '@storybook/test';
import NavigationBar from './NavigationBar.vue';

// 컴포넌트 프롭스 타입 정의
interface NavigationBarProps {
  isLoggedIn?: boolean;
  activeRoute?: string;
  logout?: () => void;
}

const meta: Meta<NavigationBarProps> = {
  title: 'Components/NavigationBar',
  component: NavigationBar,
  tags: ['autodocs'],
  args: {
    isLoggedIn: false,
    activeRoute: '/',
  },
  argTypes: {
    isLoggedIn: {
      description: '로그인 상태 여부',
      control: { type: 'boolean' },
    },
    activeRoute: {
      description: '현재 활성화된 경로',
      control: { type: 'select' },
      options: ['/', '/trip-plans', '/attractions', '/mypage', '/login', '/register'],
    },
    logout: {
      description: '로그아웃 버튼 클릭 시 발생하는 이벤트',
      action: '로그아웃',
    },
  },
  parameters: {
    backgrounds: {
      default: 'dark',
      values: [
        {
          name: 'dark',
          value: 'linear-gradient(135deg, #1a0033 0%, #3a0080 50%, #000033 100%)',
        },
      ],
    },
    docs: {
      description: {
        component:
          '별여행 서비스의 네비게이션 바 컴포넌트. 로그인 상태에 따라 메뉴 아이템이 달라집니다.',
      },
    },
    layout: 'fullscreen',
  },
  decorators: [
    () => ({
      template: `
        <div style="padding-top: 5rem; min-height: 100vh;">
          <story />
        </div>
      `,
    }),
  ],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const 로그아웃상태: Story = {
  args: {
    isLoggedIn: false,
    activeRoute: '/',
  },
  render: args => ({
    components: { NavigationBar },
    setup() {
      const onLogout = fn();

      return {
        args,
        onLogout,
      };
    },
    template: `
      <NavigationBar 
        v-bind="args"
        @logout="onLogout"
      />
    `,
  }),
  parameters: {
    docs: {
      description: {
        story:
          '로그아웃 상태의 네비게이션 바입니다. 홈, 여행계획, 여행지, 로그인, 회원가입 메뉴가 표시됩니다.',
      },
    },
  },
};

export const 로그인상태: Story = {
  args: {
    isLoggedIn: true,
    activeRoute: '/',
  },
  render: args => ({
    components: { NavigationBar },
    setup() {
      const onLogout = fn();

      return {
        args,
        onLogout,
      };
    },
    template: `
      <NavigationBar 
        v-bind="args"
        @logout="onLogout"
      />
    `,
  }),
  parameters: {
    docs: {
      description: {
        story:
          '로그인 상태의 네비게이션 바입니다. 홈, 여행계획, 여행지, 마이페이지 메뉴와 로그아웃 버튼이 표시됩니다.',
      },
    },
  },
};

export const 여행계획페이지: Story = {
  args: {
    isLoggedIn: true,
    activeRoute: '/trip-plans',
  },
  render: args => ({
    components: { NavigationBar },
    setup() {
      const onLogout = fn();

      return {
        args,
        onLogout,
      };
    },
    template: `
      <NavigationBar 
        v-bind="args"
        @logout="onLogout"
      />
    `,
  }),
  parameters: {
    docs: {
      description: {
        story:
          '여행계획 페이지에서의 네비게이션 바 상태입니다. 여행계획 메뉴가 활성화되어 있습니다.',
      },
    },
  },
};

export const 여행지페이지: Story = {
  args: {
    isLoggedIn: true,
    activeRoute: '/attractions',
  },
  render: args => ({
    components: { NavigationBar },
    setup() {
      const onLogout = fn();

      return {
        args,
        onLogout,
      };
    },
    template: `
      <NavigationBar 
        v-bind="args"
        @logout="onLogout"
      />
    `,
  }),
  parameters: {
    docs: {
      description: {
        story: '여행지 페이지에서의 네비게이션 바 상태입니다. 여행지 메뉴가 활성화되어 있습니다.',
      },
    },
  },
};

export const 콘텐츠와함께표시: Story = {
  args: {
    isLoggedIn: true,
    activeRoute: '/',
  },
  render: args => ({
    components: { NavigationBar },
    setup() {
      const onLogout = fn();

      return {
        args,
        onLogout,
      };
    },
    template: `
      <div>
        <NavigationBar 
          v-bind="args"
          @logout="onLogout"
        />
        <div class="p-4 pt-20 mx-auto max-w-7xl">
          <div class="bg-black/30 backdrop-blur-md rounded-xl border border-white/20 p-6 mb-8">
            <h1 class="text-3xl font-bold text-white mb-4">StellaTrip <span class="text-purple-400">별여행</span></h1>
            <p class="text-gray-300">별자리를 테마로 한 여행 서비스입니다. 다양한 여행지와 계획을 만나보세요.</p>
          </div>
          
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div v-for="i in 6" :key="i" class="bg-black/30 backdrop-blur-md rounded-xl border border-white/20 p-4 h-48 flex items-center justify-center">
              <div class="text-center">
                <div class="text-purple-400 text-xl mb-2">여행 아이템 {{ i }}</div>
                <p class="text-gray-300 text-sm">네비게이션 바와 함께 표시되는 콘텐츠 예시입니다.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    `,
  }),
  parameters: {
    docs: {
      description: {
        story: '실제 콘텐츠와 함께 네비게이션 바가 표시되는 예시입니다.',
      },
    },
  },
};

export const 모바일뷰: Story = {
  args: {
    isLoggedIn: true,
    activeRoute: '/',
  },
  parameters: {
    viewport: {
      defaultViewport: 'mobile1',
    },
    docs: {
      description: {
        story: '모바일 화면에서의 네비게이션 바 모습입니다. 햄버거 메뉴 아이콘이 표시됩니다.',
      },
    },
  },
  render: args => ({
    components: { NavigationBar },
    setup() {
      const onLogout = fn();

      return {
        args,
        onLogout,
      };
    },
    template: `
      <div>
        <NavigationBar 
          v-bind="args"
          @logout="onLogout"
        />
        <div class="p-4 pt-20 text-center">
          <p class="text-white mt-4">모바일 뷰에서는 우측 상단의 햄버거 아이콘을 클릭하여 메뉴를 확인할 수 있습니다.</p>
        </div>
      </div>
    `,
  }),
};
