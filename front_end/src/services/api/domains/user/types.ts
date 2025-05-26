export type UserInfo = {
  name: string;
  email: string;
  description: string;
  image: string;
  followerCount: number;
  followingCount: number;
};

export type BasicUserInfo = {
  name: string;
  description: string;
  image: string;
};

export type Password = {
  password: string;
};
