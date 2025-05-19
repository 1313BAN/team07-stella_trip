import type { Meta, StoryObj } from '@storybook/vue3';
import CardSkeleton from './CardSkeleton.vue';

const meta: Meta<typeof CardSkeleton> = {
  title: 'Components/common/CardSkeleton',
  component: CardSkeleton,
  tags: ['autodocs'],
  parameters: {
    backgrounds: {
      default: 'dark',
      values: [{ name: 'dark', value: '#121212' }],
    },
  },
};

export default meta;
type Story = StoryObj<typeof meta>;

// 기본 스토리
export const Default: Story = {};
