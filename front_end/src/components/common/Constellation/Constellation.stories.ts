import type { Meta, StoryObj } from '@storybook/vue3';
import Constellation from './Constellation.vue';

interface Star {
  x: number;
  y: number;
  r: number;
  brightness: number;
  delay?: number;
  duration?: number;
}

interface Connection {
  from: number;
  to: number;
}

interface ConstellationProps {
  stars: Star[];
  connections: Connection[];
  backgroundStars: Star[];
  isHovered: boolean;
}

const defaultStars: Star[] = [
  { x: 20, y: 15, r: 1.5, brightness: 1, delay: 0 },
  { x: 45, y: 10, r: 2, brightness: 0.9, delay: 0.3 },
  { x: 75, y: 25, r: 1.5, brightness: 0.8, delay: 0.6 },
  { x: 85, y: 50, r: 2.5, brightness: 1, delay: 0.9 },
  { x: 60, y: 70, r: 1.5, brightness: 0.7, delay: 1.2 },
  { x: 30, y: 85, r: 2, brightness: 0.85, delay: 1.5 },
  { x: 15, y: 60, r: 1.5, brightness: 0.75, delay: 1.8 },
];

const defaultConnections: Connection[] = [
  { from: 0, to: 1 },
  { from: 1, to: 2 },
  { from: 2, to: 3 },
  { from: 3, to: 4 },
  { from: 4, to: 5 },
  { from: 5, to: 6 },
  { from: 6, to: 0 },
];

const defaultBackgroundStars: Star[] = Array.from({ length: 40 }, () => ({
  x: Math.random() * 100,
  y: Math.random() * 100,
  r: Math.random() * 0.6 + 0.3,
  brightness: Math.random() * 0.5 + 0.3,
  delay: Math.random() * 5,
  duration: Math.random() * 3 + 2,
}));

const heartStars: Star[] = [
  { x: 35, y: 30, r: 2, brightness: 1, delay: 0 },
  { x: 65, y: 30, r: 2, brightness: 1, delay: 0.2 },
  { x: 25, y: 45, r: 1.5, brightness: 0.9, delay: 0.4 },
  { x: 75, y: 45, r: 1.5, brightness: 0.9, delay: 0.6 },
  { x: 50, y: 75, r: 2.5, brightness: 1, delay: 0.8 },
  { x: 40, y: 60, r: 1, brightness: 0.7, delay: 1.0 },
  { x: 60, y: 60, r: 1, brightness: 0.7, delay: 1.2 },
];

const heartConnections: Connection[] = [
  { from: 0, to: 2 },
  { from: 2, to: 5 },
  { from: 5, to: 4 },
  { from: 4, to: 6 },
  { from: 6, to: 3 },
  { from: 3, to: 1 },
  { from: 1, to: 0 },
];

const meta: Meta<ConstellationProps> = {
  title: 'Components/common/Constellation',
  component: Constellation,
  tags: ['autodocs'],
  argTypes: {
    stars: {
      description: '별자리의 주요 별들',
      control: { type: 'object' },
    },
    connections: {
      description: '별들을 연결하는 선',
      control: { type: 'object' },
    },
    backgroundStars: {
      description: '배경의 작은 별들',
      control: { type: 'object' },
    },
    isHovered: {
      description: '호버 상태 여부',
      control: { type: 'boolean' },
    },
  },
  parameters: {
    backgrounds: {
      default: 'dark',
      values: [
        { name: 'dark', value: '#0f0f23' },
        { name: 'purple', value: '#1a1a2e' },
        {
          name: 'gradient',
          value: 'linear-gradient(135deg, #0f172a 0%, #581c87 50%, #312e81 100%)',
        },
      ],
    },
    docs: {
      description: {
        component:
          '별자리를 SVG로 렌더링하는 컴포넌트입니다. 호버 시 연결선이 실선으로 변하며, 별들이 반짝입니다.',
      },
    },
  },
  decorators: [
    () => ({
      template: `
        <div style="padding: 2rem; display: flex; align-items: center; justify-content: center; height: 400px;">
          <div style="width: 300px; height: 300px;">
            <story />
          </div>
        </div>
      `,
    }),
  ],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    stars: defaultStars,
    connections: defaultConnections,
    backgroundStars: defaultBackgroundStars,
    isHovered: false,
  },
  parameters: {
    docs: {
      description: {
        story: '기본 상태의 별자리입니다.',
      },
    },
  },
};

export const Hovered: Story = {
  args: {
    stars: defaultStars,
    connections: defaultConnections,
    backgroundStars: defaultBackgroundStars,
    isHovered: true,
  },
  parameters: {
    docs: {
      description: {
        story: '호버 상태의 별자리입니다. 연결선이 실선으로 표시됩니다.',
      },
    },
  },
};

export const HeartShape: Story = {
  args: {
    stars: heartStars,
    connections: heartConnections,
    backgroundStars: defaultBackgroundStars,
    isHovered: false,
  },
  parameters: {
    docs: {
      description: {
        story: '하트 모양의 별자리입니다.',
      },
    },
  },
};

export const HeartShapeHovered: Story = {
  args: {
    stars: heartStars,
    connections: heartConnections,
    backgroundStars: defaultBackgroundStars,
    isHovered: true,
  },
  parameters: {
    docs: {
      description: {
        story: '호버된 하트 모양의 별자리입니다.',
      },
    },
  },
};

export const WithoutBackgroundStars: Story = {
  args: {
    stars: defaultStars,
    connections: defaultConnections,
    backgroundStars: [],
    isHovered: false,
  },
  parameters: {
    docs: {
      description: {
        story: '배경별 없이 별자리만 표시된 상태입니다.',
      },
    },
  },
};
