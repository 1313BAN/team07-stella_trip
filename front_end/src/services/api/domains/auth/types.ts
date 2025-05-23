import type { AuthToken, User } from '@/types/type';

export type LoginRequest = {
  email: string;
  password: string;
};

export type LogoutRequest = {
  refreshToken: string;
};

export type LoginResponse = User & AuthToken;

export type SignupRequest = {
  email: string;
  password: string;
  name: string;
};

export type SignupResponse = User;

type AuthActionResponse = {
  success: boolean;
};

export type LogoutResponse = AuthActionResponse;
export type SignoutResponse = AuthActionResponse;
