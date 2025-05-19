import type { Meta, StoryObj } from '@storybook/vue3';
import { fn } from '@storybook/test';
import PlanCard from './PlanCard.vue';
import type { Plan, Writer } from '@/services/api/domains/plan';

interface PlanCardProps {
  plan: Plan;
  onCardClick: (plan: Plan) => void;
  onLikeClick: (plan: Plan) => void;
  onTagClick: (tag: string) => void;
}

const meta: Meta<PlanCardProps> = {
  title: 'Components/card/PlanCard',
  component: PlanCard,
  tags: [],
  args: {
    onCardClick: fn(),
    onLikeClick: fn(),
    onTagClick: fn(),
  },
  argTypes: {
    plan: {
      description: '여행 계획 데이터 (JSON 형태로 편집 가능)',
      control: {
        type: 'object',
      },
    },
  },
  parameters: {
    backgrounds: {
      default: 'dark',
      values: [
        {
          name: 'dark',
          value: 'linear-gradient(135deg, #0f0424 0%, #1a0033 50%, #3a0080 100%)',
        },
      ],
    },
    docs: {
      description: {
        component:
          '여행 계획을 바탕으로 실제 좌표 계산으로 생성된 별자리 카드 컴포넌트. 여행 루트, 기간, 태그, 좋아요 수 등을 표시합니다. 별자리 데이터는 JSON 형태로 직접 편집할 수 있습니다.',
      },
    },
  },
  decorators: [
    () => ({
      template: `
        <div style="padding: 2rem; min-height: 600px; display: flex; align-items: center; justify-content: center;">
          <story />
        </div>
      `,
    }),
  ],
};

export default meta;
type Story = StoryObj<typeof meta>;

// 기본 작성자 데이터
const defaultWriters: Writer[] = [
  { userId: 1, name: '김여행' },
  { userId: 2, name: '박탐험' },
];

// 기본 여행 계획 데이터
const defaultPlan: Plan = {
  planId: 1,
  title: '제주 힐링 여행',
  description: '제주도의 아름다운 자연과 함께하는 힐링 여행',
  startDate: '2025-05-15',
  endDate: '2025-05-18',
  tags: [],
  planWriters: defaultWriters,
  likeCount: 1234,
  isLiked: false,
  stella: null,
  isPublic: false,
};

export const Default: Story = {
  args: {
    plan: defaultPlan,
  },
  parameters: {
    docs: {
      description: {
        story:
          '기본 상태의 여행 별자리 카드입니다. 제주 여행 루트를 클로버 모양의 별자리로 표현했습니다.',
      },
    },
  },
};

export const Liked: Story = {
  args: {
    plan: {
      ...defaultPlan,
      isLiked: true,
    },
  },
  parameters: {
    docs: {
      description: {
        story: '좋아요 상태가 활성화된 카드입니다.',
      },
    },
  },
};

export const PopularRoute: Story = {
  args: {
    plan: {
      planId: 10,
      title: '서울 문화 탐방',
      description: '서울의 전통과 현대가 어우러진 문화 체험 여행',
      startDate: '2025-06-01',
      endDate: '2025-06-03',
      tags: [],
      planWriters: [{ userId: 3, name: '이서울' }],
      likeCount: 15600,
      isLiked: true,
      stella: null,
      isPublic: false,
    },
  },
  parameters: {
    docs: {
      description: {
        story:
          '많은 좋아요를 받은 인기 여행 루트입니다. 서울의 주요 명소들을 원형 패턴으로 연결했습니다.',
      },
    },
  },
};
