import type { AttractionContentType } from '@/constants/constant';

export type User = {
  id: string;
  email: string;
  username: string;
};

export type AuthToken = {
  accessToken: string;
  refreshToken: string;
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

export type AttractionContentTypeCode = keyof typeof AttractionContentType;
export type AttractionContentTypeValue = (typeof AttractionContentType)[AttractionContentTypeCode];
