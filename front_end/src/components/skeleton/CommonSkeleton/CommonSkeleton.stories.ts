import type { Meta, StoryObj } from '@storybook/vue3';
import CommonSkeleton from './CommonSkeleton.vue';

const meta: Meta<typeof CommonSkeleton> = {
  title: 'Components/skeleton/CommonSkeleton',
  component: CommonSkeleton,
  tags: ['autodocs'],
  parameters: {
    backgrounds: {
      default: 'dark',
      values: [{ name: 'dark', value: '#121212' }],
    },
  },
  decorators: [
    () => ({
      template: `
        <div style="height: 400px; position: relative;">
          <story />
        </div>
      `,
    }),
  ],
};

export default meta;
type Story = StoryObj<typeof meta>;

// 기본 스토리
export const Default: Story = {};
