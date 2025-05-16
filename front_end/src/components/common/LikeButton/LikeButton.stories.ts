import type { Meta, StoryObj } from '@storybook/vue3';
import { fn } from '@storybook/test';
import LikeButton from './LikeButton.vue';

interface FavoriteButtonProps {
  isLiked: boolean;
  size?: 'sm' | 'md' | 'lg';
  transparent?: boolean;
  onClick: () => void;
}

const meta: Meta<FavoriteButtonProps> = {
  title: 'Components/common/LikeButton',
  component: LikeButton,
  tags: ['autodocs'],
  args: {
    onClick: fn(),
  },
  argTypes: {
    isLiked: {
      description: '즐겨찾기 상태',
      control: { type: 'boolean' },
    },
    size: {
      description: '버튼 크기',
      control: { type: 'select', options: ['sm', 'md', 'lg'] },
    },
    transparent: {
      description: '투명 배경 사용 여부',
      control: { type: 'boolean' },
    },
  },
  parameters: {
    backgrounds: {
      default: 'dark',
      values: [
        { name: 'dark', value: '#0f172a' },
        { name: 'gradient', value: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
        { name: 'light', value: '#ffffff' },
      ],
    },
    docs: {
      description: {
        component:
          '즐겨찾기 하트 버튼 컴포넌트입니다. 클릭 시 상태가 토글되며, 크기와 배경 투명도를 조절할 수 있습니다.',
      },
    },
  },
  decorators: [
    () => ({
      template: `
        <div style="padding: 2rem; display: flex; align-items: center; justify-content: center;">
          <story />
        </div>
      `,
    }),
  ],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    isLiked: false,
    size: 'md',
    transparent: false,
  },
  parameters: {
    docs: {
      description: {
        story: '기본 상태의 즐겨찾기 버튼입니다.',
      },
    },
  },
};

export const Favorite: Story = {
  args: {
    isLiked: true,
    size: 'md',
    transparent: false,
  },
  parameters: {
    docs: {
      description: {
        story: '즐겨찾기가 활성화된 상태입니다.',
      },
    },
  },
};

export const Small: Story = {
  args: {
    isLiked: false,
    size: 'sm',
    transparent: false,
  },
  parameters: {
    docs: {
      description: {
        story: '작은 크기의 버튼입니다.',
      },
    },
  },
};

export const Large: Story = {
  args: {
    isLiked: true,
    size: 'lg',
    transparent: false,
  },
  parameters: {
    docs: {
      description: {
        story: '큰 크기의 버튼입니다.',
      },
    },
  },
};

export const Transparent: Story = {
  args: {
    isLiked: false,
    size: 'md',
    transparent: true,
  },
  parameters: {
    docs: {
      description: {
        story: '투명 배경을 사용하는 버튼입니다.',
      },
    },
  },
};
