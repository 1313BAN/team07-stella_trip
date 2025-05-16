import type { Meta, StoryObj } from '@storybook/vue3';
import { fn } from '@storybook/test';
import ScrollIndicator from './ScrollIndicator.vue';

interface Props {
  label?: string;
}

interface ScrollIndicatorProps extends Props {
  onClick: () => void;
}

const meta: Meta<ScrollIndicatorProps> = {
  title: 'Components/common/ScrollIndicator',
  component: ScrollIndicator,
  tags: ['autodocs'],
  args: {
    onClick: fn(),
  },
  parameters: {
    backgrounds: {
      default: 'dark',
    },
  },
  decorators: [
    () => ({
      template: `
        <div style="height: 400px; position: relative;">
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
    label: '스크롤하여 더 보기',
  },
  render: args => ({
    components: { ScrollIndicator },
    setup() {
      return { args };
    },
    template: '<ScrollIndicator v-bind="args" />',
  }),
};

export const ShortLabel: Story = {
  args: {
    label: '더보기',
  },
  render: args => ({
    components: { ScrollIndicator },
    setup() {
      return { args };
    },
    template: '<ScrollIndicator v-bind="args" />',
  }),
};

export const NoLabel: Story = {
  args: {
    label: '',
  },
  render: args => ({
    components: { ScrollIndicator },
    setup() {
      return { args };
    },
    template: '<ScrollIndicator v-bind="args" />',
  }),
};
