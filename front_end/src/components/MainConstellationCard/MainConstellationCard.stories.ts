import type { Meta, StoryObj } from '@storybook/vue3';
import { fn } from '@storybook/test';
import MainConstellationCard from './MainConstellationCard.vue';

interface Props {
  constellationName: string;
  isBackVisible?: boolean;
}

interface TarotCardProps extends Props {
  onClick: () => void;
}

const meta: Meta<TarotCardProps> = {
  title: 'Components/MainConstellationCard',
  component: MainConstellationCard,
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
        <div style="min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 2rem;">
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
    constellationName: 'hunter',
    isBackVisible: false,
  },
  render: args => ({
    components: { MainConstellationCard },
    setup() {
      return { args };
    },
    template: '<MainConstellationCard v-bind="args" @click="args.onClick" />',
  }),
};
