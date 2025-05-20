import type { Meta, StoryObj } from '@storybook/vue3';
import { fn } from '@storybook/test';
import RowCardListContainer from './RowCardListContainer.vue';
import PlanCard from '@/components/card/PlanCard/PlanCard.vue';
import AttractionCard from '@/components/card/AttractionCard/AttractionCard.vue';
import type { Plan } from '@/services/api/domains/plan';
import type { Attraction } from '@/services/api/domains/attraction';

const examplePlans: Plan[] = [
  {
    planId: 1,
    title: '제주 힐링 여행',
    description: '제주도에서 휴식과 힐링을 찾는 3박 4일 여행',
    startDate: '2025-06-01',
    endDate: '2025-06-04',
    tags: [],
    planWriters: [{ userId: 1, name: '김여행' }],
    likeCount: 345,
    isLiked: false,
    stella: null,
    isPublic: false,
  },
  {
    planId: 2,
    title: '서울 문화탐방',
    description: '서울의 역사와 문화를 탐방하는 2박 3일 여행',
    startDate: '2025-05-01',
    endDate: '2025-05-03',
    tags: [],
    planWriters: [{ userId: 2, name: '역사탐험가' }],
    likeCount: 234,
    isLiked: true,
    stella: null,
    isPublic: false,
  },
  {
    planId: 3,
    title: '부산 바다 여행',
    description: '부산의 해안선을 따라가는 4박 5일 여행',
    startDate: '2025-07-10',
    endDate: '2025-07-14',
    tags: [],
    planWriters: [{ userId: 3, name: '해변러버' }],
    likeCount: 189,
    isLiked: false,
    stella: null,
    isPublic: false,
  },
];

const exampleAttractions: Attraction[] = [
  {
    attractionId: 1,
    name: '서울 남산타워',
    address: '서울특별시 용산구 남산공원길 105',
    contentType: 10,
    image: 'https://example.com/image1.jpg',
    rating: 4.7,
    likeCount: 345,
    isLiked: false,
    latitude: 0,
    review: [],
    longitude: 0,
  },
  {
    attractionId: 2,
    name: '경복궁',
    address: '서울특별시 종로구 사직로 161',
    contentType: 10,
    image: 'https://example.com/image2.jpg',
    rating: 4.8,
    likeCount: 298,
    isLiked: true,
    latitude: 0,
    longitude: 0,
    review: [],
  },
  {
    attractionId: 3,
    name: '해운대 해수욕장',
    address: '부산광역시 해운대구 해운대해변로 264',
    contentType: 10,
    image: 'https://example.com/image3.jpg',
    rating: 4.6,
    likeCount: 421,
    isLiked: false,
    latitude: 0,
    longitude: 0,
    review: [],
  },
  {
    attractionId: 4,
    name: '제주 성산일출봉',
    address: '제주특별자치도 서귀포시 성산읍 일출로 284-12',
    contentType: 10,
    image: 'https://example.com/image4.jpg',
    rating: 4.9,
    likeCount: 567,
    isLiked: true,
    latitude: 0,
    longitude: 0,
    review: [],
  },
  {
    attractionId: 5,
    name: '홍대 거리',
    address: '서울특별시 마포구 와우산로 29',
    contentType: 11,
    image: 'https://example.com/image5.jpg',
    rating: 4.5,
    likeCount: 231,
    isLiked: false,
    latitude: 0,
    longitude: 0,
    review: [],
  },
];
interface RowCardListContainerProps {
  title: string;
  showMoreButton?: boolean;
}

const meta: Meta<RowCardListContainerProps> = {
  title: 'Components/common/RowCardListContainer',
  component: RowCardListContainer,
  tags: ['autodocs'],
  argTypes: {
    title: {
      description: '카테고리 제목',
      control: { type: 'text' },
    },
    showMoreButton: {
      description: '더보기 버튼 표시 여부',
      control: { type: 'boolean' },
      defaultValue: true,
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
        :showMoreButton="args.showMoreButton"
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
    showMoreButton: true,
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
        :showMoreButton="args.showMoreButton"
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
    showMoreButton: true,
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
        :showMoreButton="args.showMoreButton"
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
    showMoreButton: false,
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
        :showMoreButton="args.showMoreButton"
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
    showMoreButton: true,
  },
  parameters: {
    docs: {
      description: {
        story: '다양한 커스텀 콘텐츠를 넣을 수 있는 예시입니다.',
      },
    },
  },
};
