import { ref, onMounted, onUnmounted, watch, type Ref } from 'vue';

interface UseInViewOptions {
  threshold?: number | number[];
  root?: Element | null;
  rootMargin?: string;
  triggerOnce?: boolean;
}

interface UseInViewReturn {
  isInView: Ref<boolean>;
}

/**
 * 요소가 뷰포트에 들어왔는지 감지하는 훅
 *
 * @param targetRef - 감지할 요소의 ref
 * @param options - 감지 옵션
 * @param options.threshold - 요소가 얼마나 보여야 트리거될지 (0~1, 기본값: 0.1)
 * @param options.root - 감지 기준이 될 조상 요소 (기본값: null, 뷰포트)
 * @param options.rootMargin - root의 마진 설정 (기본값: '0px')
 * @param options.triggerOnce - 한 번만 트리거할지 여부 (기본값: true)
 *
 * @returns
 * - `isInView`: 요소가 뷰포트에 보이는지 여부
 *
 */
const useInView = (
  targetRef: Ref<Element | null>,
  options: UseInViewOptions = {}
): UseInViewReturn => {
  const { threshold = 0.1, root = null, rootMargin = '0px', triggerOnce = true } = options;

  const isInView = ref(false);
  let observer: IntersectionObserver | null = null;

  const setupObserver = () => {
    if (!targetRef.value) return;

    observer = new IntersectionObserver(
      ([entry]) => {
        const isIntersecting = entry.isIntersecting;

        if (isIntersecting) {
          isInView.value = true;

          if (triggerOnce && observer) {
            observer.unobserve(targetRef.value!);
          }
        } else if (!triggerOnce) {
          isInView.value = false;
        }
      },
      {
        threshold,
        root,
        rootMargin,
      }
    );

    observer.observe(targetRef.value);
  };

  const cleanup = () => {
    if (observer && targetRef.value) {
      observer.unobserve(targetRef.value);
      observer.disconnect();
      observer = null;
    }
  };

  watch(targetRef, (newRef, oldRef) => {
    if (oldRef && observer) {
      observer.unobserve(oldRef);
    }
    if (newRef) {
      setupObserver();
    }
  });

  onMounted(() => {
    setupObserver();
  });

  onUnmounted(() => {
    cleanup();
  });

  return {
    isInView,
  };
};

export default useInView;
