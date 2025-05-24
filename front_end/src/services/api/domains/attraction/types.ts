import { ContentTypeId } from '@/constants/constant';

export type AttractionsParams = {
  page?: number;
  size?: number;
  sidoCode?: number;
  gugunCode?: number;
  contentTypeIds?: ContentTypeId[];
  keyword?: string;
};

export type Sigungu = {
  sidoList: Sido[];
};

export type Sido = {
  sidoCode: number;
  sidoName: string;
  gugunList: Gugun[];
};

export type Gugun = {
  gugunCode: number;
  gugunName: string;
  sidoCode: number;
};

export type Attraction = {
  attractionId: number;
  contentType: ContentTypeId;
  name: string;
  image: string;
  address: string;
  likeCount: number;
  rating: number;
  latitude: number;
  longitude: number;
  review: Review[];
  isLiked: boolean;
};

export type Review = {
  reviewId: number;
  userId: number;
  userName: string;
  title: string;
  content: string;
  rating: number;
  visitedDate: string;
  createdAt: string;
  likeCount: number;
  isLiked: boolean;
};

export type AttractionDetail = Attraction;
