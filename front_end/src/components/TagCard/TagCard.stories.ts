import type { Meta, StoryObj } from '@storybook/vue3';
import { fn } from '@storybook/test';
import TagCard from './TagCard.vue';

interface TagCardProps {
  label: string;
  labelCount: number;
}

const meta: Meta<TagCardProps> = {
  title: 'Components/TagCard',
  component: TagCard,
  tags: ['autodocs'],
  argTypes: {
    label: {
      description: '태그 라벨',
      control: { type: 'text' },
    },
    labelCount: {
      description: '태그에 해당하는 여행 수',
      control: { type: 'number' },
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
          '여행 태그를 표시하는 카드 컴포넌트입니다. 태그 이름과 해당 태그에 속한 여행 수를 표시합니다.',
      },
    },
    actions: {
      handles: ['click'],
    },
  },
  decorators: [
    () => ({
      template: `
        <div style="padding: 2rem; max-width: 600px; margin: 0 auto;">
          <story />
        </div>
      `,
    }),
  ],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  render: args => ({
    components: { TagCard },
    setup() {
      const onClickHandler = fn();
      return { args, onClickHandler };
    },
    template: `
      <TagCard
        :label="args.label"
        :labelCount="args.labelCount"
        @click="onClickHandler"
      />
    `,
  }),
  args: {
    label: '도시여행',
    labelCount: 1221,
  },
  parameters: {
    docs: {
      description: {
        story: '기본 상태의 태그 카드입니다.',
      },
    },
  },
};

export const SmallCount: Story = {
  render: args => ({
    components: { TagCard },
    setup() {
      const onClickHandler = fn();
      return { args, onClickHandler };
    },
    template: `
      <TagCard
        :label="args.label"
        :labelCount="args.labelCount"
        @click="onClickHandler"
      />
    `,
  }),
  args: {
    label: '로컬체험',
    labelCount: 42,
  },
  parameters: {
    docs: {
      description: {
        story: '적은 수의 여행을 가진 태그 카드입니다.',
      },
    },
  },
};

export const LargeCount: Story = {
  render: args => ({
    components: { TagCard },
    setup() {
      const onClickHandler = fn();
      return { args, onClickHandler };
    },
    template: `
      <TagCard
        :label="args.label"
        :labelCount="args.labelCount"
        @click="onClickHandler"
      />
    `,
  }),
  args: {
    label: '인기여행',
    labelCount: 5327,
  },
  parameters: {
    docs: {
      description: {
        story: '많은 수의 여행을 가진 태그 카드입니다.',
      },
    },
  },
};

export const LongLabel: Story = {
  render: args => ({
    components: { TagCard },
    setup() {
      const onClickHandler = fn();
      return { args, onClickHandler };
    },
    template: `
      <TagCard
        :label="args.label"
        :labelCount="args.labelCount"
        @click="onClickHandler"
      />
    `,
  }),
  args: {
    label: '아주아주긴태그이름입니다',
    labelCount: 375,
  },
  parameters: {
    docs: {
      description: {
        story: '긴 태그 이름을 가진 카드입니다.',
      },
    },
  },
};

export const ShortLabel: Story = {
  render: args => ({
    components: { TagCard },
    setup() {
      const onClickHandler = fn();
      return { args, onClickHandler };
    },
    template: `
      <TagCard
        :label="args.label"
        :labelCount="args.labelCount"
        @click="onClickHandler"
      />
    `,
  }),
  args: {
    label: '산',
    labelCount: 823,
  },
  parameters: {
    docs: {
      description: {
        story: '짧은 태그 이름을 가진 카드입니다.',
      },
    },
  },
};

export const Rounded: Story = {
  render: args => ({
    components: { TagCard },
    setup() {
      const onClickHandler = fn();
      return { args, onClickHandler };
    },
    template: `
      <div class="rounded-full overflow-hidden">
        <TagCard
          :label="args.label"
          :labelCount="args.labelCount"
          @click="onClickHandler"
        />
      </div>
    `,
  }),
  args: {
    label: '힐링',
    labelCount: 1642,
  },
  parameters: {
    docs: {
      description: {
        story: '완전히 둥근 형태로 사용하는 예시입니다.',
      },
    },
  },
};

export const Grid: Story = {
  render: () => ({
    components: { TagCard },
    setup() {
      const tags = [
        { label: '도시여행', count: 1221 },
        { label: '자연', count: 842 },
        { label: '로컬체험', count: 567 },
        { label: '힐링', count: 1345 },
        { label: '미식', count: 982 },
        { label: '역사탐방', count: 432 },
      ];

      const handleTagClick = fn();

      return { tags, handleTagClick };
    },
    template: `
      <div class="grid grid-cols-2 gap-4">
        <TagCard 
          v-for="tag in tags" 
          :key="tag.label"
          :label="tag.label"
          :labelCount="tag.count"
          @click="() => handleTagClick(tag)"
        />
      </div>
    `,
  }),
  parameters: {
    docs: {
      description: {
        story: '그리드 레이아웃에서 여러 태그 카드를 표시하는 예시입니다.',
      },
    },
  },
};
