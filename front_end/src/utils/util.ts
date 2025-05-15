export interface StarsData {
  full: number;
  half: number;
  empty: number;
}

/**
 * 별점 숫자를 기반으로 꽉 찬 별, 반 별, 빈 별 개수를 계산
 *
 * @param rating - 0부터 5까지의 실수형 별점 값
 * @returns 별점에 해당하는 별의 구성 정보
 * - full: 꽉찬 별 개수
 * - half: 반쪽 별 개수
 * - empty: 빈 별 개수
 */
export const getStarData = (rating: number): StarsData => {
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
