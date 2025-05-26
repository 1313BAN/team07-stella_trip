import type { Meta, StoryObj } from '@storybook/vue3';
import StellaCard from './StellaCard.vue';
import type { StellaData } from '@/services/api/domains/plan/types';

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

const sampleStella: StellaData = {
  nodes: [
    { x: 10, y: 20, r: 2, brightness: 0.9, delay: 0.5 },
    { x: 30, y: 15, r: 1.5, brightness: 0.8, delay: 1.0 },
    { x: 50, y: 25, r: 2.5, brightness: 1.0, delay: 0.2 },
    { x: 20, y: 40, r: 1.8, brightness: 0.7, delay: 0.8 },
  ],
  edges: [
    { from: 0, to: 1 },
    { from: 1, to: 2 },
    { from: 0, to: 3 },
  ],
};

const sampleCardData = {
  frontTitle: '뭐적지',
  backTitle: '여행 계획 이름',
  constellation: '별자리 이름',
  keywords: '여행 계획 태그 목록',
  description: '간단 설명',
  uprightMeaning: '뭘 써야 하지',
  reversedMeaning: '여기에 ai쓸까',
};

export const Default: Story = {
  render: () => ({
    components: { StellaCard },
    setup() {
      return {
        sampleStella,
        sampleCardData,
      };
    },
    template: '<StellaCard :stella="sampleStella" :cardData="sampleCardData" />',
  }),
};

export const InteractiveDemo: Story = {
  render: () => ({
    components: { StellaCard },
    setup() {
      return {
        sampleStella,
        sampleCardData,
      };
    },
    template: '<StellaCard :stella="sampleStella" :cardData="sampleCardData" />',
  }),
  parameters: {
    docs: {
      description: {
        story:
          '카드에 마우스를 올려보세요. 3D 효과와 함께 기울어집니다. 클릭하면 카드가 뒤집힙니다.',
      },
    },
  },
};

export const MultipleCards: Story = {
  render: () => ({
    components: { StellaCard },
    setup() {
      return {
        sampleStella,
        sampleCardData,
      };
    },
    template: `
      <div style="display: flex; gap: 2rem; flex-wrap: wrap; justify-content: center;">
        <StellaCard :stella="sampleStella" :cardData="sampleCardData" />
        <StellaCard :stella="sampleStella" :cardData="sampleCardData" />
        <StellaCard :stella="sampleStella" :cardData="sampleCardData" />
      </div>
    `,
  }),
};
