import type { Meta, StoryObj } from '@storybook/vue3';
import GridCardListContainer from './GridCardListContainer.vue';
import TagCard from '../TagCard/TagCard.vue';

interface GridCardListContainerProps {
  title: string;
}

const meta: Meta<GridCardListContainerProps> = {
  title: 'Components/GridCardListContainer',
  component: GridCardListContainer,
  tags: ['autodocs'],
  argTypes: {
    title: {
      description: '그리드 제목',
      control: { type: 'text' },
    },
  },
  parameters: {
    backgrounds: {
      default: 'dark',
      values: [
        { name: 'dark', value: '#0f172a' },
        { name: 'gradient', value: 'linear-gradient(135deg, #1e1b4b 0%, #312e81 100%)' },
        { name: 'light', value: '#ffffff' },
      ],
    },
    docs: {
      description: {
        component:
          '인기 여행 태그를 그리드 형태로 보여주는 컴포넌트입니다. TagCard를 슬롯으로 받아 표시합니다.',
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

// 예시 태그 데이터
const sampleTags = [
  { id: '1', label: '힐링', count: 2543 },
  { id: '2', label: '액티비티', count: 1872 },
  { id: '3', label: '미식여행', count: 1654 },
  { id: '4', label: '가족여행', count: 1432 },
  { id: '5', label: '도시여행', count: 1221 },
  { id: '6', label: '자연', count: 986 },
  { id: '7', label: '휴양여행', count: 872 },
  { id: '8', label: '커플여행', count: 754 },
];

export const Default: Story = {
  render: args => ({
    components: { GridCardListContainer, TagCard },
    setup() {
      const tags = sampleTags;
      const handleTagClick = (label: string) => {
        console.log(`태그 클릭: ${label}`);
      };

      return { args, tags, handleTagClick };
    },
    template: `
      <GridCardListContainer :title="args.title">
        <TagCard
          v-for="tag in tags"
          :key="tag.id"
          :label="tag.label"
          :labelCount="tag.count"
          @click="() => handleTagClick(tag.label)"
        />
      </GridCardListContainer>
    `,
  }),
  args: {
    title: '인기 여행 태그',
  },
  parameters: {
    docs: {
      description: {
        story: '기본 상태의 인기 태그 그리드입니다. 8개의 태그를 표시합니다.',
      },
    },
  },
};

export const FewTags: Story = {
  render: args => ({
    components: { GridCardListContainer, TagCard },
    setup() {
      const tags = sampleTags.slice(0, 3);
      const handleTagClick = (label: string) => {
        console.log(`태그 클릭: ${label}`);
      };

      return { args, tags, handleTagClick };
    },
    template: `
      <GridCardListContainer :title="args.title">
        <TagCard
          v-for="tag in tags"
          :key="tag.id"
          :label="tag.label"
          :labelCount="tag.count"
          @click="() => handleTagClick(tag.label)"
        />
      </GridCardListContainer>
    `,
  }),
  args: {
    title: '추천 여행 태그',
  },
  parameters: {
    docs: {
      description: {
        story: '적은 수의 태그만 표시하는 예시입니다.',
      },
    },
  },
};

export const DifferentTitle: Story = {
  render: args => ({
    components: { GridCardListContainer, TagCard },
    setup() {
      const tags = sampleTags;
      const handleTagClick = (label: string) => {
        console.log(`태그 클릭: ${label}`);
      };

      return { args, tags, handleTagClick };
    },
    template: `
      <GridCardListContainer :title="args.title">
        <TagCard
          v-for="tag in tags"
          :key="tag.id"
          :label="tag.label"
          :labelCount="tag.count"
          @click="() => handleTagClick(tag.label)"
        />
      </GridCardListContainer>
    `,
  }),
  args: {
    title: '테마별 여행',
  },
  parameters: {
    docs: {
      description: {
        story: '다른 제목을 가진 태그 그리드입니다.',
      },
    },
  },
};

export const CustomContent: Story = {
  render: args => ({
    components: { GridCardListContainer },
    setup() {
      return { args };
    },
    template: `
      <GridCardListContainer :title="args.title">
        <div 
          v-for="i in 8" 
          :key="i" 
          class="p-4 bg-indigo-800 rounded-lg text-white text-center"
        >
          커스텀 콘텐츠 {{i}}
        </div>
      </GridCardListContainer>
    `,
  }),
  args: {
    title: '커스텀 그리드',
  },
  parameters: {
    docs: {
      description: {
        story: 'TagCard 대신 커스텀 콘텐츠를 사용하는 예시입니다.',
      },
    },
  },
};
