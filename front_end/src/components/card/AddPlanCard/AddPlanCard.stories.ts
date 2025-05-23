import AddPlanCard from './AddPlanCard.vue';

export default {
  title: 'Components/card/AddPlanCard',
  component: AddPlanCard,
  tags: ['autodocs'],
};

export const Default = {
  render: () => ({
    components: { AddPlanCard },
    setup() {
      const handleAddPlan = () => {
        alert('여행 계획 추가하기 클릭됨!');
      };
      return { handleAddPlan };
    },
    template: `<AddPlanCard @addPlan="handleAddPlan" />`,
  }),
};
