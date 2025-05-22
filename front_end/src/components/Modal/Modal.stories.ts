import type { Meta, StoryObj } from '@storybook/vue3';
import { ref } from 'vue';
import Modal from './Modal.vue';

// Import Button directly from its source component
import Button from '@/components/ui/button/Button.vue';

interface ModalProps {
  modelValue: boolean;
  title?: string;
  description?: string;
  maxWidth?: string;
  closeOnOutsideClick?: boolean;
  showCloseButton?: boolean;
  hideOverlay?: boolean;
  overlayOpacity?: string;
}

const meta: Meta<ModalProps> = {
  title: 'Components/Modal',
  component: Modal,
  tags: ['autodocs'],
  argTypes: {
    modelValue: {
      description: '모달의 표시 여부',
      control: { type: 'boolean' },
    },
    title: {
      description: '모달의 제목',
      control: { type: 'text' },
    },
    description: {
      description: '모달의 설명',
      control: { type: 'text' },
    },
    maxWidth: {
      description: '모달의 최대 너비',
      control: { type: 'text' },
    },
    closeOnOutsideClick: {
      description: '외부 클릭 시 모달 닫기 여부',
      control: { type: 'boolean' },
    },
    showCloseButton: {
      description: '닫기 버튼 표시 여부',
      control: { type: 'boolean' },
    },
    hideOverlay: {
      description: '오버레이 숨김 여부',
      control: { type: 'boolean' },
    },
    overlayOpacity: {
      description: '오버레이 투명도',
      control: { type: 'text' },
    },
  },
  args: {
    modelValue: true, // docs 모드에서 기본적으로 모달 표시
    title: 'Docs 모드 모달',
    description: '문서 모드에서 볼 수 있는 모달입니다.',
    maxWidth: '28rem',
    closeOnOutsideClick: true,
    showCloseButton: true,
    hideOverlay: false,
    overlayOpacity: '0.6',
  },
  parameters: {
    backgrounds: {
      default: 'dark',
      values: [
        {
          name: 'dark',
          value: 'linear-gradient(135deg, #1a0033 0%, #3a0080 50%, #000033 100%)',
        },
      ],
    },
    docs: {
      description: {
        component:
          '별여행 서비스의 모달 컴포넌트입니다. 사용자에게 중요한 정보를 보여주거나 입력을 받을 때 사용합니다.',
      },
    },
    actions: {
      handles: ['update:modelValue', 'close'],
    },
  },
};

export default meta;
type Story = StoryObj<typeof meta>;

// 기본 모달 스토리 - 간소화된 버전
export const 기본모달: Story = {
  render: () => ({
    components: { Modal, Button },
    setup() {
      const isOpen = ref(false);

      function openModal() {
        isOpen.value = true;
      }

      return { isOpen, openModal };
    },
    template: `
      <div class="p-8 flex justify-center">
        <Button @click="openModal" class="bg-indigo-600 hover:bg-indigo-700">모달 열기</Button>
        <Modal v-model="isOpen" title="기본 모달">
          <div class="space-y-4">
            <p>모달 내용을 이곳에 넣어주세요.</p>
          </div>
        </Modal>
      </div>
    `,
  }),
};

// 제목과 설명이 있는 모달 - 간소화된 버전
export const 제목과설명이있는모달: Story = {
  render: () => ({
    components: { Modal, Button },
    setup() {
      const isOpen = ref(false);

      function openModal() {
        isOpen.value = true;
      }

      return { isOpen, openModal };
    },
    template: `
      <div class="p-8 flex justify-center">
        <Button @click="openModal" class="bg-purple-600 hover:bg-purple-700">모달 열기</Button>
        <Modal 
          v-model="isOpen"
          title="별여행 안내"
          description="별자리를 테마로 한 여행 서비스입니다."
        >
          <div class="space-y-4">
            <p>모달 내용을 이곳에 넣어주세요.</p>
          </div>
        </Modal>
      </div>
    `,
  }),
};

// 푸터가 있는 모달 - 간소화된 버전
export const 푸터가있는모달: Story = {
  render: () => ({
    components: { Modal, Button },
    setup() {
      const isOpen = ref(false);

      function openModal() {
        isOpen.value = true;
      }

      function closeModal() {
        isOpen.value = false;
      }

      return { isOpen, openModal, closeModal };
    },
    template: `
      <div class="p-8 flex justify-center">
        <Button @click="openModal" class="bg-indigo-700 hover:bg-indigo-800">모달 열기</Button>
        <Modal v-model="isOpen" title="별여행 안내">
          <div class="space-y-4">
            <p>여행 계획을 저장하시겠습니까?</p>
          </div>
          <template #footer>
            <Button @click="closeModal" class="bg-indigo-600 hover:bg-indigo-700">취소</Button>
            <Button @click="closeModal" class="bg-indigo-600 hover:bg-indigo-700">저장</Button>
          </template>
        </Modal>
      </div>
    `,
  }),
};

// 별 배경이 있는 모달 - PlanCard 스타일과 일치
export const 별배경모달: Story = {
  render: () => ({
    components: { Modal, Button },
    setup() {
      const isOpen = ref(false);

      function openModal() {
        isOpen.value = true;
      }

      return { isOpen, openModal };
    },
    template: `
      <div class="p-8 flex justify-center">
        <Button @click="openModal">별 배경 모달 열기</Button>
        <Modal 
          v-model="isOpen"
          title="별자리 여행 루트"
          description="우주를 담은 모달창입니다. PlanCard와 동일한 스타일의 별 배경을 가지고 있습니다."
          maxWidth="32rem"
        >
          <div class="space-y-4 relative z-10">
            <p>이 모달은 별자리 여행 정보를 표시하기 위한 특별한 디자인을 가지고 있습니다.</p>
            <p>별들이 반짝이는 배경으로 우주 여행의 분위기를 연출합니다.</p>
            <p>PlanCard 컴포넌트와 일관된 디자인 언어를 사용하여 사용자 경험을 향상시킵니다.</p>
          </div>
        </Modal>
      </div>
    `,
  }),
};

// 푸터가 있는 별 배경 모달
export const 푸터가있는별배경모달: Story = {
  render: () => ({
    components: { Modal, Button },
    setup() {
      const isOpen = ref(false);

      function openModal() {
        isOpen.value = true;
      }

      function closeModal() {
        isOpen.value = false;
      }

      function saveAction() {
        // 저장 동작 시뮬레이션
        setTimeout(() => {
          alert('여행 계획이 저장되었습니다!');
          closeModal();
        }, 500);
      }

      return { isOpen, openModal, closeModal, saveAction };
    },
    template: `
      <div class="p-8 flex justify-center">
        <Button @click="openModal">별 배경 모달 + 버튼 열기</Button>
        <Modal 
          v-model="isOpen"
          title="여행 계획 저장"
          description="별자리 여행 계획을 저장하시겠습니까?"
          maxWidth="30rem"
        >
          <div class="space-y-4 relative z-10">
            <p>작성하신 별자리 여행 루트를 저장하면:</p>
            <ul class="list-disc pl-5 space-y-1">
              <li>마이페이지에서 언제든지 확인할 수 있습니다</li>
              <li>다른 여행자들과 공유할 수 있습니다</li>
              <li>나중에 수정 및 업데이트가 가능합니다</li>
            </ul>
          </div>
          <template #footer>
            <Button @click="saveAction" variant="default">저장하기</Button>
            <Button @click="closeModal" variant="outline" class="text-white">취소</Button>
          </template>
        </Modal>
      </div>
    `,
  }),
};

// 큰 크기의 별 배경 모달
export const 큰크기별배경모달: Story = {
  render: () => ({
    components: { Modal, Button },
    setup() {
      const isOpen = ref(false);

      function openModal() {
        isOpen.value = true;
      }

      return { isOpen, openModal };
    },
    template: `
      <div class="p-8 flex justify-center">
        <Button @click="openModal" class="bg-violet-700 hover:bg-violet-800">큰 모달 열기</Button>
        <Modal 
          v-model="isOpen"
          title="별자리 여행 계획 상세보기"
          description="여행 별자리와 경로 정보를 확인해보세요"
          maxWidth="48rem"
        >
          <div class="bg-indigo-800 relative z-10">
            <h3 class="text-lg font-medium text-white">북두칠성 별자리 여행</h3>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="bg-white/10 p-4 rounded-lg backdrop-blur-sm">
                <h4 class="font-medium mb-2 text-purple-300">기본 정보</h4>
                <p>기간: 2023-06-10 ~ 2023-06-17</p>
                <p>여행 테마: 도시 탐험, 자연 체험</p>
                <p>추천 계절: 여름, 가을</p>
              </div>
              <div class="bg-white/10 p-4 rounded-lg backdrop-blur-sm">
                <h4 class="font-medium mb-2 text-purple-300">경비 정보</h4>
                <p>예상 비용: ₩1,200,000</p>
                <p>경비 수준: 중간</p>
                <p>추천 예산: ₩1,500,000</p>
              </div>
              <div class="bg-white/10 p-4 rounded-lg backdrop-blur-sm col-span-2">
                <h4 class="font-medium mb-2 text-purple-300">여행 코스</h4>
                <p>서울 → 부산 → 제주 → 서울</p>
                <p class="mt-2">본 여행은 서울을 시작으로 부산, 제주를 거쳐 다시 서울로 돌아오는 코스입니다. 각 지역의 특색 있는 명소와 자연경관을 즐길 수 있습니다.</p>
              </div>
            </div>
          </div>
        </Modal>
      </div>
    `,
  }),
};

// 인터랙티브 별 배경 모달
export const 인터랙티브별배경모달: Story = {
  render: () => ({
    components: { Modal, Button },
    setup() {
      const isOpen = ref(false);
      const currentStep = ref(1);
      const totalSteps = 3;

      // 별 배경 강도 상태
      const starIntensity = ref(0.5);

      function openModal() {
        isOpen.value = true;
        currentStep.value = 1;
        starIntensity.value = 0.5;
      }

      function nextStep() {
        if (currentStep.value < totalSteps) {
          currentStep.value++;
          // 단계가 진행될수록 별이 더 밝아짐
          starIntensity.value += 0.2;
        } else {
          // 마지막 단계에서는 모달 닫기
          isOpen.value = false;
        }
      }

      function prevStep() {
        if (currentStep.value > 1) {
          currentStep.value--;
          // 단계가 뒤로 갈수록 별이 덜 밝아짐
          starIntensity.value -= 0.2;
        }
      }

      return {
        isOpen,
        openModal,
        currentStep,
        totalSteps,
        nextStep,
        prevStep,
        starIntensity,
      };
    },
    template: `
      <div class="p-8 flex justify-center">
        <Button @click="openModal" class="bg-purple-600 hover:bg-purple-700">단계별 모달 열기</Button>
        <Modal 
          v-model="isOpen"
          :title="'여행 계획 마법사 - ' + currentStep + '/' + totalSteps + ' 단계'"
          description="단계별로 여행 계획을 작성해보세요."
          maxWidth="36rem"
        >
          <div 
            class="space-y-6 relative z-10 transition-all duration-500"
            :style="{ opacity: starIntensity }"
          >
            <!-- 단계 1: 여행 기본 정보 -->
            <div v-if="currentStep === 1" class="space-y-4">
              <h3 class="text-lg font-semibold text-purple-200">여행 기본 정보</h3>
              <div class="grid gap-3">
                <div class="bg-white/10 p-3 rounded-lg">
                  <label class="block text-sm mb-1 text-gray-200">여행 제목</label>
                  <input 
                    type="text" 
                    placeholder="예: 북두칠성 별자리 여행" 
                    class="w-full bg-black/30 text-white border border-purple-500/30 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-purple-400"
                  />
                </div>
                <div class="grid grid-cols-2 gap-3">
                  <div class="bg-white/10 p-3 rounded-lg">
                    <label class="block text-sm mb-1 text-gray-200">시작일</label>
                    <input 
                      type="date" 
                      class="w-full bg-black/30 text-white border border-purple-500/30 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-purple-400"
                    />
                  </div>
                  <div class="bg-white/10 p-3 rounded-lg">
                    <label class="block text-sm mb-1 text-gray-200">종료일</label>
                    <input 
                      type="date" 
                      class="w-full bg-black/30 text-white border border-purple-500/30 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-purple-400"
                    />
                  </div>
                </div>
              </div>
            </div>

            <!-- 단계 2: 여행 지역 선택 -->
            <div v-if="currentStep === 2" class="space-y-4">
              <h3 class="text-lg font-semibold text-purple-200">여행 지역 선택</h3>
              <div class="grid grid-cols-2 gap-3">
                <div 
                  v-for="region in ['서울', '부산', '제주', '경주', '강원도', '전주']" 
                  :key="region"
                  class="bg-white/10 hover:bg-white/20 cursor-pointer transition-all p-3 rounded-lg flex items-center gap-2"
                >
                  <input type="checkbox" class="h-4 w-4 accent-purple-400" />
                  <span>{{ region }}</span>
                </div>
              </div>
              <div class="bg-indigo-900/30 p-3 rounded-lg mt-3">
                <p class="text-sm text-gray-300">* 여행하고 싶은 지역을 모두 선택해주세요. 나중에 변경할 수 있습니다.</p>
              </div>
            </div>

            <!-- 단계 3: 여행 테마 선택 -->
            <div v-if="currentStep === 3" class="space-y-4">
              <h3 class="text-lg font-semibold text-purple-200">여행 테마 선택</h3>
              <div class="flex flex-wrap gap-2">
                <div 
                  v-for="theme in ['문화 체험', '자연 경관', '맛집 탐방', '쇼핑', '휴양', '축제', '역사 탐방', '액티비티']" 
                  :key="theme"
                  class="bg-white/10 hover:bg-white/20 cursor-pointer transition-all px-3 py-1.5 rounded-full flex items-center gap-1.5"
                >
                  <input type="checkbox" class="h-3 w-3 accent-purple-400" />
                  <span class="text-sm">{{ theme }}</span>
                </div>
              </div>
              <div class="bg-purple-900/30 p-4 rounded-lg mt-3 flex items-center gap-3">
                <div class="bg-purple-400/20 p-1.5 rounded-full">
                  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-purple-300">
                    <path d="m12 3-1.912 5.813a2 2 0 0 1-1.275 1.275L3 12l5.813 1.912a2 2 0 0 1 1.275 1.275L12 21l1.912-5.813a2 2 0 0 1 1.275-1.275L21 12l-5.813-1.912a2 2 0 0 1-1.275-1.275L12 3Z"/>
                    <path d="M5 3v4"/>
                    <path d="M19 17v4"/>
                    <path d="M3 5h4"/>
                    <path d="M17 19h4"/>
                  </svg>
                </div>
                <p class="text-sm text-gray-200">테마를 선택하면 별자리 여행 루트에 맞는 추천 장소가 표시됩니다.</p>
              </div>
            </div>
          </div>

          <template #footer>
            <div class="flex w-full justify-between">
              <Button 
                v-if="currentStep > 1" 
                @click="prevStep" 
                class="bg-transparent border border-purple-500/50 text-purple-200 hover:bg-purple-900/20"
              >
                이전
              </Button>
              <div v-else></div>
              <Button 
                @click="nextStep"
                class="bg-purple-600 hover:bg-purple-700"
              >
                {{ currentStep === totalSteps ? '완료' : '다음' }}
              </Button>
            </div>
          </template>
        </Modal>
      </div>
    `,
  }),
};

// 완성된 여행 계획 미리보기 모달
// Docs 모드를 위한 자동 열림 모달
export const Docs모드용모달: Story = {
  render: () => ({
    components: { Modal, Button },
    setup() {
      const isOpen = ref(true); // 기본적으로 열려있음

      function closeModal() {
        isOpen.value = false;
      }

      function openModal() {
        isOpen.value = true;
      }

      return { isOpen, openModal, closeModal };
    },
    template: `
      <div class="p-8 flex flex-col gap-4 items-center">
        <p class="text-white text-center">이 모달은 Docs 모드를 위해 기본적으로 열려있습니다.</p>
        <Button v-if="!isOpen" @click="openModal" class="bg-purple-600 hover:bg-purple-700">모달 다시 열기</Button>
        <Modal 
          v-model="isOpen"
          title="Docs 모드 모달"
          description="스토리북 문서 모드에서도 정상적으로 볼 수 있는 모달입니다."
          maxWidth="32rem"
        >
          <div class="space-y-4 relative z-10">
            <p>이 모달은 스토리북 문서 모드에서도 정상적으로 볼 수 있도록 설계되었습니다.</p>
            <p>닫기 버튼 또는 외부 클릭으로 모달을 닫을 수 있습니다.</p>
          </div>
          <template #footer>
            <Button @click="closeModal" class="bg-purple-600 hover:bg-purple-700">닫기</Button>
          </template>
        </Modal>
      </div>
    `,
  }),
};
