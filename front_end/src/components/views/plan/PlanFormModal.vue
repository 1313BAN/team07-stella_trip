<template>
  <Dialog :open="isOpen" @update:open="$emit('update:open', $event)">
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
            <DialogTitle class="text-2xl font-bold text-white">
              {{ isEditMode ? '✏️ 여행 계획 수정' : '✨ 새로운 여행 계획' }}
            </DialogTitle>
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
                @click="$emit('update:open', false)"
              >
                취소
              </Button>
              <Button
                type="submit"
                :disabled="isSubmitting"
                class="bg-gradient-to-r from-purple-500 to-indigo-500 font-medium text-white shadow-lg shadow-purple-500/25 hover:from-purple-600 hover:to-indigo-600 focus:ring-purple-400/50 disabled:opacity-50"
              >
                <LoaderCircle v-if="isSubmitting" class="mr-2 h-4 w-4 animate-spin" />
                <span v-if="isSubmitting">{{ isEditMode ? '수정 중...' : '추가 중...' }}</span>
                <span v-else>{{ isEditMode ? '✏️ 계획 수정' : '✨ 계획 추가' }}</span>
              </Button>
            </div>
          </Form>
        </div>
      </div>
    </DialogContent>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, watch, type Ref } from 'vue';
import { CalendarIcon, LoaderCircle } from 'lucide-vue-next';
import { type DateValue, getLocalTimeZone, today, parseDate } from '@internationalized/date';
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
import type { PlanDetail, CreatePlanRequest } from '@/services/api/domains/plan';

const props = defineProps<{
  isOpen: boolean;
  isEditMode?: boolean;
  initialData?: PlanDetail | null;
  isSubmitting?: boolean;
}>();

const emit = defineEmits<{
  'update:open': [value: boolean];
  'submit': [data: CreatePlanRequest];
}>();

const start = today(getLocalTimeZone());
const end = start.add({ days: 7 });

/**
 * 날짜 값을 "월 일" 형식으로 포맷팅합니다.
 * @param dateValue - 포맷팅할 날짜 값
 * @returns 포맷된 날짜 문자열
 */
const formatDateValue = (dateValue: DateValue | undefined) => {
  if (!dateValue) return '';
  return `${dateValue.month}월 ${dateValue.day}일`;
};

/**
 * DateValue를 YYYY-MM-DD 문자열로 변환합니다.
 * @param dateValue - 변환할 날짜 값
 * @returns 날짜 문자열
 */
const dateValueToString = (dateValue: DateValue | undefined): string => {
  if (!dateValue) return '';
  return `${dateValue.year}-${String(dateValue.month).padStart(2, '0')}-${String(dateValue.day).padStart(2, '0')}`;
};

/**
 * YYYY-MM-DD 문자열을 DateValue로 변환합니다.
 * @param dateString - 변환할 날짜 문자열
 * @returns DateValue 또는 undefined
 */
const stringToDateValue = (dateString: string): DateValue | undefined => {
  if (!dateString) return undefined;
  try {
    return parseDate(dateString);
  } catch {
    return undefined;
  }
};

const dateRange = ref({
  start,
  end,
}) as Ref<DateRange>;

const form = ref<CreatePlanRequest>({
  title: '',
  description: '',
  tags: [],
  isPublic: true,
  startDate: '',
  endDate: '',
});

const tagInput = ref('');

/**
 * 날짜 범위 변경 핸들러
 * @param range - 선택된 날짜 범위
 */
const handleDateRangeChange = (range: {
  start: DateValue | undefined;
  end: DateValue | undefined;
}) => {
  dateRange.value = range;
  form.value.startDate = dateValueToString(range.start);
  form.value.endDate = dateValueToString(range.end);
};

/**
 * 폼 초기화
 */
const resetForm = () => {
  form.value = {
    title: '',
    description: '',
    tags: [],
    isPublic: true,
    startDate: '',
    endDate: '',
  };
  tagInput.value = '';
  dateRange.value = { start, end };
};

/**
 * 초기 데이터로 폼 설정
 * @param data - 초기 데이터
 */
const setFormData = (data: PlanDetail) => {
  form.value = {
    title: data.title || '',
    description: data.description || '',
    tags: data.tags?.map(tag => tag.name) || [],
    isPublic: data.isPublic ?? true,
    startDate: data.startDate || '',
    endDate: data.endDate || '',
  };

  tagInput.value = form.value.tags.join(', ');

  const startDateValue = stringToDateValue(data.startDate);
  const endDateValue = stringToDateValue(data.endDate);

  dateRange.value = {
    start: startDateValue || start,
    end: endDateValue || end,
  };
};

/**
 * 폼 제출 핸들러
 */
const onSubmit = async () => {
  const tags = tagInput.value
    .split(',')
    .map(t => t.trim())
    .filter(t => t.length > 0);

  const formData: CreatePlanRequest = {
    ...form.value,
    tags,
  };

  emit('submit', formData);
};

// props 변경 감지하여 폼 초기화/설정
watch(
  () => [props.isOpen, props.initialData],
  ([isOpen, initialData]) => {
    if (isOpen) {
      if (props.isEditMode && initialData) {
        setFormData(initialData as PlanDetail);
      } else {
        resetForm();
      }
    }
  },
  { immediate: true }
);
</script>
