import type { Meta, StoryObj } from '@storybook/vue3';
import { fn } from '@storybook/test';
import RowCardListContainer from './RowCardListContainer.vue';
import PlanCard from '@/components/PlanCard/PlanCard.vue';
import AttractionCard from '@/components/AttractionCard/AttractionCard.vue';
import type { Plan, Attraction } from '@/types/type';

const examplePlans: Plan[] = [
  {
    planId: 1,
    title: '제주 힐링 여행',
    description: '제주도에서 휴식과 힐링을 찾는 3박 4일 여행',
    startDate: '2025-06-01',
    endDate: '2025-06-04',
    tags: ['제주', '힐링', '휴양'],
    planWriters: [{ userId: 1, name: '김여행' }],
    likeCount: 345,
    isLiked: false,
    constellation: {
      stars: [
        { x: 30, y: 30, r: 0.8, brightness: 0.7 },
        { x: 50, y: 20, r: 0.6, brightness: 0.5 },
        { x: 70, y: 40, r: 0.9, brightness: 0.8 },
        { x: 40, y: 60, r: 0.7, brightness: 0.6 },
        { x: 20, y: 50, r: 0.5, brightness: 0.4 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 4 },
        { from: 4, to: 0 },
      ],
    },
  },
  {
    planId: 2,
    title: '서울 문화탐방',
    description: '서울의 역사와 문화를 탐방하는 2박 3일 여행',
    startDate: '2025-05-01',
    endDate: '2025-05-03',
    tags: ['서울', '문화', '박물관'],
    planWriters: [{ userId: 2, name: '역사탐험가' }],
    likeCount: 234,
    isLiked: true,
    constellation: {
      stars: [
        { x: 30, y: 30, r: 0.8, brightness: 0.7 },
        { x: 70, y: 30, r: 0.6, brightness: 0.5 },
        { x: 70, y: 70, r: 0.9, brightness: 0.8 },
        { x: 30, y: 70, r: 0.7, brightness: 0.6 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 0 },
      ],
    },
  },
  {
    planId: 3,
    title: '부산 바다 여행',
    description: '부산의 해안선을 따라가는 4박 5일 여행',
    startDate: '2025-07-10',
    endDate: '2025-07-14',
    tags: ['부산', '바다', '해변'],
    planWriters: [{ userId: 3, name: '해변러버' }],
    likeCount: 189,
    isLiked: false,
    constellation: {
      stars: [
        { x: 20, y: 20, r: 0.8, brightness: 0.7 },
        { x: 40, y: 40, r: 0.6, brightness: 0.5 },
        { x: 60, y: 20, r: 0.9, brightness: 0.8 },
        { x: 80, y: 40, r: 0.7, brightness: 0.6 },
        { x: 60, y: 60, r: 0.5, brightness: 0.4 },
      ],
      connections: [
        { from: 0, to: 1 },
        { from: 1, to: 2 },
        { from: 2, to: 3 },
        { from: 3, to: 4 },
        { from: 4, to: 1 },
      ],
    },
  },
];

const exampleAttractions: Attraction[] = [
  {
    attractionId: 1,
    name: '서울 남산타워',
    address: '서울특별시 용산구 남산공원길 105',
    contentType: '관광명소',
    image: 'https://example.com/image1.jpg',
    rating: 4.7,
    likeCount: 345,
    isLiked: false,
    latitude: 0,
    longitude: 0,
  },
  {
    attractionId: 2,
    name: '경복궁',
    address: '서울특별시 종로구 사직로 161',
    contentType: '역사유적',
    image: 'https://example.com/image2.jpg',
    rating: 4.8,
    likeCount: 298,
    isLiked: true,
    latitude: 0,
    longitude: 0,
  },
  {
    attractionId: 3,
    name: '해운대 해수욕장',
    address: '부산광역시 해운대구 해운대해변로 264',
    contentType: '자연명소',
    image: 'https://example.com/image3.jpg',
    rating: 4.6,
    likeCount: 421,
    isLiked: false,
    latitude: 0,
    longitude: 0,
  },
  {
    attractionId: 4,
    name: '제주 성산일출봉',
    address: '제주특별자치도 서귀포시 성산읍 일출로 284-12',
    contentType: '자연명소',
    image: 'https://example.com/image4.jpg',
    rating: 4.9,
    likeCount: 567,
    isLiked: true,
    latitude: 0,
    longitude: 0,
  },
  {
    attractionId: 5,
    name: '홍대 거리',
    address: '서울특별시 마포구 와우산로 29',
    contentType: '문화거리',
    image: 'https://example.com/image5.jpg',
    rating: 4.5,
    likeCount: 231,
    isLiked: false,
    latitude: 0,
    longitude: 0,
  },
];
interface RowCardListContainerProps {
  title: string;
  moreLink?: string;
}

const meta: Meta<RowCardListContainerProps> = {
  title: 'Components/RowCardListContainer',
  component: RowCardListContainer,
  tags: ['autodocs'],
  argTypes: {
    title: {
      description: '카테고리 제목',
      control: { type: 'text' },
    },
    moreLink: {
      description: '더보기 링크 URL',
      control: { type: 'text' },
    },
  },
  parameters: {
    backgrounds: {
      default: 'dark',
      values: [
        { name: 'dark', value: '#0f172a' },
        { name: 'gradient', value: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
        { name: 'light', value: '#ffffff' },
      ],
    },
    docs: {
      description: {
        component:
          '카테고리 제목과 화살표, 그리고 가로 스크롤 컨테이너를 제공하는 범용 컴포넌트입니다. 슬롯을 통해 다양한 유형의 카드를 넣을 수 있습니다.',
      },
    },
  },
  decorators: [
    () => ({
      template: `
        <div style="padding: 2rem; max-width: 1200px; margin: 0 auto; background-color: #0f172a;">
          <story />
        </div>
      `,
    }),
  ],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const WithPlanCards: Story = {
  render: args => ({
    components: { RowCardListContainer, PlanCard },
    setup() {
      const onMoreClick = fn();
      return {
        args,
        plans: examplePlans,
        onMoreClick,
      };
    },
    template: `
      <RowCardListContainer
        :title="args.title"
        :moreLink="args.moreLink"
        @moreClick="onMoreClick"
      >
        <PlanCard
          v-for="plan in plans"
          :key="plan.planId"
          :plan="plan"
          @cardClick="() => {}"
          @likeClick="() => {}"
          @tagClick="() => {}"
        />
      </RowCardListContainer>
    `,
  }),
  args: {
    title: '인기 여행 계획',
    moreLink: '/categories/popular',
  },
  parameters: {
    docs: {
      description: {
        story: '여행 계획 카드를 담은 카테고리 리스트입니다.',
      },
    },
  },
};

export const WithAttractionCards: Story = {
  render: args => ({
    components: { RowCardListContainer, AttractionCard },
    setup() {
      const onMoreClick = fn();
      return {
        args,
        attractions: exampleAttractions,
        onMoreClick,
      };
    },
    template: `
      <RowCardListContainer
        :title="args.title"
        :moreLink="args.moreLink"
        @moreClick="onMoreClick"
      >
        <AttractionCard
          v-for="attraction in attractions"
          :key="attraction.attractionId"
          :attraction="attraction"
          :showRating="true"
          @cardClick="() => {}"
          @likeClick="() => {}"
          @tagClick="() => {}"
        />
      </RowCardListContainer>
    `,
  }),
  args: {
    title: '인기 관광지',
    moreLink: '/attractions/popular',
  },
  parameters: {
    docs: {
      description: {
        story: '관광지 카드를 담은 카테고리 리스트입니다.',
      },
    },
  },
};

export const EmptyContainer: Story = {
  render: args => ({
    components: { RowCardListContainer },
    setup() {
      const onMoreClick = fn();
      return {
        args,
        onMoreClick,
      };
    },
    template: `
      <RowCardListContainer
        :title="args.title"
        :moreLink="args.moreLink"
        @moreClick="onMoreClick"
      >
        <div class="flex items-center justify-center w-full h-32 text-purple-300">
          데이터가 없습니다.
        </div>
      </RowCardListContainer>
    `,
  }),
  args: {
    title: '빈 카테고리',
    moreLink: undefined,
  },
  parameters: {
    docs: {
      description: {
        story: '컨텐츠가 없는 경우의 표시 예시입니다.',
      },
    },
  },
};

export const CustomContent: Story = {
  render: args => ({
    components: { RowCardListContainer },
    setup() {
      const onMoreClick = fn();
      return {
        args,
        onMoreClick,
      };
    },
    template: `
      <RowCardListContainer
        :title="args.title"
        :moreLink="args.moreLink"
        @moreClick="onMoreClick"
      >
        <div 
          v-for="i in 5" 
          :key="i" 
          class="w-72 h-64 rounded-2xl bg-gradient-to-br from-purple-900 to-indigo-900 flex items-center justify-center text-white text-xl font-bold"
        >
          커스텀 카드 {{i}}
        </div>
      </RowCardListContainer>
    `,
  }),
  args: {
    title: '커스텀 콘텐츠',
    moreLink: '/custom',
  },
  parameters: {
    docs: {
      description: {
        story: '다양한 커스텀 콘텐츠를 넣을 수 있는 예시입니다.',
      },
    },
  },
};
