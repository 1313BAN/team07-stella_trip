import type { Meta, StoryObj } from '@storybook/vue3';
import { fn } from '@storybook/test';
import PlanCard from './PlanCard.vue';
import type { Plan, ConstellationData, Writer } from '@/types/type';

interface PlanCardProps {
  plan: Plan;
  onCardClick: (plan: Plan) => void;
  onLikeClick: (plan: Plan) => void;
  onTagClick: (tag: string) => void;
}

const meta: Meta<PlanCardProps> = {
  title: 'Components/PlanCard',
  component: PlanCard,
  tags: ['autodocs'],
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

// 제주 힐링 여행 별자리 (클로버 모양)
const jejuConstellation: ConstellationData = {
  stars: [
    { x: 30, y: 20, r: 2, brightness: 1, delay: 0 },
    { x: 50, y: 30, r: 1.5, brightness: 0.9, delay: 0.3 },
    { x: 70, y: 20, r: 2, brightness: 0.8, delay: 0.6 },
    { x: 50, y: 50, r: 2.5, brightness: 1, delay: 0.9 },
    { x: 30, y: 80, r: 1.5, brightness: 0.7, delay: 1.2 },
    { x: 50, y: 70, r: 2, brightness: 0.85, delay: 1.5 },
    { x: 70, y: 80, r: 1.5, brightness: 0.75, delay: 1.8 },
  ],
  connections: [
    { from: 0, to: 1 },
    { from: 1, to: 2 },
    { from: 1, to: 3 },
    { from: 3, to: 4 },
    { from: 3, to: 5 },
    { from: 3, to: 6 },
    { from: 4, to: 5 },
    { from: 5, to: 6 },
  ],
};

// 기본 여행 계획 데이터
const defaultPlan: Plan = {
  planId: 'plan1',
  title: '제주 힐링 여행',
  description: '제주도의 아름다운 자연과 함께하는 힐링 여행',
  startDate: '2025-05-15',
  endDate: '2025-05-18',
  tags: ['자연치유', '해변산책', '맛집투어'],
  planWriters: defaultWriters,
  likeCount: 1234,
  isLiked: false,
  constellation: jejuConstellation,
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

// 서울 문화 탐방 별자리 (원형 패턴)
const seoulConstellation: ConstellationData = {
  stars: [
    { x: 50, y: 15, r: 2.5, brightness: 1, delay: 0 },
    { x: 75, y: 35, r: 1.5, brightness: 0.9, delay: 0.2 },
    { x: 75, y: 65, r: 2, brightness: 0.8, delay: 0.4 },
    { x: 50, y: 85, r: 1.5, brightness: 0.9, delay: 0.6 },
    { x: 25, y: 65, r: 2, brightness: 0.7, delay: 0.8 },
    { x: 25, y: 35, r: 1.5, brightness: 0.85, delay: 1.0 },
    { x: 50, y: 50, r: 1, brightness: 0.6, delay: 1.2 },
  ],
  connections: [
    { from: 0, to: 1 },
    { from: 1, to: 2 },
    { from: 2, to: 3 },
    { from: 3, to: 4 },
    { from: 4, to: 5 },
    { from: 5, to: 0 },
    { from: 6, to: 0 },
    { from: 6, to: 2 },
    { from: 6, to: 4 },
  ],
};

export const PopularRoute: Story = {
  args: {
    plan: {
      planId: 'plan2',
      title: '서울 문화 탐방',
      description: '서울의 전통과 현대가 어우러진 문화 체험 여행',
      startDate: '2025-06-01',
      endDate: '2025-06-03',
      tags: ['박물관', '전통시장', '한강야경', 'N서울타워'],
      planWriters: [{ userId: 3, name: '이서울' }],
      likeCount: 15600,
      isLiked: true,
      constellation: seoulConstellation,
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

// 부산 바다 여행 별자리 (물결 모양)
const busanConstellation: ConstellationData = {
  stars: [
    { x: 20, y: 40, r: 2, brightness: 1, delay: 0 },
    { x: 35, y: 25, r: 1.5, brightness: 0.9, delay: 0.3 },
    { x: 50, y: 35, r: 2, brightness: 0.8, delay: 0.6 },
    { x: 65, y: 20, r: 1.5, brightness: 0.9, delay: 0.9 },
    { x: 80, y: 30, r: 2, brightness: 0.7, delay: 1.2 },
    { x: 60, y: 60, r: 1.5, brightness: 0.85, delay: 1.5 },
    { x: 40, y: 70, r: 1.8, brightness: 0.75, delay: 1.8 },
  ],
  connections: [
    { from: 0, to: 1 },
    { from: 1, to: 2 },
    { from: 2, to: 3 },
    { from: 3, to: 4 },
    { from: 0, to: 6 },
    { from: 6, to: 5 },
    { from: 5, to: 4 },
  ],
};

export const WeekendTrip: Story = {
  args: {
    plan: {
      planId: 'plan3',
      title: '부산 바다 여행',
      description: '부산의 시원한 바다와 함께하는 웰컴 투 부산!',
      startDate: '2025-06-14',
      endDate: '2025-06-15',
      tags: ['해운대', '감천문화마을', '자갈치시장'],
      planWriters: [
        { userId: 4, name: '최바다' },
        { userId: 5, name: '한해변' },
      ],
      likeCount: 892,
      isLiked: false,
      constellation: busanConstellation,
    },
  },
  parameters: {
    docs: {
      description: {
        story:
          '주말 여행에 적합한 짧은 기간의 여행 루트입니다. 바다의 물결을 연상케 하는 패턴입니다.',
      },
    },
  },
};

export const ExtensiveTags: Story = {
  args: {
    plan: {
      planId: 'plan4',
      title: '경주 역사 기행',
      description: '천년고도 경주에서 만나는 우리나라의 찬란한 역사',
      startDate: '2025-07-01',
      endDate: '2025-07-05',
      tags: ['불국사', '석굴암', '천마총', '첨성대', '안압지', '황룡사지', '포석정'],
      planWriters: [{ userId: 6, name: '김역사' }],
      likeCount: 2567,
      isLiked: false,
      constellation: jejuConstellation, // 기본 별자리 재사용
    },
  },
  parameters: {
    docs: {
      description: {
        story: '많은 여행지를 포함한 카드입니다. 3개까지만 표시되고 나머지는 +N 형태로 표시됩니다.',
      },
    },
  },
};

export const FoodTrip: Story = {
  args: {
    plan: {
      planId: 'plan5',
      title: '전주 미식 여행',
      description: '전주의 맛있는 음식들을 찾아 떠나는 미식 여행',
      startDate: '2025-07-15',
      endDate: '2025-07-16',
      tags: ['비빔밥', '한옥마을', '막걸리'],
      planWriters: [
        { userId: 7, name: '박미식' },
        { userId: 8, name: '이맛집' },
      ],
      likeCount: 3456,
      isLiked: false,
      constellation: busanConstellation, // 기본 별자리 재사용
    },
  },
  parameters: {
    docs: {
      description: {
        story: '음식 중심의 여행 루트입니다.',
      },
    },
  },
};

// 강원도 자연 별자리 (산 모양)
const gangwonConstellation: ConstellationData = {
  stars: [
    { x: 20, y: 80, r: 1.5, brightness: 0.8, delay: 0 },
    { x: 35, y: 60, r: 2, brightness: 0.9, delay: 0.2 },
    { x: 50, y: 20, r: 2.5, brightness: 1, delay: 0.4 },
    { x: 65, y: 60, r: 2, brightness: 0.9, delay: 0.6 },
    { x: 80, y: 80, r: 1.5, brightness: 0.8, delay: 0.8 },
    { x: 40, y: 40, r: 1.2, brightness: 0.7, delay: 1.0 },
    { x: 60, y: 40, r: 1.2, brightness: 0.7, delay: 1.2 },
  ],
  connections: [
    { from: 0, to: 1 },
    { from: 1, to: 2 },
    { from: 2, to: 3 },
    { from: 3, to: 4 },
    { from: 1, to: 5 },
    { from: 5, to: 2 },
    { from: 2, to: 6 },
    { from: 6, to: 3 },
  ],
};

export const LongTrip: Story = {
  args: {
    plan: {
      planId: 'plan6',
      title: '강원도 자연 만끽',
      description: '강원도의 아름다운 자연 속에서 힐링하는 여행',
      startDate: '2025-08-01',
      endDate: '2025-08-07',
      tags: ['설악산', '속초해변', '정동진', '평창'],
      planWriters: [
        { userId: 9, name: '정자연' },
        { userId: 10, name: '최산악' },
      ],
      likeCount: 8900,
      isLiked: true,
      constellation: gangwonConstellation,
    },
  },
  parameters: {
    docs: {
      description: {
        story: '장기간의 자연 중심 여행 루트입니다. 강원도의 산들을 연상케 하는 봉우리 패턴입니다.',
      },
    },
  },
};

// 커플 여행 별자리 (하트 모양)
const coupleConstellation: ConstellationData = {
  stars: [
    { x: 35, y: 30, r: 2, brightness: 1, delay: 0 },
    { x: 65, y: 30, r: 2, brightness: 1, delay: 0.2 },
    { x: 25, y: 45, r: 1.5, brightness: 0.9, delay: 0.4 },
    { x: 75, y: 45, r: 1.5, brightness: 0.9, delay: 0.6 },
    { x: 50, y: 75, r: 2.5, brightness: 1, delay: 0.8 },
    { x: 40, y: 60, r: 1, brightness: 0.7, delay: 1.0 },
    { x: 60, y: 60, r: 1, brightness: 0.7, delay: 1.2 },
  ],
  connections: [
    { from: 0, to: 2 },
    { from: 2, to: 5 },
    { from: 5, to: 4 },
    { from: 4, to: 6 },
    { from: 6, to: 3 },
    { from: 3, to: 1 },
    { from: 1, to: 0 },
  ],
};

export const CoupleTrip: Story = {
  args: {
    plan: {
      planId: 'plan7',
      title: '낭만 커플 여행',
      description: '연인과 함께하는 로맨틱한 여행 코스',
      startDate: '2025-09-01',
      endDate: '2025-09-03',
      tags: ['펜션', '카페투어', '일출명소', '데이트코스'],
      planWriters: [
        { userId: 11, name: '김커플' },
        { userId: 12, name: '이로맨' },
      ],
      likeCount: 5234,
      isLiked: true,
      constellation: coupleConstellation,
    },
  },
  parameters: {
    docs: {
      description: {
        story: '커플을 위한 로맨틱한 여행 루트입니다. 하트 모양의 별자리로 사랑을 표현했습니다.',
      },
    },
  },
};

export const DayTrip: Story = {
  args: {
    plan: {
      planId: 'plan8',
      title: '대구 근교 당일치기',
      description: '대구 주변의 아름다운 명소들을 둘러보는 당일 여행',
      startDate: '2025-09-15',
      endDate: '2025-09-15',
      tags: ['팔공산', '동성로', '서문시장'],
      planWriters: [{ userId: 13, name: '조당일' }],
      likeCount: 567,
      isLiked: false,
      constellation: jejuConstellation, // 기본 별자리 재사용
    },
  },
  parameters: {
    docs: {
      description: {
        story:
          '당일치기 여행 계획입니다. calculatePeriod 함수가 당일치기로 표시하는지 확인할 수 있습니다.',
      },
    },
  },
};

export const Grid: Story = {
  render: () => ({
    components: { PlanCard },
    setup() {
      const plans: Plan[] = [
        defaultPlan,
        {
          planId: 'plan9',
          title: '인천 섬 탐험',
          description: '인천의 아름다운 섬들을 탐험하는 여행',
          startDate: '2025-10-01',
          endDate: '2025-10-02',
          tags: ['영종도', '월미도', '차이나타운'],
          planWriters: [{ userId: 14, name: '섬탐험가' }],
          likeCount: 1890,
          isLiked: true,
          constellation: seoulConstellation,
        },
        {
          planId: 'plan10',
          title: '충청도 온천 투어',
          description: '충청도의 다양한 온천에서 힐링하는 여행',
          startDate: '2025-10-15',
          endDate: '2025-10-17',
          tags: ['온양온천', '덕산온천', '도고온천', '스파리조트'],
          planWriters: [{ userId: 15, name: '온천마니아' }],
          likeCount: 2341,
          isLiked: false,
          constellation: gangwonConstellation,
        },
        {
          planId: 'plan11',
          title: '남해 드라이브',
          description: '남해의 아름다운 해안선을 따라 드라이브하는 여행',
          startDate: '2025-11-01',
          endDate: '2025-11-04',
          tags: ['독일마을', '바래길', '미조항', '상주해변'],
          planWriters: [
            { userId: 16, name: '드라이버' },
            { userId: 17, name: '해안탐험가' },
          ],
          likeCount: 4123,
          isLiked: true,
          constellation: busanConstellation,
        },
        {
          planId: 'plan12',
          title: '여수 밤바다 감상',
          description: '여수의 아름다운 밤바다를 감상하는 여행',
          startDate: '2025-11-15',
          endDate: '2025-11-16',
          tags: ['여수엑스포', '오동도', '향일암', '여수밤바다'],
          planWriters: [{ userId: 18, name: '야경러버' }],
          likeCount: 6789,
          isLiked: false,
          constellation: coupleConstellation,
        },
      ];

      return {
        plans,
        onCardClick: fn(),
        onLikeClick: fn(),
        onTagClick: fn(),
      };
    },
    template: `
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 max-w-6xl">
        <PlanCard
          v-for="plan in plans"
          :key="plan.planId"
          :plan="plan"
          @card-click="onCardClick"
          @like-click="onLikeClick"
          @tag-click="onTagClick"
        />
      </div>
    `,
  }),
  parameters: {
    docs: {
      description: {
        story: '여러 개의 여행 별자리 카드를 그리드 레이아웃으로 표시한 예시입니다.',
      },
    },
  },
};
