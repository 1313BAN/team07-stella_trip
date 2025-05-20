import type { Meta, StoryObj } from '@storybook/vue3';
import TagCardSkeleton from './TagCardSkeleton.vue';

const meta: Meta<typeof TagCardSkeleton> = {
  title: 'Components/skeleton/TagCardSkeleton',
  component: TagCardSkeleton,
  tags: ['autodocs'],
  parameters: {
    backgrounds: {
      default: 'dark',
    },
  },
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};

export const Multiple: Story = {
  render: () => ({
    components: { TagCardSkeleton },
    template: `
      <div class="flex flex-wrap gap-4">
        <TagCardSkeleton v-for="i in 3" :key="i" />
      </div>
    `,
  }),
};
