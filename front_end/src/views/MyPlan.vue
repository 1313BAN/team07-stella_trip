<template>
  <section class="relative">
    <AsyncContainer :loadingComponent="FilteredPlansSkeleton" :errorComponent="FilteredPlansError">
      <MyPlans
        :parentScrollContainer="scrollContainerRef"
        :apiParams="{ page: 1, size: 100 }"
        @addPlanClick="handleAddPlanCardClick"
        @cardClick="handlePlanCardClick"
        @likeClick="handlePlanLikeClick"
        @tagClick="handlePlanTagClick"
      />
    </AsyncContainer>

    <!-- 여행 계획 추가 다이얼로그 -->
    <Dialog :open="isAddDialogOpen" @update:open="isAddDialogOpen = $event">
      <DialogContent class="w-full max-w-lg border-0 bg-transparent p-0 shadow-none">
        <div
          class="relative overflow-hidden rounded-2xl border border-white/20 bg-white/10 backdrop-blur-md"
        >
          <!-- 그라데이션 오버레이 -->
          <div
            class="absolute inset-0 bg-gradient-to-br from-slate-900/80 via-purple-950/80 to-indigo-950/80"
          />

          <!-- 콘텐츠 -->
          <div class="relative p-6">
            <DialogHeader class="mb-6">
              <DialogTitle class="text-2xl font-bold text-white">✨ 새로운 여행 계획</DialogTitle>
            </DialogHeader>

            <Form class="space-y-6" @submit="onSubmit">
              <!-- 제목 입력 -->
              <FormField v-slot="{ componentField }" name="title">
                <FormItem>
                  <FormLabel class="text-sm font-medium text-purple-200">제목</FormLabel>
                  <FormControl>
                    <Input
                      v-bind="componentField"
                      v-model="form.title"
                      placeholder="어떤 여행을 계획하고 계신가요?"
                      required
                      class="border-white/20 bg-white/10 text-white backdrop-blur-sm transition-all duration-200 focus:border-purple-400/50 focus:bg-white/15 focus:ring-purple-400/30 [&::placeholder]:text-gray-400"
                    />
                  </FormControl>
                  <FormMessage class="text-red-300" />
                </FormItem>
              </FormField>

              <!-- 설명 입력 -->
              <FormField v-slot="{ componentField }" name="description">
                <FormItem>
                  <FormLabel class="text-sm font-medium text-purple-200">설명</FormLabel>
                  <FormControl>
                    <Textarea
                      v-bind="componentField"
                      v-model="form.description"
                      placeholder="여행에 대한 간단한 설명을 적어주세요"
                      required
                      rows="3"
                      class="resize-none border-white/20 bg-white/10 text-white backdrop-blur-sm transition-all duration-200 focus:border-purple-400/50 focus:bg-white/15 focus:ring-purple-400/30 [&::placeholder]:text-gray-400"
                    />
                  </FormControl>
                  <FormMessage class="text-red-300" />
                </FormItem>
              </FormField>

              <!-- 태그 입력 -->
              <FormField v-slot="{ componentField }" name="tags">
                <FormItem>
                  <FormLabel class="text-sm font-medium text-purple-200">태그</FormLabel>
                  <FormControl>
                    <Input
                      v-bind="componentField"
                      v-model="tagInput"
                      placeholder="태그를 쉼표로 구분해서 입력해주세요 (예: 힐링, 자연, 맛집)"
                      class="border-white/20 bg-white/10 text-white backdrop-blur-sm transition-all duration-200 focus:border-purple-400/50 focus:bg-white/15 focus:ring-purple-400/30 [&::placeholder]:text-gray-400"
                    />
                  </FormControl>
                  <FormDescription class="text-xs text-purple-200/70">
                    쉼표로 구분하여 여러 태그를 입력할 수 있습니다
                  </FormDescription>
                  <FormMessage class="text-red-300" />
                </FormItem>
              </FormField>

              <!-- 날짜 범위 선택 -->
              <FormField name="dateRange">
                <FormItem class="flex flex-col">
                  <FormLabel class="text-sm font-medium text-purple-200">여행 기간</FormLabel>
                  <Popover>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          :class="[
                            'w-full justify-start border-white/20 bg-white/10 text-left font-normal text-white backdrop-blur-sm transition-all duration-200 hover:border-purple-400/50 hover:bg-white/15 focus:border-purple-400/50 focus:ring-purple-400/30',
                            (!form.startDate || !form.endDate) && 'text-gray-400',
                          ]"
                        >
                          <CalendarIcon class="mr-2 h-4 w-4 text-purple-400" />
                          <span v-if="form.startDate && form.endDate">
                            {{ formatDateValue(dateRange.start) }} ~
                            {{ formatDateValue(dateRange.end) }}
                          </span>
                          <span v-else-if="form.startDate">
                            {{ formatDateValue(dateRange.start) }} ~ 종료일 선택
                          </span>
                          <span v-else>여행 날짜를 선택해주세요</span>
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent class="w-auto border-white/20 bg-white/10 p-0 backdrop-blur-md">
                      <RangeCalendar
                        v-model="dateRange"
                        class="rounded-md border-0 bg-transparent text-white [&_[role=gridcell]]:text-white [&_[role=gridcell]:hover]:bg-white/20 [&_[role=gridcell][data-range-middle]]:bg-purple-300/50 [&_[role=gridcell][data-selected]]:bg-purple-500 [&_[role=gridcell][data-selected]]:text-white [&_button]:text-white [&_button:hover]:bg-white/20"
                        :numberOfMonths="2"
                        @update:modelValue="handleDateRangeChange"
                      />
                    </PopoverContent>
                  </Popover>
                  <FormDescription class="text-xs text-purple-200/70">
                    시작일과 종료일을 선택해주세요
                  </FormDescription>
                  <FormMessage class="text-red-300" />
                </FormItem>
              </FormField>

              <!-- 공개 설정 -->
              <FormField name="isPublic">
                <FormItem class="flex flex-row items-start space-y-0 space-x-3">
                  <FormControl>
                    <Checkbox
                      :checked="form.isPublic"
                      class="border-white/20 bg-white/10 text-purple-400 focus:ring-purple-400/30 data-[state=checked]:border-purple-500 data-[state=checked]:bg-purple-500"
                      @update:checked="form.isPublic = $event"
                    />
                  </FormControl>
                  <div class="space-y-1 leading-none">
                    <FormLabel class="cursor-pointer text-sm font-medium text-purple-200">
                      다른 사람들과 공유하기
                    </FormLabel>
                    <FormDescription class="text-xs text-purple-200/70">
                      공개로 설정하면 다른 사용자들도 회원님의 여행 계획을 볼 수 있습니다
                    </FormDescription>
                  </div>
                </FormItem>
              </FormField>

              <!-- 버튼들 -->
              <div class="flex justify-end gap-3 pt-4">
                <Button
                  type="button"
                  variant="ghost"
                  class="border border-white/20 bg-white/10 text-white backdrop-blur-sm hover:border-white/30 hover:bg-white/20 focus:ring-white/30"
                  @click="isAddDialogOpen = false"
                >
                  취소
                </Button>
                <Button
                  type="submit"
                  :disabled="isSubmitting"
                  class="bg-gradient-to-r from-purple-500 to-indigo-500 font-medium text-white shadow-lg shadow-purple-500/25 hover:from-purple-600 hover:to-indigo-600 focus:ring-purple-400/50 disabled:opacity-50"
                >
                  <LoaderCircle v-if="isSubmitting" class="mr-2 h-4 w-4 animate-spin" />
                  <span v-if="isSubmitting">추가 중...</span>
                  <span v-else>✨ 계획 추가</span>
                </Button>
              </div>
            </Form>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  </section>
</template>

<script setup lang="ts">
import { ref, type Ref } from 'vue';
import { useRouter } from 'vue-router';
import { toast } from 'vue-sonner';
import { CalendarIcon, LoaderCircle } from 'lucide-vue-next';
import { type DateValue, getLocalTimeZone, today } from '@internationalized/date';
import AsyncContainer from '@/components/AsyncContainer/AsyncContainer.vue';
import { FilteredPlansSkeleton, FilteredPlansError } from '@/components/views/plan/FilteredPlans';
import { type Plan, type Tag, createPlan } from '@/services/api/domains/plan';
import { ROUTES } from '@/router/routes';
import MyPlans from '@/components/views/myPlan/myPlans/myPlans.vue';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Checkbox } from '@/components/ui/checkbox';
import { RangeCalendar } from '@/components/ui/range-calendar';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import type { DateRange } from 'reka-ui';

const router = useRouter();
const scrollContainerRef = ref<HTMLElement | null>(null);
const isAddDialogOpen = ref(false);
const isSubmitting = ref(false);
const start = today(getLocalTimeZone());
const end = start.add({ days: 7 });

// 날짜 포맷팅 함수
const formatDateValue = (dateValue: DateValue | undefined) => {
  if (!dateValue) return '';
  return `${dateValue.month}월 ${dateValue.day}일`;
};

const dateRange = ref({
  start,
  end,
}) as Ref<DateRange>;

const dateValueToString = (dateValue: DateValue | undefined): string => {
  if (!dateValue) return '';
  return `${dateValue.year}-${String(dateValue.month).padStart(2, '0')}-${String(dateValue.day).padStart(2, '0')}`;
};

// 날짜 범위 변경 핸들러
const handleDateRangeChange = (range: {
  start: DateValue | undefined;
  end: DateValue | undefined;
}) => {
  dateRange.value = range;

  form.value.startDate = dateValueToString(range.start);
  form.value.endDate = dateValueToString(range.end);
};

// 여행 계획 다이얼로그 상태
const form = ref({
  title: '',
  description: '',
  tags: [] as string[],
  isPublic: true,
  startDate: '',
  endDate: '',
});
const tagInput = ref('');

// 핸들러들
const handlePlanCardClick = (plan: Plan) => {
  router.push({
    name: ROUTES.PLAN_DETAIL.name,
    params: { planId: plan.planId.toString() },
  });
};

const handleAddPlanCardClick = () => {
  isAddDialogOpen.value = true;
};

const handlePlanLikeClick = (plan: Plan) => {
  console.log('여행 계획 좋아요 클릭:', plan.title);
};

const handlePlanTagClick = (tag: Tag) => {
  console.log('여행 계획 태그 클릭:', tag.name);
};

const onSubmit = async () => {
  if (isSubmitting.value) return;

  try {
    isSubmitting.value = true;
    form.value.tags = tagInput.value
      .split(',')
      .map(t => t.trim())
      .filter(t => t.length > 0);

    const plan = await createPlan(form.value);

    toast.success('여행 계획이 성공적으로 추가되었습니다!', {
      description: `${form.value.title} 계획이 생성되었습니다.`,
      duration: 4000,
    });

    router.push({
      name: ROUTES.PLAN_MODIFY.name,
      params: { planId: plan.planId },
      state: { plan },
    });
  } catch (err) {
    console.error(err);

    toast.error('계획 추가에 실패했습니다', {
      description: '잠시 후 다시 시도해주세요.',
      duration: 4000,
    });
  } finally {
    isSubmitting.value = false;
  }
};
</script>
