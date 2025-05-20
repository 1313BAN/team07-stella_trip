import type { Meta, StoryObj } from '@storybook/vue3';
import StellaCard from './StellaCard.vue';

const meta: Meta<typeof StellaCard> = {
  title: 'Components/card/StellaCard',
  component: StellaCard,
  tags: ['autodocs'],
  parameters: {
    backgrounds: {
      default: 'dark',
      values: [
        { name: 'dark', value: '#000000' },
        { name: 'space', value: '#0a0a1a' },
      ],
    },
    layout: 'fullscreen',
  },
  decorators: [
    () => ({
      template: `
        <div style="min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 2rem;">
          <story />
        </div>
      `,
    }),
  ],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  render: () => ({
    components: { StellaCard },
    template: '<StellaCard />',
  }),
};

export const InteractiveDemo: Story = {
  render: () => ({
    components: { StellaCard },
    template: '<StellaCard />',
  }),
  parameters: {
    docs: {
      description: {
        story:
          '카드에 마우스를 올려보세요. 3D 효과와 함께 기울어집니다. 클릭하면 별자리 연결선이 빛납니다.',
      },
    },
  },
};

export const MultipleCards: Story = {
  render: () => ({
    components: { StellaCard },
    template: `
      <div style="display: flex; gap: 2rem; flex-wrap: wrap; justify-content: center;">
        <StellaCard />
        <StellaCard />
        <StellaCard />
      </div>
    `,
  }),
  parameters: {
    docs: {
      description: {
        story: '여러 개의 별자리 카드를 함께 표시한 예시입니다.',
      },
    },
  },
};
