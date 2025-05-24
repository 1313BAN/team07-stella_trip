<script setup lang="ts">
import { Button } from '@/components/ui/button';
import { Heart } from 'lucide-vue-next';

interface Props {
  isLiked: boolean;
  size?: 'sm' | 'md' | 'lg';
  transparent?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  size: 'md',
  transparent: false,
});

const emit = defineEmits<{
  click: [];
}>();

const handleClick = () => {
  emit('click');
};

const sizeConfig = {
  sm: {
    buttonClass: 'h-8 w-8',
    iconClass: 'h-3 w-3',
  },
  md: {
    buttonClass: 'h-10 w-10',
    iconClass: 'h-4 w-4',
  },
  lg: {
    buttonClass: 'h-12 w-12',
    iconClass: 'h-5 w-5',
  },
};

const config = sizeConfig[props.size];
</script>

<template>
  <Button
    variant="ghost"
    size="icon"
    :class="[
      config.buttonClass,
      'rounded-full border border-white/20 transition-all duration-200 hover:scale-110 hover:bg-transparent',
      props.transparent ? 'border-none bg-transparent' : 'bg-black/40 backdrop-blur-sm',
    ]"
    :ariaLabel="isLiked ? '좋아요 취소하기' : '좋아요 하기'"
    :ariaPressed="isLiked"
    @click.stop="handleClick"
  >
    <Heart
      :class="[
        config.iconClass,
        'transition-colors duration-200',
        isLiked ? 'fill-red-500 text-red-500' : 'text-white/90',
      ]"
    />
  </Button>
</template>
