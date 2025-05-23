import type { Meta, StoryObj } from '@storybook/vue3';
import AttractionItem from './AttractionItem.vue';

const meta: Meta<typeof AttractionItem> = {
  title: 'Components/AttractionItem',
  component: AttractionItem,
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

export const Default: Story = {
  args: {
    order: 1,
    name: '경복궁',
    visitTime: '09:30',
    address: '서울 종로구 사직로 161',
    memo: '조선시대 왕궁, 한복 체험 가능',
  },
};
