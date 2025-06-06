import { ref, watch, type Ref } from 'vue';
import { cn } from '@/lib/shadcn/utils';

interface StaggeredAnimationOptions {
  trigger?: Ref<boolean>;
  fromClass?: string;
  toClass?: string;
  baseClass?: string;
  staggerDelay?: number;
  duration?: number;
  easing?: string;
}

interface StaggeredAnimationReturn {
  getItemClass: (useBaseClass?: boolean) => string;
  getItemStyle: (index: number) => Record<string, string>;
  startAnimation: () => void;
  resetAnimation: () => void;
  isAnimating: Ref<boolean>;
}

/**
 * 순차적으로 시차를 두고 애니메이션하는 훅
 * @param options 애니메이션 설정 옵션
 * @param options.trigger 애니메이션 트리거 ref
 * @param options.fromClass 초기 상태 클래스 (기본값: 'opacity-0 translate-y-4')
 * @param options.toClass 최종 상태 클래스 (기본값: 'opacity-100 translate-y-0')
 * @param options.baseClass 항상 유지할 공통 클래스
 * @param options.staggerDelay 각 요소 간 지연 시간 ms (기본값: 150)
 * @param options.duration 애니메이션 지속 시간 ms (기본값: 500)
 * @param options.easing 이징 함수 (기본값: 'ease-out')
 * @returns
 * - `getItemClass(useBaseClass)`: 특정 인덱스 요소의 클래스를 반환 (baseClass 선택 가능, 기본값 true)
 * - `getItemStyle(index)`: 특정 인덱스 요소의 스타일을 반환하는 함수
 * - `startAnimation()`: 애니메이션을 수동으로 시작하는 함수
 * - `resetAnimation()`: 애니메이션을 리셋하는 함수 (모든 요소를 초기 상태로)
 * - `isAnimating`: 현재 애니메이션이 진행 중인지 여부 (computed)
 */
export function useStaggeredAnimation(
  options: StaggeredAnimationOptions = {}
): StaggeredAnimationReturn {
  const {
    trigger,
    fromClass = 'opacity-0 translate-y-4',
    toClass = 'opacity-100 translate-y-0',
    baseClass = '',
    staggerDelay = 150,
    duration = 500,
    easing = 'ease-out',
  } = options;

  const isAnimating = ref(false);

  if (trigger) {
    watch(trigger, newValue => {
      isAnimating.value = newValue;
    });
  }

  const getItemClass = (useBaseClass: boolean = true): string => {
    const transitionClasses = `transition-all ${easing}`;

    if (!isAnimating.value) {
      return cn(transitionClasses, useBaseClass ? baseClass : '', fromClass);
    } else {
      return cn(transitionClasses, useBaseClass ? baseClass : '', toClass);
    }
  };

  const getItemStyle = (index: number): Record<string, string> => {
    if (!isAnimating.value) {
      return {
        transitionDuration: `${duration}ms`,
        transitionDelay: '0ms',
      };
    }

    return {
      transitionDuration: `${duration}ms`,
      transitionDelay: `${index * staggerDelay}ms`,
    };
  };

  const startAnimation = () => {
    isAnimating.value = true;
  };

  const resetAnimation = () => {
    isAnimating.value = false;
  };

  return {
    getItemClass,
    getItemStyle,
    startAnimation,
    resetAnimation,
    isAnimating,
  };
}
