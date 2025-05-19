export type PlansParams = {
  page?: number;
  size?: number;
  search?: string;
  userName?: string;
  duration?: number;
  sort?: 'RECENT' | 'POPULAR';
};

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
