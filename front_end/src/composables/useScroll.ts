import { ref } from 'vue';

export function useScroll(scrollThreshold = 30) {
  const isScrollingDown = ref(false);
  const scrollY = ref(0);
  const lastScrollTop = ref(0);

  const handleScroll = (event: Event, onScrollChange?: () => void) => {
    const target = event.target as HTMLElement;
    const currentScrollTop = target.scrollTop;

    // 스크롤 방향 감지
    isScrollingDown.value = currentScrollTop > lastScrollTop.value;
    scrollY.value = currentScrollTop;

    // 스크롤 거리 계산
    const scrollDistance = Math.abs(currentScrollTop - lastScrollTop.value);

    // 임계값 이상 스크롤되면 콜백 실행
    if (scrollDistance > scrollThreshold && onScrollChange) {
      onScrollChange();
    }

    // 포커스 해제
    if (document.activeElement instanceof HTMLElement) {
      document.activeElement.blur();
    }

    lastScrollTop.value = currentScrollTop;
  };

  return {
    isScrollingDown,
    scrollY,
    handleScroll,
  };
}
