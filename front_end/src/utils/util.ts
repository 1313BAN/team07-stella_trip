interface StarsData {
  full: number;
  half: number;
  empty: number;
}

/**
 * 별점 숫자를 기반으로 꽉 찬 별, 반 별, 빈 별 개수를 계산
 *
 * @param rating - 0부터 5까지의 실수형 별점 값 (벗어나는 경우 0)
 * @returns 별점에 해당하는 별의 구성 정보
 * - full: 꽉찬 별 개수
 * - half: 반쪽 별 개수
 * - empty: 빈 별 개수
 */
export const getStarData = (rating: number): StarsData => {
  if (rating < 0 || rating > 5) rating = 0;
  const fullStars = Math.floor(rating);
  const hasHalfStar = rating % 1 >= 0.5;
  const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

  return {
    full: fullStars,
    half: hasHalfStar ? 1 : 0,
    empty: emptyStars,
  };
};

/**
 * 1,000 이상일 경우 1.2k 형태로 반환
 *
 * @param count 변환할 수
 * @returns 변환된 문자열
 */
export const formatLikeCount = (count: number): string => {
  if (count >= 1000) {
    return `${(count / 1000).toFixed(1)}k`;
  }
  return count.toString();
};

/**
 * 시작일과 종료일을 받아 "몇박 몇일" 형태로 반환
 *
 * @param startDate 시작일 (YYYY-MM-DD 형식)
 * @param endDate 종료일 (YYYY-MM-DD 형식)
 * @returns "2박 3일" 또는 "당일치기" 형태의 문자열
 * @throws 올바르지 않은 날짜 형식이거나 종료일이 시작일보다 이전인 경우
 */
export const calculatePeriod = (startDate: string, endDate: string): string => {
  const start = new Date(startDate);
  const end = new Date(endDate);

  if (isNaN(start.getTime()) || isNaN(end.getTime())) return '';

  const days = Math.floor((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24)) + 1;

  const nights = days - 1;

  if (days <= 0) throw new Error('종료일이 시작일보다 이전입니다');

  if (days === 1) return '당일치기';

  return `${nights}박 ${days}일`;
};
