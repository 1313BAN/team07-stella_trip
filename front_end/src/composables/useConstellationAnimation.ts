import { ref, reactive, watch, onUnmounted, nextTick, readonly, computed } from 'vue';

export interface ConstellationAnimationOptions {
  /**
   * 애니메이션 지속 시간 (ms)
   * @default 4000
   */
  duration?: number;

  /**
   * DOM 로드 대기 시간 (ms)
   * @default 200
   */
  loadDelay?: number;

  /**
   * 트랜지션 활성화 지연 시간 (ms)
   * @default 100
   */
  transitionDelay?: number;

  /**
   * 애니메이션 완료 후 콜백
   */
  onComplete?: () => void;
}

export const useConstellationAnimation = (options: ConstellationAnimationOptions = {}) => {
  const { duration = 4000, loadDelay = 200, transitionDelay = 100, onComplete } = options;

  // 애니메이션 상태
  const isCenter = ref(false);
  const showOverlay = ref(false);
  const isPositioned = ref(false);
  const constellationRef = ref<HTMLDivElement | null>(null);
  const containerRef = ref<HTMLDivElement | null>(null);

  // Transform 스타일
  const transformStyle = reactive({
    '--translate-x': '0px',
    '--translate-y': '0px',
  });

  // 타이머 정리용
  const timeouts = new Set<number>();

  /**
   * 타이머 관리 함수들
   */
  const addTimeout = (callback: () => void, delay: number): void => {
    const id = window.setTimeout(() => {
      timeouts.delete(id);
      callback();
    }, delay);
    timeouts.add(id);
  };

  const clearAllTimeouts = (): void => {
    timeouts.forEach(id => clearTimeout(id));
    timeouts.clear();
  };

  /**
   * 바디 스크롤 제어
   */
  const setBodyOverflow = (hidden: boolean): void => {
    document.body.style.overflow = hidden ? 'hidden' : '';
  };

  // showOverlay 상태가 변경될 때마다 body의 overflow 속성 조절
  watch(showOverlay, setBodyOverflow, { immediate: true });

  /**
   * 중앙 위치 계산
   */
  const calculateCenterPosition = () => {
    const constellation = constellationRef.value;
    const container = containerRef.value;

    if (!constellation || !container) return false;

    // 컴포넌트의 현재 위치 계산
    const rect = constellation.getBoundingClientRect();

    // 화면 중앙 계산
    const centerX = window.innerWidth / 2;
    const centerY = window.innerHeight / 2;

    // 컴포넌트 중심점 계산
    const componentCenterX = rect.left + rect.width / 2;
    const componentCenterY = rect.top + rect.height / 2;

    // 중앙으로 이동하기 위한 transform 오프셋 계산
    const deltaX = centerX - componentCenterX;
    const deltaY = centerY - componentCenterY;

    // CSS 변수에 적용
    transformStyle['--translate-x'] = `${deltaX}px`;
    transformStyle['--translate-y'] = `${deltaY}px`;

    return true;
  };

  /**
   * 애니메이션 시작
   */
  const startAnimation = async (): Promise<void> => {
    // 기존 타이머들 정리
    clearAllTimeouts();

    // DOM 업데이트가 완료될 때까지 기다림
    await nextTick();

    // 처음부터 어두운 배경과 스크롤 막기
    showOverlay.value = true;

    // DOM이 완전히 로드된 후 위치 계산
    addTimeout(() => {
      const success = calculateCenterPosition();
      if (!success) return;

      // 즉시 중앙 위치로 설정 (애니메이션 없이)
      isCenter.value = true;

      // 잠깐 후 transition 활성화
      addTimeout(() => {
        isPositioned.value = true;
      }, transitionDelay);
    }, loadDelay);

    // 지정된 시간 후 원래 위치로 애니메이션
    addTimeout(() => {
      isCenter.value = false;
      showOverlay.value = false;

      // 애니메이션 완료 콜백 실행
      if (onComplete) {
        addTimeout(onComplete, 2500); // transition 완료 후
      }
    }, duration);
  };

  /**
   * 애니메이션 중지 및 초기화
   */
  const stopAnimation = (): void => {
    clearAllTimeouts();
    isCenter.value = false;
    showOverlay.value = false;
    isPositioned.value = false;
    setBodyOverflow(false);

    // Transform 초기화
    transformStyle['--translate-x'] = '0px';
    transformStyle['--translate-y'] = '0px';
  };

  /**
   * 애니메이션 상태 리셋
   */
  const resetAnimation = (): void => {
    stopAnimation();
    // 초기 상태로 되돌림
    isCenter.value = false;
    showOverlay.value = false;
    isPositioned.value = false;
  };

  // 컴포넌트가 언마운트될 때 정리
  onUnmounted(() => {
    clearAllTimeouts();
    setBodyOverflow(false);
  });

  return {
    // 상태
    isCenter: readonly(isCenter),
    showOverlay: readonly(showOverlay),
    isPositioned: readonly(isPositioned),
    transformStyle: readonly(transformStyle),

    // Refs
    constellationRef,
    containerRef,

    // 메서드
    startAnimation,
    stopAnimation,
    resetAnimation,

    // 계산된 클래스명 (템플릿에서 사용하기 편하도록)
    constellationClasses: computed(() => ({
      'center-position': isCenter.value,
      'with-transition': isPositioned.value,
      'no-transition': isCenter.value && !isPositioned.value,
    })),
  };
};
