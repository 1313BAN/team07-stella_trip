import type { Meta, StoryObj } from '@storybook/vue3';
import { fn } from '@storybook/test';
import AttractionCard from './AttractionCard.vue';
import type { Attraction } from '@/services/api/domains/attraction/types';
import { ContentTypeId } from '@/constants/constant';

interface AttractionCardProps {
  attraction: Attraction;
  showRating?: boolean;
  onCardClick: (attraction: Attraction) => void;
  onLikeClick: (attraction: Attraction) => void;
  onTagClick: (contentType: string) => void;
}

const meta: Meta<AttractionCardProps> = {
  title: 'Components/card/AttractionCard',
  component: AttractionCard,
  tags: ['autodocs'],
  args: {
    onCardClick: fn(),
    onLikeClick: fn(),
    onTagClick: fn(),
    showRating: true,
  },
  argTypes: {
    showRating: {
      description: '평점 표시 여부',
      control: { type: 'boolean' },
    },
  },
  parameters: {
    backgrounds: {
      default: 'dark',
      values: [
        {
          name: 'dark',
          value: 'linear-gradient(135deg, #1a0033 0%, #3a0080 50%, #000033 100%)',
        },
      ],
    },
    docs: {
      description: {
        component: '별여행 관광지 카드 컴포넌트. 이미지, 평점, 태그 등을 표시합니다.',
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

const defaultAttraction: Attraction = {
  attractionId: 1,
  name: '성산일출봉',
  address: '제주특별자치도 서귀포시 성산읍',
  image: 'https://images.unsplash.com/photo-1539650116574-8efeb43e2750?w=400&h=250&fit=crop',
  rating: 4.8,

  contentType: ContentTypeId.TOURIST_SPOT,
  isLiked: false,
  likeCount: 756,
  latitude: 0,
  longitude: 0,
  review: [],
};

export const Default: Story = {
  args: {
    attraction: defaultAttraction,
  },
  render: args => ({
    components: { AttractionCard },
    setup() {
      return { args };
    },
    template: `
      <AttractionCard 
        v-bind="args" 
        @card-click="args.onCardClick"
        @like-click="args.onLikeClick"
        @tag-click="args.onTagClick"
      />
    `,
  }),
  parameters: {
    docs: {
      description: {
        story: '기본 상태의 관광지 카드입니다.',
      },
    },
  },
};

export const Favorited: Story = {
  args: {
    attraction: {
      ...defaultAttraction,
      isLiked: true,
      likeCount: 1245,
    },
  },
  render: args => ({
    components: { AttractionCard },
    setup() {
      return { args };
    },
    template: `
      <AttractionCard 
        v-bind="args" 
        @card-click="args.onCardClick"
        @like-click="args.onLikeClick"
        @tag-click="args.onTagClick"
      />
    `,
  }),
  parameters: {
    docs: {
      description: {
        story: '찜하기 상태가 활성화된 카드입니다.',
      },
    },
  },
};

export const PerfectRating: Story = {
  args: {
    attraction: {
      attractionId: 2,
      name: '한라산 백록담',
      address: '제주특별자치도 제주시 아라동',
      image: 'https://images.unsplash.com/photo-1557804506-669a67965ba0?w=400&h=250&fit=crop',
      rating: 5.0,

      contentType: 12,
      isLiked: false,
      likeCount: 2180,
      latitude: 0,
      longitude: 0,
      review: [],
    },
  },
  render: args => ({
    components: { AttractionCard },
    setup() {
      return { args };
    },
    template: `
      <AttractionCard 
        v-bind="args" 
        @card-click="args.onCardClick"
        @like-click="args.onLikeClick"
        @tag-click="args.onTagClick"
      />
    `,
  }),
  parameters: {
    docs: {
      description: {
        story: '완벽한 5점 평점을 받은 관광지 카드입니다.',
      },
    },
  },
};

export const FestivalType: Story = {
  args: {
    attraction: {
      attractionId: 3,
      name: '제주 별빛정원',
      address: '제주특별자치도 제주시 애월읍',
      image: 'https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=400&h=250&fit=crop',
      rating: 4.3,

      contentType: 12,
      isLiked: false,
      likeCount: 432,
      latitude: 0,
      longitude: 0,
      review: [],
    },
  },
  render: args => ({
    components: { AttractionCard },
    setup() {
      return { args };
    },
    template: `
      <AttractionCard 
        v-bind="args" 
        @card-click="args.onCardClick"
        @like-click="args.onLikeClick"
        @tag-click="args.onTagClick"
      />
    `,
  }),
  parameters: {
    docs: {
      description: {
        story: '축제 타입의 관광지 카드입니다.',
      },
    },
  },
};

export const LowRating: Story = {
  args: {
    attraction: {
      attractionId: 4,
      name: '조용한 작은 마을',
      address: '제주특별자치도 어딘가',
      image: 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=250&fit=crop',
      rating: 2.8,
      contentType: ContentTypeId.TOURIST_SPOT,
      isLiked: false,
      likeCount: 23,
      latitude: 0,
      longitude: 0,
      review: [],
    },
  },
  render: args => ({
    components: { AttractionCard },
    setup() {
      return { args };
    },
    template: `
      <AttractionCard 
        v-bind="args" 
        @card-click="args.onCardClick"
        @like-click="args.onLikeClick"
        @tag-click="args.onTagClick"
      />
    `,
  }),
  parameters: {
    docs: {
      description: {
        story: '낮은 평점을 가진 관광지 카드입니다.',
      },
    },
  },
};

export const WithoutRating: Story = {
  args: {
    attraction: defaultAttraction,
    showRating: false,
  },
  render: args => ({
    components: { AttractionCard },
    setup() {
      return { args };
    },
    template: `
      <AttractionCard 
        v-bind="args" 
        @card-click="args.onCardClick"
        @like-click="args.onLikeClick"
        @tag-click="args.onTagClick"
      />
    `,
  }),
  parameters: {
    docs: {
      description: {
        story: '평점이 숨겨진 상태의 카드입니다.',
      },
    },
  },
};

export const GridAttractions: Story = {
  render: () => ({
    components: { AttractionCard },
    setup() {
      const attractions: Attraction[] = [
        defaultAttraction,
        {
          attractionId: 5,
          name: '정방폭포',
          address: '제주특별자치도 서귀포시',
          image:
            'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=250&fit=crop',
          rating: 4.2,
          contentType: ContentTypeId.TOURIST_SPOT,
          isLiked: false,
          likeCount: 342,
          latitude: 0,
          longitude: 0,
          review: [],
        },
        {
          attractionId: 6,
          name: '용머리해안',
          address: '제주특별자치도 서귀포시 안덕면',
          image: 'https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=400&h=250&fit=crop',
          rating: 4.6,
          contentType: ContentTypeId.TOURIST_SPOT,
          isLiked: true,
          likeCount: 876,
          latitude: 0,
          longitude: 0,
          review: [],
        },
        {
          attractionId: 7,
          name: '제주 테마파크',
          address: '제주특별자치도 제주시',
          image:
            'https://images.unsplash.com/photo-1539650116574-8efeb43e2750?w=400&h=250&fit=crop',
          rating: 3.9,
          contentType: ContentTypeId.TOURIST_SPOT,
          isLiked: false,
          likeCount: 234,
          latitude: 0,
          longitude: 0,
          review: [],
        },
        {
          attractionId: 8,
          name: '제주 문화 체험',
          address: '제주특별자치도 서귀포시',
          image: 'https://images.unsplash.com/photo-1557804506-669a67965ba0?w=400&h=250&fit=crop',
          rating: 4.4,
          contentType: ContentTypeId.TOURIST_SPOT,
          isLiked: false,
          likeCount: 567,
          latitude: 0,
          longitude: 0,
          review: [],
        },
        {
          attractionId: 9,
          name: '제주 맛집 투어',
          address: '제주특별자치도 전 지역',
          image: 'https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=400&h=250&fit=crop',
          rating: 4.7,
          contentType: ContentTypeId.TOURIST_SPOT,
          isLiked: true,
          likeCount: 1234,
          latitude: 0,
          longitude: 0,
          review: [],
        },
      ];

      return {
        attractions,
        onCardClick: fn(),
        onLikeClick: fn(),
        onTagClick: fn(),
      };
    },
    template: `
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 max-w-6xl">
        <AttractionCard
          v-for="attraction in attractions"
          :key="attraction.attractionId"
          :attraction="attraction"
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
        story: '여러 개의 카드를 그리드 레이아웃으로 표시한 예시입니다.',
      },
    },
  },
};
