import type { Meta, StoryObj } from '@storybook/vue3';
import DailyScheduleCard from './DailyScheduleCard.vue';
import type { RouteAttraction } from '@/services/api/domains/plan';

const meta: Meta<typeof DailyScheduleCard> = {
  title: 'Components/card/DailyScheduleCard',
  component: DailyScheduleCard,
  tags: ['autodocs'],
  parameters: {
    backgrounds: {
      default: 'dark',
      values: [{ name: 'dark', value: '#121212' }],
    },
  },
};

export default meta;
type Story = StoryObj<typeof meta>;

const mockAttractions: RouteAttraction[] = [
  {
    routeId: 1,
    order: 1,
    name: '서울 타워',
    visitTime: '10:00',
    address: '서울 중구 남산공원길 105',
    memo: '야경이 예쁨',
    image: '',
    latitude: 0,
    longitude: 0,
    attractionId: 0,
    contentTypeId: 0,
    likeCount: 0,
    rating: 0,
  },
  {
    routeId: 2,
    order: 2,
    name: '광화문 광장',
    visitTime: '13:00',
    address: '서울 종로구 세종대로 172',
    memo: '사진 많이 찍기',
    image: '',
    latitude: 0,
    longitude: 0,
    attractionId: 0,
    contentTypeId: 0,
    likeCount: 0,
    rating: 0,
  },
  {
    routeId: 3,
    order: 3,
    name: '한강공원',
    visitTime: '17:00',
    address: '서울 영등포구 여의동로 330',
    memo: '',
    image: '',
    latitude: 0,
    longitude: 0,
    attractionId: 0,
    contentTypeId: 0,
    likeCount: 0,
    rating: 0,
  },
];

export const Default: Story = {
  args: {
    date: '2025-05-23',
    attractions: mockAttractions,
  },
  // 이벤트 핸들러를 테스트하고 싶다면 아래와 같이 추가
  // methods: {
  //   showRoute: (date: string, attractions: RouteAttraction[]) => {
  //     console.log('Route shown:', date, attractions);
  //   },
  // },
};
