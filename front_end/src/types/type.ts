export type User = {
  id: number;
  email: string;
  name: string;
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
