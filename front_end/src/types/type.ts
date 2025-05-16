export type Attraction = {
  attractionId: number;
  name: string;
  image: string;
  address: string;
  contentType: string;
  rating: number;
  latitude: number;
  longitude: number;
  isLiked: boolean;
  likeCount: number;
};

export type Plan = {
  planId: string;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  tags: string[];
  planWriters: Writer[];
  likeCount: number;
  isLiked: boolean;
  constellation: ConstellationData;
};

export type Writer = {
  userId: number;
  name: string;
};

export type Star = {
  x: number;
  y: number;
  r: number;
  brightness: number;
  delay?: number;
};

export type Connection = {
  from: number;
  to: number;
};

export type ConstellationData = {
  stars: Star[];
  connections: Connection[];
};
