export type AttractionsParams = {
  page?: number;
  size?: number;
  sidoCode?: number;
  gugunCode?: number;
  contentTypeId?: number;
  keyword?: string;
};

export type Attraction = {
  attractionId: number;
  contentType: number;
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
  title: string;
  content: string;
  rating: number;
  isLiked: boolean;
  userId: number;
  userName: string;
  visitedDate: string;
  createdAt: string;
};
