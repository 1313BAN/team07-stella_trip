import type { Meta, StoryObj } from '@storybook/vue3';
import ImageWithFallback from './ImageWithFallback.vue';

interface Props {
  src: string;
  alt: string;
  class?: string;
}

const meta: Meta<Props> = {
  title: 'Components/common/ImageWithFallback',
  component: ImageWithFallback,
  tags: ['autodocs'],
  parameters: {
    backgrounds: {
      default: 'light',
    },
  },
  decorators: [
    () => ({
      template: `
        <div style="width: 300px; height: 200px; border: 1px solid #e5e7eb; border-radius: 8px; overflow: hidden;">
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
    src: 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=300&fit=crop',
    alt: '아름다운 풍경 이미지',
  },
  render: args => ({
    components: { ImageWithFallback },
    setup() {
      return { args };
    },
    template: '<ImageWithFallback v-bind="args" />',
  }),
};

export const WithError: Story = {
  args: {
    src: 'https://invalid-url-that-will-fail.jpg',
    alt: '존재하지 않는 이미지',
  },
  render: args => ({
    components: { ImageWithFallback },
    setup() {
      return { args };
    },
    template: '<ImageWithFallback v-bind="args" />',
  }),
};

export const WithCustomClass: Story = {
  args: {
    src: 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=300&fit=crop',
    alt: '커스텀 클래스가 적용된 이미지',
    class: 'rounded-full',
  },
  render: args => ({
    components: { ImageWithFallback },
    setup() {
      return { args };
    },
    template: '<ImageWithFallback v-bind="args" />',
  }),
};

export const Portrait: Story = {
  args: {
    src: 'https://images.unsplash.com/photo-1494790108411-57d5e4c8b5ba?w=300&h=400&fit=crop',
    alt: '세로 이미지',
  },
  render: args => ({
    components: { ImageWithFallback },
    setup() {
      return { args };
    },
    template: '<ImageWithFallback v-bind="args" />',
  }),
  decorators: [
    () => ({
      template: `
        <div style="width: 200px; height: 300px; border: 1px solid #e5e7eb; border-radius: 8px; overflow: hidden;">
          <story />
        </div>
      `,
    }),
  ],
};
