import type { Meta, StoryObj } from '@storybook/vue3';
import MainConstellationCard from './MainConstellationCard.vue';
import type { StellaData } from '@/services/api/domains/plan/types';

const meta: Meta<typeof MainConstellationCard> = {
  title: 'Components/MainConstellationCard',
  component: MainConstellationCard,
  tags: ['autodocs'],
  parameters: {
    backgrounds: {
      default: 'dark',
    },
    layout: 'fullscreen',
  },
  decorators: [
    () => ({
      template: `
        <div style="min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 2rem; background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 50%, #0f0f23 100%);">
          <story />
        </div>
      `,
    }),
  ],
};

export default meta;
type Story = StoryObj<typeof meta>;

// 오리온 별자리 데이터
const orionStella: StellaData = {
  nodes: [
    { x: 10, y: 15, r: 2.5, brightness: 0.9, delay: 0 },
    { x: 25, y: 10, r: 3, brightness: 1.0, delay: 0.2 },
    { x: 40, y: 8, r: 2, brightness: 0.8, delay: 0.4 },
    { x: 15, y: 35, r: 2.8, brightness: 0.9, delay: 0.6 },
    { x: 30, y: 40, r: 3.2, brightness: 1.0, delay: 0.8 },
    { x: 45, y: 38, r: 2.3, brightness: 0.7, delay: 1.0 },
  ],
  edges: [
    { from: 0, to: 1 },
    { from: 1, to: 2 },
    { from: 0, to: 3 },
    { from: 1, to: 4 },
    { from: 2, to: 5 },
    { from: 3, to: 4 },
    { from: 4, to: 5 },
  ],
};
// 북두칠성 별자리 데이터 (더 좁은 국자)
const bigDipperStella: StellaData = {
  nodes: [
    // 국자 왼쪽 아래 (두베) - 오른쪽으로 이동
    { x: 18, y: 65, r: 2.9, brightness: 0.9, delay: 0 },
    // 국자 오른쪽 아래 (메라크) - 왼쪽으로 이동
    { x: 37, y: 70, r: 3.1, brightness: 1.0, delay: 0.2 },
    // 국자 오른쪽 위 (페크다) - 왼쪽으로 이동
    { x: 42, y: 45, r: 2.7, brightness: 0.8, delay: 0.4 },
    // 국자 왼쪽 위 (메그레즈) - 오른쪽으로 이동
    { x: 23, y: 45, r: 2.8, brightness: 0.9, delay: 0.6 },
    // 손잡이 첫번째 (알리오트)
    { x: 55, y: 25, r: 2.6, brightness: 0.8, delay: 0.8 },
    // 손잡이 두번째 (미자르)
    { x: 68, y: 12, r: 3.0, brightness: 0.9, delay: 1.0 },
    // 손잡이 끝 (알카이드)
    { x: 82, y: 5, r: 2.5, brightness: 0.7, delay: 1.2 },
  ],
  edges: [
    // 국자 바닥 (사각형)
    { from: 0, to: 1 }, // 두베 → 메라크 (바닥)
    { from: 1, to: 2 }, // 메라크 → 페크다 (오른쪽 벽)
    { from: 2, to: 3 }, // 페크다 → 메그레즈 (윗면)
    { from: 3, to: 0 }, // 메그레즈 → 두베 (왼쪽 벽)
    // 손잡이 (위쪽 곡선)
    { from: 2, to: 4 }, // 페크다 → 알리오트
    { from: 4, to: 5 }, // 알리오트 → 미자르 (바깥쪽 곡선)
    { from: 5, to: 6 }, // 미자르 → 알카이드 (곡선 마무리)
  ],
};

export const OrionConstellation: Story = {
  render: () => ({
    components: { MainConstellationCard },
    setup() {
      return { orionStella };
    },
    template: `
      <MainConstellationCard 
        :stella="orionStella"
        title="Orion Journey"
        subtitle="Orion"
      />
    `,
  }),
};

export const BigDipperConstellation: Story = {
  render: () => ({
    components: { MainConstellationCard },
    setup() {
      return { bigDipperStella };
    },
    template: `
      <MainConstellationCard 
        :stella="bigDipperStella"
        title="Big Dipper"
        subtitle="북두칠성"
      />
    `,
  }),
};
