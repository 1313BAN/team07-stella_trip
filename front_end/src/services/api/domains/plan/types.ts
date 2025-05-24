export type PlansParams = {
  page?: number;
  size?: number;
  search?: string;
  userName?: string;
  minDuration?: number;
  maxDuration?: number;
  sort?: PlansSortOption;
};

export type PlansSortOption = 'recent' | 'like' | '';

export type Plan = {
  title: string;
  description: string;
  stella: null;
  tags: Tag[];
  planId: number;
  isLiked: boolean;
  startDate: string;
  endDate: string;
  likeCount: number;
  isPublic: boolean;
  planWriters: Writer[];
};

export type Tag = {
  tagId: number;
  name: string;
  count: number;
};

export type Writer = {
  userId: number;
  name: string;
};

export type RouteAttraction = {
  order: number;
  name: string;
  image: string;
  address: string;
  latitude: number;
  longitude: number;
  memo: string | null;
  routeId: number;
  attractionId: number;
  contentTypeId: number;
  likeCount: number;
  rating: number;
  visitTime: string;
};

export type PlanDetails = Record<string, RouteAttraction[]>;

export type PlanDetail = {
  planId: number;
  title: string;
  description: string | null;
  stella: {
    stars: Array<{
      x: number;
      y: number;
      r: number;
      brightness: number;
      delay: number;
    }>;
    connections: Array<{
      from: number;
      to: number;
    }>;
  } | null;
  likeCount: number;
  tags: Tag[];
  isLiked: boolean;
  details: PlanDetails;
  isPublic: boolean;
  startDate: string;
  endDate: string;
  planWriters: Writer[];
};

export type CreatePlanRequest = UpdatePlanInfoRequest & UpdatePlanScheduleRequest;

//TODO: 타입 일관화 필요
export type PlanWriter = {
  id: number;
  name: string;
  profileImageUrl?: string;
};

export type PlanTag = {
  id: number;
  name: string;
};

export type PlanAttractionRequest = {
  dayIndex: number;
  attractionId: number;
  visitTime: string;
  memo: string;
};

export type UpdatePlanInfoRequest = {
  title: string;
  description: string;
  isPublic: boolean;
  tags: string[];
};

export type UpdatePlanScheduleRequest = {
  startDate: string;
  endDate: string;
};

export type LockResponse = {
  userId: number;
  planId: number;
  lockStatus: boolean;
};
