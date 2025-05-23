import { ref } from 'vue';

/**
 * 스크롤 동작을 감지하고 관리하는 컴포저블
 *
 * @param scrollThreshold - 스크롤 임계값 (픽셀 단위, 기본값: 30)
 *
 * @returns
 * - `isScrollingDown`: 아래로 스크롤 중인지 여부
 * - `scrollY`: 현재 스크롤 Y 위치
 * - `handleScroll`: 스크롤 이벤트 핸들러
 * - `register`: 스크롤 이벤트 리스너 등록
 * - `unregister`: 스크롤 이벤트 리스너 제거
 * - `cleanup`: 리소스 정리 (언마운트 시 사용)
 */
export function useScroll(scrollThreshold = 30) {
  const isScrollingDown = ref(false);
  const scrollY = ref(0);
  const lastScrollTop = ref(0);
  const hasRegisteredListener = ref(false);
  let target: HTMLElement | null = null;
  let handler: ((e: Event) => void) | null = null;

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

  const register = (element: HTMLElement, callback?: () => void) => {
    if (hasRegisteredListener.value) {
      unregister();
    }

    target = element;
    handler = (e: Event) => handleScroll(e, callback);
    target.addEventListener('scroll', handler);
    hasRegisteredListener.value = true;
  };

  const unregister = () => {
    if (hasRegisteredListener.value && target && handler) {
      target.removeEventListener('scroll', handler);
      hasRegisteredListener.value = false;
      target = null;
      handler = null;
    }
  };

  const cleanup = () => {
    unregister();
  };

  return {
    isScrollingDown,
    scrollY,
    handleScroll,
    register,
    unregister,
    cleanup,
  };
}
