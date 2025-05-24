<script setup lang="ts">
import type { SliderRootEmits, SliderRootProps } from 'reka-ui';
import type { HTMLAttributes } from 'vue';
import { reactiveOmit } from '@vueuse/core';
import { SliderRange, SliderRoot, SliderThumb, SliderTrack, useForwardPropsEmits } from 'reka-ui';
import { cn } from '@/lib/shadcn/utils';
import { computed, ref, watch } from 'vue';

const props = withDefaults(
  defineProps<
    SliderRootProps & {
      class?: HTMLAttributes['class'];
      /**
       * Default initial values for the dual slider
       */
      defaultValues?: [number, number];
    }
  >(),
  {
    defaultValues: () => [25, 75],
    step: 1,
    min: 0,
    max: 100,
  }
);

const emits = defineEmits<SliderRootEmits>();

// Set default model value if not provided
const values = ref<number[]>(
  props.modelValue && props.modelValue.length === 2
    ? [...props.modelValue]
    : [...props.defaultValues]
);

// Ensure we always have exactly 2 thumbs for the dual slider
const normalizedValues = computed(() => {
  if (!values.value || values.value.length !== 2) {
    return [props.min || 0, props.max || 100];
  }
  return values.value;
});

// Watch for external modelValue changes
watch(
  () => props.modelValue,
  newValue => {
    if (newValue && newValue.length === 2) {
      values.value = newValue;
    }
  },
  { deep: true }
);

// Watch for internal value changes and emit update
watch(
  values,
  newValues => {
    emits('update:modelValue', newValues);
  },
  { deep: true }
);

// Omit props that we handle specially
const delegatedProps = reactiveOmit(props, ['class', 'defaultValues', 'modelValue']);

// Forward the remaining props but don't override modelValue
const forwardedProps = useForwardPropsEmits(delegatedProps, emits);
</script>

<template>
  <SliderRoot
    v-slot="slotProps"
    data-slot="dual-slider"
    :class="
      cn(
        'relative flex w-full touch-none items-center select-none',
        'data-[disabled]:opacity-50',
        'data-[orientation=vertical]:h-full data-[orientation=vertical]:min-h-44 data-[orientation=vertical]:w-auto data-[orientation=vertical]:flex-col',
        props.class
      )
    "
    v-bind="forwardedProps"
    :modelValue="normalizedValues"
    @update:modelValue="values = $event || normalizedValues"
    @valueCommit="emits('valueCommit', $event)"
  >
    <SliderTrack
      data-slot="dual-slider-track"
      :class="[
        'relative h-2 w-full grow overflow-hidden rounded-full',
        props.class && props.class.includes('slider-purple') ? 'bg-purple-200/20' : 'bg-secondary',
      ]"
    >
      <SliderRange
        data-slot="dual-slider-range"
        :class="[
          'absolute h-full',
          props.class && props.class.includes('slider-purple') ? 'bg-purple-600' : 'bg-primary',
        ]"
        :style="{
          left:
            normalizedValues[0] !== undefined
              ? `${((normalizedValues[0] - (props.min || 0)) / ((props.max || 100) - (props.min || 0))) * 100}%`
              : '0%',
          width:
            normalizedValues[0] !== undefined && normalizedValues[1] !== undefined
              ? `${((normalizedValues[1] - normalizedValues[0]) / ((props.max || 100) - (props.min || 0))) * 100}%`
              : '0%',
        }"
      />
    </SliderTrack>
    <SliderThumb
      v-for="(_, index) in normalizedValues"
      :key="index"
      data-slot="dual-slider-thumb"
      :class="[
        'bg-background ring-offset-background focus-visible:ring-ring block h-5 w-5 rounded-full border-2 transition-colors focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:outline-none disabled:pointer-events-none disabled:opacity-50',
        props.class && props.class.includes('slider-purple')
          ? 'border-purple-600'
          : 'border-primary',
      ]"
    />
  </SliderRoot>
</template>
