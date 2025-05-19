import type { Meta, StoryObj } from '@storybook/vue3';
import { fn } from '@storybook/test';
import ErrorContent from './ErrorContent.vue';

interface ErrorContentProps {
  error: Error | null;
  resetError: () => void;
  title?: string;
  description?: string;
  showRetry?: boolean;
}

const meta: Meta<ErrorContentProps> = {
  title: 'Components/common/ErrorContent',
  component: ErrorContent,
  tags: ['autodocs'],
  args: {
    error: new Error('테스트 에러 메시지'),
    resetError: fn(),
  },
  parameters: {
    backgrounds: {
      default: 'dark',
    },
  },
  decorators: [
    () => ({
      template: `
        <div style="padding: 20px;">
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
    title: '오류가 발생했습니다',
    description: '데이터를 불러오는 중 문제가 발생했습니다',
    showRetry: true,
  },
  render: args => ({
    components: { ErrorContent },
    setup() {
      return { args };
    },
    template: '<ErrorContent v-bind="args" />',
  }),
};

export const WithoutTitle: Story = {
  args: {
    description: '데이터를 불러오는 중 문제가 발생했습니다',
    showRetry: true,
  },
};

export const WithoutRetryButton: Story = {
  args: {
    title: '오류가 발생했습니다',
    description: '데이터를 불러오는 중 문제가 발생했습니다',
    showRetry: false,
  },
};
