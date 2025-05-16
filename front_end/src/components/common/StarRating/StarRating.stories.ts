import type { Meta, StoryObj } from '@storybook/vue3';
import StarRating from './StarRating.vue';

interface Props {
  rating: number;
}

const meta: Meta<Props> = {
  title: 'Components/common/StarRating',
  component: StarRating,
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
    rating: 4.5,
  },
  render: args => ({
    components: { StarRating },
    setup() {
      return { args };
    },
    template: '<StarRating v-bind="args" />',
  }),
};

export const FullStars: Story = {
  args: {
    rating: 5.0,
  },
  render: args => ({
    components: { StarRating },
    setup() {
      return { args };
    },
    template: '<StarRating v-bind="args" />',
  }),
};

export const LowRating: Story = {
  args: {
    rating: 2.0,
  },
  render: args => ({
    components: { StarRating },
    setup() {
      return { args };
    },
    template: '<StarRating v-bind="args" />',
  }),
};

export const WithHalfStar: Story = {
  args: {
    rating: 3.5,
  },
  render: args => ({
    components: { StarRating },
    setup() {
      return { args };
    },
    template: '<StarRating v-bind="args" />',
  }),
};

export const DecimalRating: Story = {
  args: {
    rating: 4.7,
  },
  render: args => ({
    components: { StarRating },
    setup() {
      return { args };
    },
    template: '<StarRating v-bind="args" />',
  }),
};

export const ZeroRating: Story = {
  args: {
    rating: 0.0,
  },
  render: args => ({
    components: { StarRating },
    setup() {
      return { args };
    },
    template: '<StarRating v-bind="args" />',
  }),
};

export const MultipleRatings: Story = {
  args: {
    rating: 4.2,
  },
  render: () => ({
    components: { StarRating },
    template: `
      <div class="flex flex-col gap-4">
        <StarRating :rating="5.0" />
        <StarRating :rating="4.5" />
        <StarRating :rating="3.7" />
        <StarRating :rating="2.3" />
        <StarRating :rating="1.0" />
      </div>
    `,
  }),
};
