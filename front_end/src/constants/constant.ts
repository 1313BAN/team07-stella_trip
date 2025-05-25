export enum ContentTypeId {
  TOURIST_SPOT = 12, // 관광지
  CULTURAL_FACILITY = 14, // 문화시설
  FESTIVAL_EVENT = 15, // 축제공연행사
  TRAVEL_COURSE = 25, // 여행코스
  LEISURE_SPORTS = 28, // 레포츠
  ACCOMMODATION = 32, // 숙박
  SHOPPING = 38, // 쇼핑
  TOURISM_SITE = 39, // 관광지
}

// ContentTypeId에 대한 한글 이름 매핑
export const contentTypeNameKR: Record<ContentTypeId, string> = {
  [ContentTypeId.TOURIST_SPOT]: '관광지',
  [ContentTypeId.CULTURAL_FACILITY]: '문화시설',
  [ContentTypeId.FESTIVAL_EVENT]: '축제공연행사',
  [ContentTypeId.TRAVEL_COURSE]: '여행코스',
  [ContentTypeId.LEISURE_SPORTS]: '레포츠',
  [ContentTypeId.ACCOMMODATION]: '숙박',
  [ContentTypeId.SHOPPING]: '쇼핑',
  [ContentTypeId.TOURISM_SITE]: '관광지',
};

export const contentTypeList = Object.entries(ContentTypeId)
  .filter(([key, value]) => typeof value === 'number') // 숫자 key만
  .map(([key, value]) => ({
    label: key,
    value: value,
  }));
