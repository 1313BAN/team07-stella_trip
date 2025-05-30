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

export type RawNode = {
  x: number;
  y: number;
};

export type StellaNode = {
  name?: string;
  x: number;
  y: number;
  r?: number;
  brightness?: number;
  delay?: number;
};

export type StellaEdge = {
  from: number;
  to: number;
};

export type StellaData = {
  nodes: StellaNode[];
  edges: StellaEdge[];
};

export type BackgroundStar = {
  x: number;
  y: number;
  r: number;
  brightness: number;
  duration: number;
};

export type Plan = {
  title: string;
  description: string;
  stella: StellaData | null;
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

export type FeaturedTags = {
  featuredTags: Tag[];
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
  stella: StellaData;
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

export type UpdateRouteRequest = {
  routes: RouteInfo[];
};

export type RouteInfo = {
  routeId: number;
  dayIndex: number;
  order: number;
  deleted: boolean;
};
