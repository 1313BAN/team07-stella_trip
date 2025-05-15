import type { Meta, StoryObj } from '@storybook/vue3';
import Tag from './Tag.vue';

interface Props {
  label: string;
  isOverflow?: boolean;
}

const meta: Meta<Props> = {
  title: 'Components/common/Tag',
  component: Tag,
  tags: ['autodocs'],
  parameters: {
    backgrounds: {
      default: 'dark',
    },
  },
  decorators: [
    () => ({
      template: `
        <div style="padding: 20px;">
          <story />
        </div>
      `,
    }),
  ],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    label: '자연',
    isOverflow: false,
  },
  render: args => ({
    components: { Tag },
    setup() {
      return { args };
    },
    template: '<Tag v-bind="args" />',
  }),
};

export const Overflow: Story = {
  args: {
    label: '+3',
    isOverflow: true,
  },
  render: args => ({
    components: { Tag },
    setup() {
      return { args };
    },
    template: '<Tag v-bind="args" />',
  }),
};

export const LongLabel: Story = {
  args: {
    label: '문화유산',
    isOverflow: false,
  },
  render: args => ({
    components: { Tag },
    setup() {
      return { args };
    },
    template: '<Tag v-bind="args" />',
  }),
};

export const MultipleTags: Story = {
  args: {
    label: '자연',
  },
  render: () => ({
    components: { Tag },
    template: `
      <div class="flex flex-wrap gap-2">
        <Tag label="자연" :isOverflow="false" />
        <Tag label="문화" :isOverflow="false" />
        <Tag label="현대" :isOverflow="false" />
        <Tag label="해변" :isOverflow="false" />
        <Tag label="+2" :isOverflow="true" />
      </div>
    `,
  }),
};

export const VaryingLengths: Story = {
  args: {
    label: '자연',
  },
  render: () => ({
    components: { Tag },
    template: `
      <div class="flex flex-wrap gap-2">
        <Tag label="산" :isOverflow="false" />
        <Tag label="문화유산" :isOverflow="false" />
        <Tag label="자연 경관" :isOverflow="false" />
        <Tag label="역사 문화 명소" :isOverflow="false" />
        <Tag label="+5" :isOverflow="true" />
      </div>
    `,
  }),
};
